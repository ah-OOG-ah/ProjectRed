/*
 * Copyright (c) 2014.
 * Created by MrTJP.
 * All rights reserved.
 */
package mrtjp.projectred.integration

import codechicken.lib.data.{MCDataInput, MCDataOutput}
import codechicken.lib.packet.PacketCustom
import codechicken.lib.packet.PacketCustom.{
  IClientPacketHandler,
  IServerPacketHandler
}
import codechicken.lib.vec.BlockCoord
import codechicken.multipart.TMultiPart
import mrtjp.projectred.ProjectRedIntegration
import mrtjp.projectred.core.libmc.PRLib
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.play.{INetHandlerPlayClient, INetHandlerPlayServer}
import net.minecraft.world.World

class IntegrationPH {
  val channel = ProjectRedIntegration

  def writePartIndex(out: MCDataOutput, part: TMultiPart) = {
    out
      .writeCoord(new BlockCoord(part.tile))
      .writeByte(part.tile.partList.indexOf(part))
  }

  def readPartIndex(world: World, in: MCDataInput) = {
    val tile = PRLib.getMultipartTile(world, in.readCoord)
    try {
      tile.partList(in.readUByte)
    } catch {
      case e: NullPointerException      => null
      case e: IndexOutOfBoundsException => null
    }
  }
}

object IntegrationSPH extends IntegrationPH with IServerPacketHandler {
  override def handlePacket(
      packet: PacketCustom,
      sender: EntityPlayerMP,
      handler: INetHandlerPlayServer
  ) = packet.getType match {
    case 1 => incrTimer(sender.worldObj, packet)
    case 2 => incCounter(sender.worldObj, packet)
  }

  private def incCounter(world: World, packet: PacketCustom) {
    readPartIndex(world, packet) match {
      case gate: GatePart if gate.getLogic.isInstanceOf[ICounterGuiLogic] =>
        val t = gate.getLogic[ICounterGuiLogic]
        val actionID = packet.readByte()
        if (actionID == 0)
          t.setCounterMax(gate, t.getCounterMax + packet.readShort())
        else if (actionID == 1)
          t.setCounterIncr(gate, t.getCounterIncr + packet.readShort())
        else if (actionID == 2)
          t.setCounterDecr(gate, t.getCounterDecr + packet.readShort())
      case _ =>
    }
  }

  private def incrTimer(world: World, packet: PacketCustom) {
    readPartIndex(world, packet) match {
      case gate: GatePart if gate.getLogic.isInstanceOf[ITimerGuiLogic] =>
        val t = gate.getLogic[ITimerGuiLogic]
        t.setTimerMax(gate, t.getTimerMax + packet.readShort())
      case _ =>
    }
  }

}

object IntegrationCPH extends IntegrationPH with IClientPacketHandler {
  override def handlePacket(
      packet: PacketCustom,
      mc: Minecraft,
      handler: INetHandlerPlayClient
  ) = packet.getType match {
    case _ =>
  }
}
