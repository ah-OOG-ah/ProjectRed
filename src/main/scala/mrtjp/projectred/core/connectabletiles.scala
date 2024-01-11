package mrtjp.projectred.core

import codechicken.lib.vec.{BlockCoord, Rotation}
import mrtjp.core.block.InstancedBlockTile
import mrtjp.core.world.WorldLib
import mrtjp.projectred.core.libmc.PRLib

trait TTileAcquisitions extends InstancedBlockTile {
  def getStraightCenter(s: Int) = {
    val pos = posOfInternal.offset(s)
    val t = PRLib.getMultipartTile(world, pos)
    if (t != null) t.partMap(6)
    else null
  }

  def getStraight(s: Int, edgeRot: Int) = {
    val pos = posOfStraight(s)
    val t = PRLib.getMultipartTile(world, pos)
    if (t != null) t.partMap(Rotation.rotateSide(s ^ 1, edgeRot))
    else null
  }

  def getCorner(s: Int, edgeRot: Int) = {
    val pos = posOfCorner(s, edgeRot)
    val t = PRLib.getMultipartTile(world, pos)
    if (t != null) t.partMap(s ^ 1)
    else null
  }

  def posOfStraight(s: Int) = new BlockCoord(this).offset(s)
  def posOfCorner(s: Int, edgeRot: Int) =
    new BlockCoord(this).offset(s).offset(Rotation.rotateSide(s ^ 1, edgeRot))
  def posOfInternal = new BlockCoord(this)

  def rotFromStraight(s: Int, edgeRot: Int) =
    Rotation.rotationTo(Rotation.rotateSide(s ^ 1, edgeRot), s ^ 1)
  def rotFromCorner(s: Int, edgeRot: Int) =
    Rotation.rotationTo(s ^ 1, Rotation.rotateSide(s ^ 1, edgeRot) ^ 1)

  def notifyStraight(s: Int) {
    val pos = posOfStraight(s)
    world.notifyBlockOfNeighborChange(pos.x, pos.y, pos.z, getBlockType)
  }

  def notifyCorner(s: Int, edgeRot: Int) {
    val pos = posOfCorner(s, edgeRot)
    world.notifyBlockOfNeighborChange(pos.x, pos.y, pos.z, getBlockType)
  }
}

trait TPowerTile
    extends TConnectableInstTile
    with TCachedPowerConductor {
  override def idRange = 0 until 30

  def getExternalCond(id: Int): PowerConductor = {
    if (0 until 24 contains id) // side edge conns
      {
        val s = id / 4
        val edgeRot = id % 4
        if (maskConnectsStraight(s, edgeRot)) getStraight(s, edgeRot) match {
          case tp: IPowerConnectable =>
            return tp.conductor(rotFromStraight(s, edgeRot))
          case _ =>
        }
        else if (maskConnectsCorner(s, edgeRot)) getCorner(s, edgeRot) match {
          case tp: IPowerConnectable =>
            return tp.conductor(rotFromCorner(s, edgeRot))
          case _ =>
        }
      } else if (24 until 30 contains id) // straight face conns
      {
        val s = id - 24
        if (maskConnectsStraightCenter(s)) getStraightCenter(s) match {
          case tp: IPowerConnectable => return tp.conductor(s ^ 1)
          case _ =>
            WorldLib.getTileEntity(
              world,
              posOfInternal.offset(s),
              classOf[IPowerConnectable]
            ) match {
              case tp: IPowerConnectable => return tp.conductor(s ^ 1)
              case _                     =>
            }
        }
      }
    null
  }

  abstract override def onMaskChanged() {
    super.onMaskChanged()
    needsCache = true
  }
}
