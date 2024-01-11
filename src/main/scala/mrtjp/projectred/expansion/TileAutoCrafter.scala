/*
 * Copyright (c) 2015.
 * Created by MrTJP.
 * All rights reserved.
 */
package mrtjp.projectred.expansion

import codechicken.lib.data.MCDataInput
import codechicken.lib.render.uv.{MultiIconTransformation, UVTransformation}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mrtjp.core.gui._
import mrtjp.core.inventory.{InvWrapper, TInventory}
import mrtjp.core.item.{ItemEquality, ItemKey, ItemKeyStack, ItemQueue}
import mrtjp.core.render.TCubeMapRender
import mrtjp.core.world.WorldLib
import mrtjp.projectred.ProjectRedExpansion
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{ISidedInventory, InventoryCrafting}
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{CraftingManager, IRecipe}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import net.minecraftforge.oredict.{ShapedOreRecipe, ShapelessOreRecipe}
import org.apache.commons.lang3.tuple.{ImmutableTriple, Triple}

import java.util.{List => JList}
import scala.collection.JavaConversions._

class TileAutoCrafter
    extends TileMachine
    with TPoweredMachine
    with TInventory
    with ISidedInventory
    with TGuiMachine {
  var planSlot = 0

  var currentRecipe: IRecipe = null
  var currentInputs = new ItemQueue
  var currentOutput: ItemKeyStack = null

  private val invCrafting = new InventoryCrafting(new NodeContainer, 3, 3)
  private var recipeNeedsRefresh = true
  private var cycleTimer1 = getUnpoweredCycleTimer
  private var cycleTimer2 = getPoweredCycleTimer

  override def save(tag: NBTTagCompound) {
    super.save(tag)
    saveInv(tag)
    tag.setInteger("cyt1", cycleTimer1)
    tag.setInteger("cyt2", cycleTimer2)
  }

  override def load(tag: NBTTagCompound) {
    super.load(tag)
    loadInv(tag)
    cycleTimer1 = tag.getInteger("cyt1")
    cycleTimer2 = tag.getInteger("cyt2")
  }

  override def read(in: MCDataInput, key: Int) = key match {
    case 2 => cyclePlanSlot()
    case _ => super.read(in, key)
  }

  def sendCyclePlanSlot() {
    writeStream(2).sendToServer()
  }

  def getUnpoweredCycleTimer = 40
  def getPoweredCycleTimer = 10
  def getCraftsPerPowerCycle = 5

  override def getBlock = ProjectRedExpansion.machine2

  override def doesOrient = false
  override def doesRotate = false

  /** 0 until 9 - Plans 9 until 27 - Storage
    */
  override def size = 27
  override def name = "auto_bench"

  override def canExtractItem(slot: Int, item: ItemStack, side: Int) =
    9 until 27 contains slot
  override def canInsertItem(slot: Int, item: ItemStack, side: Int) =
    9 until 27 contains slot
  override def getAccessibleSlotsFromSide(side: Int) = (9 until 27).toArray

  override def update() {
    super.update()

    if (recipeNeedsRefresh) {
      refreshRecipe()
      recipeNeedsRefresh = false
    }

    if (cond.canWork) {
      cycleTimer2 -= 1
      if (cycleTimer2 % (getPoweredCycleTimer / getCraftsPerPowerCycle) == 0)
        if (tryCraft()) cond.drawPower(1000)
      if (cycleTimer2 <= 0) {
        cycleTimer2 = getPoweredCycleTimer
        cyclePlanSlot()
        cond.drawPower(100)
      }
    } else {
      cycleTimer1 -= 1
      if (cycleTimer1 <= 0) {
        cycleTimer1 = getUnpoweredCycleTimer
        tryCraft()
      }
    }
  }

  def cyclePlanSlot() {
    val start = planSlot
    do planSlot = (planSlot + 1) % 9 while (planSlot != start && getStackInSlot(
      planSlot
    ) == null)
    if (planSlot != start) refreshRecipe()
  }

  def refreshRecipe() {
    currentRecipe = null
    currentInputs.clear()
    currentOutput = null

    val plan = getStackInSlot(planSlot)
    if (plan != null && ItemPlan.hasRecipeInside(plan)) {
      val inputs = ItemPlan.loadPlanInputs(plan)
      for (i <- 0 until 9) invCrafting.setInventorySlotContents(i, inputs(i))
      val recipes =
        CraftingManager.getInstance().getRecipeList.asInstanceOf[JList[IRecipe]]
      currentRecipe = recipes.find(_.matches(invCrafting, world)).orNull
      if (currentRecipe != null) {
        inputs
          .map { ItemKey.getOrNull }
          .filter(_ != null)
          .foreach(currentInputs.add(_, 1))
        currentOutput =
          ItemKeyStack.getOrNull(currentRecipe.getCraftingResult(invCrafting))
      }
    }
  }

  override def markDirty() {
    super.markDirty()
    recipeNeedsRefresh = true
  }

  def tryCraft(): Boolean = {
    if (currentRecipe != null && checkSpaceForOutput)
      if (
        currentInputs.result.forall(p => containsEnoughResource(p._1, p._2))
      ) {
        for ((item, amount) <- currentInputs.result)
          eatResource(item, amount)
        produceOutput()
        return true
      }
    false
  }

  def containsEnoughResource(item: ItemKey, amount: Int): Boolean = {
    val eq = new ItemEquality
    eq.matchMeta = !item.makeStack(0).isItemStackDamageable
    eq.matchNBT = false
    eq.matchOre = currentRecipe.isInstanceOf[ShapedOreRecipe] || currentRecipe
      .isInstanceOf[ShapelessOreRecipe]

    var found = 0
    for (i <- 9 until 27) {
      val s = getStackInSlot(i)
      if (s != null && eq.matches(item, ItemKey.get(s))) {
        found += s.stackSize
        if (found >= amount) return true
      }
    }
    false
  }

  def checkSpaceForOutput = {
    val w =
      InvWrapper.wrap(this).setInternalMode(true).setSlotsFromRange(9 until 27)
    w.getSpaceForItem(currentOutput.key) >= currentOutput.stackSize
  }

  def produceOutput() {
    val w =
      InvWrapper.wrap(this).setInternalMode(true).setSlotsFromRange(9 until 27)
    w.injectItem(currentOutput.key, currentOutput.stackSize)
  }

  def eatResource(item: ItemKey, amount: Int) {
    val eq = new ItemEquality
    eq.matchMeta = !item.makeStack(0).isItemStackDamageable
    eq.matchNBT = false
    eq.matchOre = currentRecipe.isInstanceOf[ShapedOreRecipe] || currentRecipe
      .isInstanceOf[ShapelessOreRecipe]

    var left = amount
    for (i <- 9 until 27) {
      val s = getStackInSlot(i)
      if (s != null && eq.matches(item, ItemKey.get(s))) {
        if (s.getItem.hasContainerItem(s)) {
          val cStack = s.getItem.getContainerItem(s)
          setInventorySlotContents(i, cStack)
          left -= 1
        } else {
          val toRem = math.min(s.stackSize, left)
          s.stackSize -= toRem
          left -= toRem
          if (s.stackSize <= 0) setInventorySlotContents(i, null)
          else markDirty()
        }

        if (left <= 0) return
      }
    }
  }

  override def onBlockRemoval() {
    super.onBlockRemoval()
    dropInvContents(world, xCoord, yCoord, zCoord)
  }

  override def openGui(player: EntityPlayer) {
    GuiAutoCrafter.open(player, createContainer(player), _.writeCoord(xCoord, yCoord, zCoord))
  }

  override def createContainer(player: EntityPlayer) =
    new ContainerAutoCrafter(player, this)
}

object RenderAutoCrafter extends TCubeMapRender {
  var bottom: IIcon = _
  var top: IIcon = _
  var side1: IIcon = _
  var side2: IIcon = _

  var iconT: UVTransformation = _

  override def getData(w: IBlockAccess, x: Int, y: Int, z: Int): Triple[Integer, Integer, UVTransformation] = new ImmutableTriple(0, 0, iconT)

  override def getInvData = new ImmutableTriple(0, 0, iconT)

  override def getIcon(side: Int, meta: Int) = side match {
    case 0 => bottom
    case 1 => top
    case _ => side1
  }

  override def registerIcons(reg: IIconRegister) {
    bottom = reg.registerIcon("projectred:mechanical/autobench/bottom")
    top = reg.registerIcon("projectred:mechanical/autobench/top")
    side1 = reg.registerIcon("projectred:mechanical/autobench/side1")
    side2 = reg.registerIcon("projectred:mechanical/autobench/side2")

    iconT = new MultiIconTransformation(bottom, top, side1, side1, side2, side2)
  }
}
