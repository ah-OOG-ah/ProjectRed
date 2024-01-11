package mrtjp.projectred.core;

import codechicken.lib.packet.PacketCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class CoreCPH extends CorePH implements PacketCustom.IClientPacketHandler {

    public static final CoreCPH instance = new CoreCPH();

    // I honestly have no idea what this does
    public void handlePacket(
        PacketCustom packet,
        Minecraft mc,
        INetHandlerPlayClient nethandler
    ) {
        final World world = mc.theWorld;
        packet.getType();
    }
}
