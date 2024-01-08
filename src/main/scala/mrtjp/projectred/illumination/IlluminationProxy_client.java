package mrtjp.projectred.illumination;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.projectred.ProjectRedIllumination;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.function.BiFunction;

import static mrtjp.projectred.ProjectRedIllumination.itemPartIllumarButton;
import static mrtjp.projectred.ProjectRedIllumination.itemPartIllumarFButton;

public class IlluminationProxy_client extends IlluminationProxy_server {

    @SideOnly(Side.CLIENT)
    public void init() {
        super.init();

        for (LightObject l : lights) l.initClient();

        MinecraftForgeClient.registerItemRenderer(
                itemPartIllumarButton,
                RenderButton
        );
        MinecraftForgeClient.registerItemRenderer(
                itemPartIllumarFButton,
                RenderFButton
        );

        MinecraftForgeClient.registerItemRenderer(
                Item.getItemFromBlock(ProjectRedIllumination.blockLamp),
                LampTESR
        );
        ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, LampTESR);
    }

    public BiFunction<Integer, Integer, Integer> getLightValue = (meta, brightness) -> brightness;
}
