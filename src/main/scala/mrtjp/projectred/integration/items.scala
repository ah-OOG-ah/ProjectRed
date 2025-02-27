/*
 * Copyright (c) 2014.
 * Created by MrTJP.
 * All rights reserved.
 */
package mrtjp.projectred.integration

import java.util.{List => JList}

import codechicken.lib.render.{CCRenderState, TextureUtils}
import codechicken.lib.vec.{BlockCoord, Scale, Translation, Vector3}
import codechicken.multipart.{MultiPartRegistry, TItemMultiPart, TMultiPart}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mrtjp.core.item.{ItemCore, ItemDefinition, TItemGlassSound}
import mrtjp.core.world.PlacementLib
import mrtjp.projectred.ProjectRedIntegration
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.{
  ItemRenderType,
  ItemRendererHelper
}
import org.lwjgl.opengl.GL11

class ItemPartGate
    extends ItemCore("projectred.integration.gate")
    with TItemMultiPart
    with TItemGlassSound {
  setHasSubtypes(true)
  setCreativeTab(ProjectRedIntegration.tabIntegration2)
  var infoBuilderFunc = { (stack: ItemStack, l: JList[String]) => }

  def newPart(
      item: ItemStack,
      player: EntityPlayer,
      world: World,
      pos: BlockCoord,
      side: Int,
      vhit: Vector3
  ): TMultiPart = {
    val onPos = pos.copy.offset(side ^ 1)
    if (
      !PlacementLib.canPlaceGateOnSide(world, onPos.x, onPos.y, onPos.z, side)
    ) return null

    val gtype = GateDefinition(item.getItemDamage)
    if (!gtype.implemented) return null

    val gate =
      MultiPartRegistry.createPart(gtype.partname, false).asInstanceOf[GatePart]
    if (gate != null)
      gate.preparePlacement(player, pos, side, item.getItemDamage)
    gate
  }

  @SideOnly(Side.CLIENT)
  override def getSubItems(id: Item, tab: CreativeTabs, list: JList[ItemStack]) {
    val l2 = list
    for (g <- GateDefinition.values) if (g.implemented) l2.add(g.makeStack)
  }

  override def registerIcons(reg: IIconRegister) {
    ComponentStore.registerIcons(reg)
  }

  @SideOnly(Side.CLIENT)
  override def getSpriteNumber = 0

  override def addInformation(
      stack: ItemStack,
      player: EntityPlayer,
      list: JList[String],
      flag: Boolean
  ) {
    infoBuilderFunc(stack, list)
  }
}

object GateDefinition extends ItemDefinition {
  override type EnumVal = GateDef
  override def getItem = ProjectRedIntegration.itemPartGate2

  val OR = new GateDef("pr_sgate")
  val NOR = new GateDef("pr_sgate")
  val NOT = new GateDef("pr_sgate")
  val AND = new GateDef("pr_sgate")
  val NAND = new GateDef("pr_sgate")
  val XOR = new GateDef("pr_sgate")
  val XNOR = new GateDef("pr_sgate")
  val Buffer = new GateDef("pr_sgate")
  val Multiplexer = new GateDef("pr_sgate")
  val Pulse = new GateDef("pr_sgate")
  val Repeater = new GateDef("pr_sgate")
  val Randomizer = new GateDef("pr_sgate")
  val SRLatch = new GateDef("pr_igate")
  val ToggleLatch = new GateDef("pr_igate")
  val TransparentLatch = new GateDef("pr_sgate")
  val LightSensor = new GateDef("pr_sgate")
  val RainSensor = new GateDef("pr_sgate")
  val Timer = new GateDef("pr_igate")
  val Sequencer = new GateDef("pr_igate")
  val Counter = new GateDef("pr_igate")
  val StateCell = new GateDef("pr_igate")
  val Synchronizer = new GateDef("pr_igate")
  val BusTransceiver = new GateDef("pr_bgate")
  val NullCell = new GateDef("pr_agate")
  val InvertCell = new GateDef("pr_agate")
  val BufferCell = new GateDef("pr_agate")
  val Comparator = new GateDef("pr_tgate")
  val ANDCell = new GateDef("pr_agate")
  val BusRandomizer = new GateDef("pr_bgate")
  val BusConverter = new GateDef("pr_bgate")
  val BusInputPanel = new GateDef("pr_bgate")
  val StackingLatch = new GateDef("pr_agate")
  val SegmentDisplay = new GateDef("pr_bgate")
  val DecRandomizer = new GateDef("pr_sgate")
  val ICGate = new GateDef("pr_icgate", true)

  class GateDef(val partname: String, val hidden: Boolean = false)
      extends ItemDef {
    def implemented = partname != null
  }
}

object GateItemRenderer extends IItemRenderer {
  override def shouldUseRenderHelper(
      t: ItemRenderType,
      item: ItemStack,
      helper: ItemRendererHelper
  ) = true

  override def handleRenderType(item: ItemStack, t: ItemRenderType) = true

  override def renderItem(t: ItemRenderType, item: ItemStack, data: AnyRef*) = {
    val damage = item.getItemDamage
    import net.minecraftforge.client.IItemRenderer.ItemRenderType._
    t match {
      case ENTITY   => renderGateInv(damage, -0.3f, 0f, -0.3f, 0.6f)
      case EQUIPPED => renderGateInv(damage, 0.0f, 0.15f, 0.0f, 1.0f)
      case EQUIPPED_FIRST_PERSON =>
        renderGateInv(damage, 1.0f, -0.2f, -0.4f, 2.0f)
      case INVENTORY => renderGateInv(damage, 0.0f, 0.2f, 0.0f, 1.0f)
      case _         =>
    }

    def renderGateInv(meta: Int, x: Float, y: Float, z: Float, scale: Float) {
      if (!GateDefinition(meta).implemented) return
      GL11.glEnable(GL11.GL_BLEND)
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
      TextureUtils.bindAtlas(0)
      val ccrsi = CCRenderState.instance
      ccrsi.reset()
      ccrsi.setDynamic()
      ccrsi.pullLightmap()

      RenderGate.renderInv(
        item,
        new Scale(scale).`with`(new Translation(x, y, z)),
        meta
      )
    }
  }
}
