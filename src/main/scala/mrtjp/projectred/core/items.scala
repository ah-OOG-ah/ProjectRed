package mrtjp.projectred.core

import cpw.mods.fml.relauncher.{Side, SideOnly}
import mrtjp.core.item.ItemCore
import mrtjp.projectred.ProjectRedCore
import mrtjp.projectred.api.IScrewdriver
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{EnumChatFormatting, IIcon}
import net.minecraft.world.World
import org.lwjgl.input.Keyboard

import java.util.{List => JList, Set => JSet}
import scala.collection.JavaConversions

abstract class ItemCraftingDamage(name: String) extends ItemCore(name) {
  setMaxStackSize(1)
  setNoRepair()

  override def hasContainerItem(itemStack: ItemStack) = true

  override def getContainerItem(stack: ItemStack) =
    if (stack.getItem == this) {
      stack.setItemDamage(stack.getItemDamage + 1)
      stack
    } else {
      val newStack = new ItemStack(this)
      newStack.setItemDamage(newStack.getMaxDamage)
      newStack
    }

  override def doesContainerItemLeaveCraftingGrid(stack: ItemStack) = false
}

class ItemScrewdriver
    extends ItemCore("projectred.core.screwdriver")
    with IScrewdriver {
  setMaxStackSize(1)
  setMaxDamage(128)
  setNoRepair()
  setCreativeTab(ProjectRedCore.tabCore)
  setTextureName("projectred:base/screwdriver")

  override def onItemUse(
      stack: ItemStack,
      player: EntityPlayer,
      w: World,
      x: Int,
      y: Int,
      z: Int,
      side: Int,
      par8: Float,
      par9: Float,
      par10: Float
  ) = false

  override def doesSneakBypassUse(
      world: World,
      x: Int,
      y: Int,
      z: Int,
      player: EntityPlayer
  ) = true

  override def canUse(player: EntityPlayer, stack: ItemStack) = true

  override def damageScrewdriver(player: EntityPlayer, stack: ItemStack) {
    stack.damageItem(1, player)
  }
}

class ItemWireDebugger extends ItemCore("projectred.core.wiredebugger") {
  setMaxStackSize(1)
  setMaxDamage(256)
  setNoRepair()
  setCreativeTab(ProjectRedCore.tabCore)

  override def onItemUse(
      stack: ItemStack,
      player: EntityPlayer,
      w: World,
      x: Int,
      y: Int,
      z: Int,
      side: Int,
      par8: Float,
      par9: Float,
      par10: Float
  ) = false

  override def doesSneakBypassUse(
      world: World,
      x: Int,
      y: Int,
      z: Int,
      player: EntityPlayer
  ) = true

  override def onItemUseFirst(
      par1ItemStack: ItemStack,
      player: EntityPlayer,
      par3World: World,
      par4: Int,
      par5: Int,
      par6: Int,
      par7: Int,
      par8: Float,
      par9: Float,
      par10: Float
  ) = false

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister) {
    itemIcon = reg.registerIcon("projectred:base/multimeter")
  }
}

class ItemDataCard extends ItemCore("projectred.core.datacard") {
  setCreativeTab(ProjectRedCore.tabCore)
  setMaxStackSize(1)

  private val icons = new Array[IIcon](2)

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister) {
    for (i <- Seq(0, 1))
      icons(i) = reg.registerIcon("projectred:base/data_card_" + i)
  }

  import ItemDataCard._

  // override def getIcon(stack:ItemStack, pass:Int) = if (hasData(stack)) icons(1) else icons(0)

  override def getIcon(
      stack: ItemStack,
      renderPass: Int,
      player: EntityPlayer,
      usingItem: ItemStack,
      useRemaining: Int
  ) =
    if (hasData(stack)) icons(1) else icons(0)

  override def getIconIndex(stack: ItemStack) =
    if (hasData(stack)) icons(1) else icons(0)

  override def addInformation(
      stack: ItemStack,
      player: EntityPlayer,
      list: JList[String],
      par4: Boolean
  ) {
    val l2 = list
    if (
      (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(
        Keyboard.KEY_RSHIFT
      )) && stack.hasTagCompound
    ) {
      val tag = stack.getTagCompound
      import JavaConversions._
      var hasData = false
      for (key <- tag.func_150296_c().asInstanceOf[JSet[String]]) {
        l2.add(
          EnumChatFormatting.GRAY +
            key + "->" + tag
              .getString(key)
              .substring(0, 10) + (if (key.length > 16) "..." else "")
        )
        hasData = true
      }
      if (!hasData) l2.add(EnumChatFormatting.GRAY + "no data")
    }
  }
}

object ItemDataCard {
  private def assertStack(stack: ItemStack) = {
    if (stack != null) stack.getItem match {
      case c: ItemDataCard =>
        if (!stack.hasTagCompound) stack.setTagCompound(new NBTTagCompound)
        true
      case _ => false
    }
    else false
  }

  def setData(stack: ItemStack, dir: String, data: String) {
    if (assertStack(stack)) {
      stack.getTagCompound.setString(dir, data)
    }
  }

  def removeData(stack: ItemStack, dir: String) {
    if (assertStack(stack)) {
      stack.getTagCompound.removeTag(dir)
    }
  }

  def hasData(stack: ItemStack) = {
    if (assertStack(stack)) {
      !stack.getTagCompound.hasNoTags
    } else false
  }

  def getData(stack: ItemStack, dir: String): String = {
    if (assertStack(stack)) {
      stack.getTagCompound.getString(dir)
    } else null
  }
}
