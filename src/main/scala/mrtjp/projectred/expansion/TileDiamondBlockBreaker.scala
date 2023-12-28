package mrtjp.projectred.expansion

import codechicken.lib.render.uv.{MultiIconTransformation, UVTransformation}
import mrtjp.core.block.TInstancedBlockRender
import mrtjp.core.render.TCubeMapRender
import mrtjp.core.world.WorldLib
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import org.apache.commons.lang3.tuple.ImmutableTriple

import java.util.{List => JList}
import scala.collection.JavaConversions._

class TileDiamondBlockBreaker extends TileBlockBreaker {
  override def onActivate() {
    val bc = position.offset(side ^ 1)
    val (b, meta, _) = WorldLib.getBlockInfo(world, bc.x, bc.y, bc.z)

    if (b == null || b == Blocks.bedrock) return
    if (b.isAir(world, bc.x, bc.y, bc.z)) return
    if (b.getBlockHardness(world, bc.x, bc.y, bc.z) < 0) return
    // if (b.getHarvestLevel(meta) > 2) return

    b.getDrops(world, bc.x, bc.y, bc.z, meta, 0).foreach(storage.add)
    world.playAuxSFXAtEntity(
      null,
      2001,
      xCoord,
      yCoord,
      zCoord,
      Block.getIdFromBlock(b) + (meta << 12)
    )
    world.setBlockToAir(bc.x, bc.y, bc.z)
    exportBuffer()
  }

}

object RenderDiamondBlockBreaker
    extends TInstancedBlockRender
    with TCubeMapRender {
  var bottom: IIcon = _
  var side1: IIcon = _
  var top1: IIcon = _
  var side2: IIcon = _
  var top2: IIcon = _

  var iconT1: UVTransformation = _
  var iconT2: UVTransformation = _

  override def getData(w: IBlockAccess, x: Int, y: Int, z: Int) = {
    val te = WorldLib.getTileEntity(w, x, y, z, classOf[TActiveDevice])
    if (te != null)
      new ImmutableTriple(te.side, te.rotation, if (te.active || te.powered) iconT2 else iconT1)
    else new ImmutableTriple(0, 0, iconT1)
  }

  override def getInvData = new ImmutableTriple(0, 0, iconT1)

  override def getIcon(s: Int, meta: Int) = s match {
    case 0 => bottom
    case 1 => top1
    case _ => side1
  }

  override def registerIcons(reg: IIconRegister) {
    bottom = reg.registerIcon("projectred:mechanical/dbreaker/bottom")
    top1 = reg.registerIcon("projectred:mechanical/dbreaker/top1")
    side1 = reg.registerIcon("projectred:mechanical/dbreaker/side1")
    top2 = reg.registerIcon("projectred:mechanical/dbreaker/top2")
    side2 = reg.registerIcon("projectred:mechanical/dbreaker/side2")

    iconT1 =
      new MultiIconTransformation(bottom, top1, side1, side1, side1, side1)
    iconT2 =
      new MultiIconTransformation(bottom, top2, side2, side2, side2, side2)
  }
}
