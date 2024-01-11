package mrtjp.projectred.core

import codechicken.lib.packet.PacketCustom
import codechicken.lib.packet.PacketCustom.IServerPacketHandler
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.play.INetHandlerPlayServer

object CoreSPH extends CorePH with IServerPacketHandler {
  override def handlePacket(
      packet: PacketCustom,
      sender: EntityPlayerMP,
      nethandler: INetHandlerPlayServer
  ) {
    packet.getType match {
      case _ =>
    }
  }
}
