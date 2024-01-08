package mrtjp.projectred.exploration

import codechicken.microblock.Saw
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mrtjp.core.block.TItemSeed
import mrtjp.core.color.Colors
import mrtjp.core.gui.{GuiLib, NodeContainer, Slot3}
import mrtjp.core.inventory.TInventory
import mrtjp.core.item.ItemCore
import mrtjp.core.world.WorldLib
import mrtjp.projectred.ProjectRedExploration
import mrtjp.projectred.core.{ItemCraftingDamage, PartDefs}
import mrtjp.projectred.exploration.ArmorDefs.ArmorDef
import mrtjp.projectred.exploration.ToolDefs.ToolDef
import net.minecraft.block._
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.Item.ToolMaterial.{EMERALD => toolMaterialEmerald, GOLD => toolMaterialGold, IRON => toolMaterialIron, STONE => toolMaterialStone, WOOD => toolMaterialWood}
import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.item._
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{EnumChatFormatting, IIcon}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.EnumPlantType
import org.lwjgl.input.Keyboard

import java.util.{List => JList}

class ItemBackpack extends ItemCore("projectred.exploration.backpack") {
  setHasSubtypes(true)
  setMaxStackSize(1)
  setCreativeTab(ProjectRedExploration.tabExploration)

  override def onItemUse(
      stack: ItemStack,
      player: EntityPlayer,
      world: World,
      x: Int,
      y: Int,
      z: Int,
      par7: Int,
      par8: Float,
      par9: Float,
      par10: Float
  ) = {
    openGui(player)
    true
  }

  override def onItemRightClick(
      stack: ItemStack,
      w: World,
      player: EntityPlayer
  ) = {
    openGui(player)
    super.onItemRightClick(stack, w, player)
  }

  def openGui(player: EntityPlayer) {
    GuiBackpack.open(player, ItemBackpack.createContainer(player))
  }

  override def getSubItems(item: Item, tab: CreativeTabs, list: JList[ItemStack]) {
    for (i <- 0 until 16)
      list.add(new ItemStack(this, 1, i))
  }

  private val icons = new Array[IIcon](16)
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister) {
    for (i <- 0 until 16)
      icons(i) = reg.registerIcon("projectred:world/backpack_" + i)
  }

  override def getIconFromDamage(meta: Int) = icons(meta)

  override def addInformation(
      stack: ItemStack,
      player: EntityPlayer,
      list: JList[String],
      flag: Boolean
  ) {
    if (
      Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(
        Keyboard.KEY_RSHIFT
      )
    )
      list.add(
          EnumChatFormatting.GRAY.toString + (if (ItemBackpack.hasBagInv(stack))
                                                ItemBackpack
                                                  .getNumberOfItems(stack)
                                              else 0) + "/27 slots used"
        )
  }
}

object ItemBackpack {
  val oreDictionaryVal = "prbackpack"

  def createContainer(player: EntityPlayer) =
    new ContainerBackpack(new BagInventory(player), player)

  def hasBagInv(stack: ItemStack) = {
    stack.hasTagCompound && stack.getTagCompound.hasKey("baginv")
  }

  def getBagTag(stack: ItemStack) = {
    stack.getTagCompound.getCompoundTag("baginv")
  }

  def saveBagTag(stack: ItemStack, tag: NBTTagCompound) {
    stack.getTagCompound.setTag("baginv", tag)
  }

  def getNumberOfItems(stack: ItemStack) = {
    getBagTag(stack).getTagList("items", 10).tagCount()
  }
}

class ContainerBackpack(inv: BagInventory, player: EntityPlayer)
    extends NodeContainer {
  {
    for (((x, y), i) <- GuiLib.createSlotGrid(8, 18, 9, 3, 0, 0).zipWithIndex) {
      val s = new Slot3(inv, i, x, y)
      addSlotToContainer(s)
    }
    addPlayerInv(player, 8, 86)

    slots(player.inventory.currentItem + 27).canRemoveDelegate = { () => false }
  }
}

class BagInventory(player: EntityPlayer) extends TInventory {
  override def size = 27
  override def stackLimit = 64
  override def name = ""

  loadInventory()

  private def loadInventory() {
    if (closeIfNoBag()) return
    loadInv(ItemBackpack.getBagTag(player.getHeldItem))
  }

  private def saveInventory() {
    if (closeIfNoBag()) return
    val tag = new NBTTagCompound
    saveInv(tag)
    ItemBackpack.saveBagTag(player.getHeldItem, tag)
  }

  override def markDirty() {
    saveInventory()
  }

  private def closeIfNoBag() = {
    val bag = player.getHeldItem
    val hasBag =
      bag != null && bag.getItem == ProjectRedExploration.itemBackpack
    if (hasBag) {
      if (!bag.hasTagCompound) bag.setTagCompound(new NBTTagCompound)
    } else player.closeScreen()
    !hasBag
  }

  override def isItemValidForSlot(i: Int, stack: ItemStack): Boolean = {
    if (stack != null) {
      if (stack.getItem == ProjectRedExploration.itemBackpack) return false
      // for (blocked <- Configurator.backpackBlacklist) if (stack.itemID == blocked) return false
      // TODO backpack blacklist
      return true
    }
    false
  }

  override def getStackInSlotOnClosing(slot: Int) =
    if (closeIfNoBag()) null
    else super.getStackInSlotOnClosing(slot)

  override def decrStackSize(slot: Int, count: Int) =
    if (closeIfNoBag()) null
    else super.decrStackSize(slot, count)

  override def setInventorySlotContents(slot: Int, item: ItemStack) {
    if (!closeIfNoBag()) super.setInventorySlotContents(slot, item)
  }

  override def dropInvContents(w: World, x: Int, y: Int, z: Int) {
    if (!closeIfNoBag()) super.dropInvContents(w, x, y, z)
  }
}

trait TGemTool extends Item {
  setUnlocalizedName("projectred.exploration." + toolDef.unlocal)
  setTextureName("projectred:world/" + toolDef.unlocal)
  setCreativeTab(ProjectRedExploration.tabExploration)
  GameRegistry.registerItem(this, "projectred.exploration." + toolDef.unlocal)

  def toolDef: ToolDef

  override def getIsRepairable(ist1: ItemStack, ist2: ItemStack) = {
    if (toolDef.repair.isItemEqual(ist2)) true
    else false // super.getIsRepairable(ist1, ist2)
  }
}

import mrtjp.projectred.exploration.ItemToolProxies._
class ItemGemAxe(override val toolDef: ToolDef)
    extends Axe(toolDef.mat)
    with TGemTool
class ItemGemPickaxe(override val toolDef: ToolDef)
    extends Pickaxe(toolDef.mat)
    with TGemTool
class ItemGemShovel(override val toolDef: ToolDef)
    extends Shovel(toolDef.mat)
    with TGemTool
class ItemGemSword(override val toolDef: ToolDef)
    extends Sword(toolDef.mat)
    with TGemTool
class ItemGemHoe(override val toolDef: ToolDef)
    extends ItemHoe(toolDef.mat)
    with TGemTool

class ItemGemSaw(val toolDef: ToolDef)
    extends ItemCraftingDamage("projectred.exploration." + toolDef.unlocal)
    with Saw {
  setMaxDamage(toolDef.mat.getMaxUses)
  setCreativeTab(ProjectRedExploration.tabExploration)

  override def getCuttingStrength(item: ItemStack) = toolDef.mat.getHarvestLevel
  override def registerIcons(reg: IIconRegister) {}
}

class ItemGemSickle(override val toolDef: ToolDef)
    extends ItemTool(3, toolDef.mat, new java.util.HashSet)
    with TGemTool {
  private val radiusLeaves = 1
  private val radiusCrops = 2

  override def func_150893_a(stack: ItemStack, b: Block) = {
    if (b.isInstanceOf[BlockLeaves]) efficiencyOnProperMaterial
    else super.func_150893_a(stack, b)
  }

  override def onBlockDestroyed(
      stack: ItemStack,
      w: World,
      b: Block,
      x: Int,
      y: Int,
      z: Int,
      ent: EntityLivingBase
  ): Boolean = {
    val player = ent match {
      case p: EntityPlayer => p
      case _               => null
    }

    if (player != null && b != null) {
      if (WorldLib.isLeafType(w, x, y, z, b))
        return runLeaves(stack, w, x, y, z, player)
      else if (WorldLib.isPlantType(w, x, y, z, b))
        return runCrops(stack, w, x, y, z, player)
    }
    super.onBlockDestroyed(stack, w, b, x, y, z, ent)
  }

  private def runLeaves(
      stack: ItemStack,
      w: World,
      x: Int,
      y: Int,
      z: Int,
      player: EntityPlayer
  ) = {
    var used = false
    for (i <- -radiusLeaves to radiusLeaves)
      for (j <- -radiusLeaves to radiusLeaves)
        for (k <- -radiusLeaves to radiusLeaves) {
          val lx = x + i
          val ly = y + j
          val lz = z + k
          val b = w.getBlock(lx, ly, lz)
          val meta = w.getBlockMetadata(lx, ly, lz)
          if (b != null && WorldLib.isLeafType(w, x, y, z, b)) {
            if (b.canHarvestBlock(player, meta))
              b.harvestBlock(w, player, lx, ly, lz, meta)
            w.setBlockToAir(lx, ly, lz)
            used = true
          }
        }

    if (used) stack.damageItem(1, player)
    used
  }

  private def runCrops(
      stack: ItemStack,
      w: World,
      x: Int,
      y: Int,
      z: Int,
      player: EntityPlayer
  ) = {
    var used = false
    for (i <- -radiusCrops to radiusCrops)
      for (j <- -radiusCrops to radiusCrops) {
        val lx = x + i
        val ly = y
        val lz = z + j
        val b = w.getBlock(lx, ly, lz)
        val meta = w.getBlockMetadata(lx, ly, lz)
        if (b != null && WorldLib.isPlantType(w, x, y, z, b)) {
          if (b.canHarvestBlock(player, meta))
            b.harvestBlock(w, player, lx, ly, lz, meta)
          w.setBlockToAir(lx, ly, lz)
          used = true
        }
      }

    if (used) stack.damageItem(1, player)
    used
  }
}

class ItemGemArmor(adef: ArmorDef, atype: Int)
    extends ItemToolProxies.Armor(adef.mat, atype) {
  setUnlocalizedName("projectred.exploration." + adef.unlocal)
  setTextureName("projectred:world/" + adef.unlocal)
  setCreativeTab(ProjectRedExploration.tabExploration)
  GameRegistry.registerItem(this, "projectred.exploration." + adef.unlocal)

  override def getIsRepairable(ist1: ItemStack, ist2: ItemStack) = {
    if (adef.repair.isItemEqual(ist2)) true
    else false
  }

  override def getArmorTexture(
      stack: ItemStack,
      entity: Entity,
      slot: Int,
      `type`: String
  ) = {
    val suffix = if (armorType == 2) 2 else 1
    "projectred:textures/items/world/" + adef.tex + "_" + suffix + ".png"
  }
}

class ItemWoolGin extends ItemCraftingDamage("projectred.exploration.woolgin") {
  setMaxDamage(128)
  setCreativeTab(ProjectRedExploration.tabExploration)
  setTextureName("projectred:world/wool_gin")
}

class ItemLilySeeds
    extends ItemCore("projectred.exploration.lilyseed")
    with TItemSeed {
  setHasSubtypes(true)
  setTextureName("projectred:world/lily_seed")
  setCreativeTab(ProjectRedExploration.tabExploration)

  override def getPlantBlock = (ProjectRedExploration.blockLily, 0)
  override def getPlantType(w: IBlockAccess, x: Int, y: Int, z: Int) =
    EnumPlantType.Plains

  override def getSubItems(item: Item, tab: CreativeTabs, list: JList[ItemStack]) {
    for (i <- 0 until 16)
      list.add(new ItemStack(this, 1, i))
  }

  override def getColorFromItemStack(item: ItemStack, meta: Int) = Colors(
    item.getItemDamage
  ).rgb

  override def onPlanted(item: ItemStack, w: World, x: Int, y: Int, z: Int) {
    if (!w.isRemote) w.getTileEntity(x, y, z) match {
      case te: TileLily => te.setupPlanted(item.getItemDamage)
      case _            =>
    }
  }
}

import com.google.common.collect.{HashMultimap, Multimap}
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.util.DamageSource
class ItemAthame() extends ItemSword(toolMaterialEmerald) {
  private var damage: Float = _

  setUnlocalizedName("projectred.exploration.athame")
  setMaxDamage(100)
  setTextureName("projectred:world/athame")
  setCreativeTab(ProjectRedExploration.tabExploration)
  GameRegistry.registerItem(this, "projectred.exploration.athame")

  override def func_150893_a(stack: ItemStack, block: Block): Float = 1.0f

  override def func_150931_i(): Float = damage

  override def hitEntity(
      stack: ItemStack,
      entity: EntityLivingBase,
      player: EntityLivingBase
  ): Boolean = {
    damage = toolMaterialEmerald.getDamageVsEntity

    if ((entity.isInstanceOf[EntityEnderman])) damage = 25.0f else damage = 1.0f

    val damageSource =
      DamageSource.causePlayerDamage(player.asInstanceOf[EntityPlayer])
    entity.attackEntityFrom(damageSource, damage)

    super.hitEntity(stack, entity, player)
  }

  override def getIsRepairable(stack: ItemStack, stack1: ItemStack): Boolean = {
    stack1.isItemEqual(PartDefs.SILVERINGOT.makeStack)
  }

  override def getItemEnchantability(): Int = 30

  override def getItemAttributeModifiers() = {
    val damageModifier =
      HashMultimap.create().asInstanceOf[Multimap[String, AttributeModifier]]
    damageModifier.put(
      SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName,
      new AttributeModifier(Item.field_111210_e, "Weapon modifier", 0, 0)
    )
    damageModifier
  }
}
