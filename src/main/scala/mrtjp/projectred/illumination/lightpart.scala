package mrtjp.projectred.illumination

import codechicken.lib.data.{MCDataInput, MCDataOutput}
import codechicken.lib.render.CCRenderState
import codechicken.lib.vec._
import codechicken.microblock.HollowMicroblock
import codechicken.multipart._
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mrtjp.core.world.{PlacementLib, WorldLib}
import mrtjp.projectred.ProjectRedIllumination
import mrtjp.projectred.core.RenderHalo
import mrtjp.projectred.core.libmc.PRLib
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.{EnumSkyBlock, World}

import scala.collection.JavaConversions._

class BaseLightPart(obj: LightObject)
    extends TMultiPart
    with TCuboidPart
    with TSlottedPart
    with TNormalOcclusion
    with IRedstonePart
    with ILight {
  protected var inverted = false
  protected var powered = false
  protected var meta: Byte = 0
  var side: Byte = 0

  def preparePlacement(side: Int, meta: Int, inv: Boolean) {
    this.inverted = inv
    this.side = side.asInstanceOf[Byte]
    this.meta = meta.asInstanceOf[Byte]
  }

  override def writeDesc(packet: MCDataOutput) {
    packet
      .writeBoolean(inverted)
      .writeBoolean(powered)
      .writeByte(meta)
      .writeByte(side)
  }

  override def readDesc(packet: MCDataInput) {
    inverted = packet.readBoolean()
    powered = packet.readBoolean()
    meta = packet.readByte()
    side = packet.readByte()
  }

  override def load(tag: NBTTagCompound) {
    inverted = tag.getBoolean("inv")
    powered = tag.getBoolean("pow")
    meta = tag.getByte("meta")
    side = tag.getByte("side")
  }

  override def save(tag: NBTTagCompound) {
    tag.setBoolean("inv", inverted)
    tag.setBoolean("pow", powered)
    tag.setByte("meta", meta)
    tag.setByte("side", side)
  }

  override def onNeighborChanged() {
    if (checkSupport) return
    updateState(false)
  }

  def checkSupport: Boolean = {
    if (world.isRemote) return false
    if (obj.canFloat) return false
    val bc = new BlockCoord(tile).offset(side)

    if (
      !obj.canFloat && !BaseLightPart.canPlaceLight(
        world,
        bc.x,
        bc.y,
        bc.z,
        side ^ 1
      )
    ) {
      WorldLib.dropItem(world, x, y, z, getItem)
      tile.remPart(this)
      return true
    }
    false
  }

  override def onPartChanged(part: TMultiPart) {
    if (checkSupport) return
    updateState(false)
  }

  override def onAdded() {
    if (checkSupport) return
    updateState(true)
  }

  private def checkPower: Boolean = {
    for (s <- 0 until 6)
      if (s != (side ^ 1))
        if (RedstoneInteractions.getPowerTo(this, s) > 0)
          return true
    false
  }

  private def updateState(forceRender: Boolean) {
    var updated = false
    if (!world.isRemote) {
      val old = powered
      powered = checkPower
      if (old != powered) {
        updated = true
        updateRender()
      }
    }
    if (forceRender && !updated) updateRender()
  }

  def updateRender() {
    world.markBlockForUpdate(x, y, z)
    world.updateLightByType(EnumSkyBlock.Block, x, y, z)
    if (!world.isRemote) sendDescUpdate()
    tile.markRender()
  }

  override def getLightValue = if (inverted != powered)
    IlluminationProxy.getLightValue(getColor, 15)
  else 0

  @SideOnly(Side.CLIENT)
  override def renderDynamic(pos: Vector3, frame: Float, pass: Int) {
    if (pass == 0 && isOn) RenderHalo.addLight(x, y, z, meta, getLightBounds)
  }

  @SideOnly(Side.CLIENT)
  override def renderStatic(pos: Vector3, pass: Int) = {
    if (pass == 0) {
      CCRenderState.instance.setBrightness(world, x, y, z)
      obj.render(this, meta, isOn, pos)
      true
    } else false
  }

  override def doesTick = false

  def getItem = new ItemStack(obj.getItem(inverted), 1, meta)
  def getLightBounds = obj.getLBounds(side)
  override def getBounds = obj.getBounds(side)
  override def getType = obj.getType

  override def getStrength(hit: MovingObjectPosition, player: EntityPlayer) = 2
  override def getSlotMask = 1 << 6
  override def getOcclusionBoxes = Seq(getBounds)

  override def getDrops = Seq(getItem)
  override def pickItem(hit: MovingObjectPosition) = getItem

  override def canConnectRedstone(side: Int) = true
  override def strongPowerLevel(side: Int) = 0
  override def weakPowerLevel(side: Int) = 0

  override def isOn = powered != inverted
  override def getColor = meta
}

object BaseLightPart {
  def canPlaceLight(w: World, x: Int, y: Int, z: Int, side: Int): Boolean = {
    if (PlacementLib.canPlaceLight(w, x, y, z, side)) return true

    val part = PRLib.getMultiPart(w, x, y, z, side)
    if (part.isInstanceOf[HollowMicroblock]) return true

    false
  }
}

class BaseLightFacePart(obj: LightObject)
    extends BaseLightPart(obj)
    with TFacePart
    with IMaskedRedstonePart {
  override def solid(side: Int) = false
  override def getSlotMask = (1 << side) & 0x40

  override def getConnectionMask(s: Int) = {
    if ((s ^ 1) == side) 0
    else if (s == side) 0x10
    else 1 << Rotation.rotationTo(s & 6, side)
  }
}

trait TAirousLight extends BaseLightPart {
  abstract override def doesTick = true

  abstract override def update() {
    super.update()
    if (!world.isRemote && isOn) {
      val rad = lightRadius
      val x1 = x + world.rand.nextInt(rad) - world.rand.nextInt(rad)
      val y1 = Math.max(
        Math.min(
          y + world.rand.nextInt(rad) - world.rand.nextInt(rad),
          world.getHeightValue(x, z) + 4
        ),
        7
      )
      val z1 = z + world.rand.nextInt(rad) - world.rand.nextInt(rad)

      if (
        world.isAirBlock(x1, y1, z1) && world.getBlockLightValue(x1, y1, z1) < 8
      ) {
        world.setBlock(
          x1,
          y1,
          z1,
          ProjectRedIllumination.blockAirousLight,
          getColor,
          3
        )
        val t =
          WorldLib.getTileEntity(world, x1, y1, z1, classOf[TileAirousLight])
        if (t != null) t.setSource(x, y, z, getColor, side)
      }
    }
  }

  def lightRadius = 16
}
