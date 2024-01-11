package mrtjp.projectred.core;

import codechicken.lib.packet.PacketCustom;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.core.fx.FXEngine;
import mrtjp.projectred.core.libmc.fx.ParticleIconRegistry;
import mrtjp.projectred.core.libmc.fx.ParticleManagement;
import net.minecraftforge.common.MinecraftForge;

public class CoreProxy_client extends CoreProxy_server {

    @SideOnly(Side.CLIENT)
    @Override
    public void postinit() {
        super.postinit();
        MinecraftForge.EVENT_BUS.register(ParticleManagement.instance);
        FMLCommonHandler.instance().bus().register(ParticleManagement.instance);
        MinecraftForge.EVENT_BUS.register(ParticleIconRegistry.instance);
        MinecraftForge.EVENT_BUS.register(RenderHalo.instance);

        FXEngine.register();

        new PRUpdateChecker();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void init() {
        super.init();
        PacketCustom.assignHandler(CoreCPH.instance.channel, CoreCPH.instance);
    }
}
