package mrtjp.projectred;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mrtjp.core.color.Colors_old;
import mrtjp.projectred.illumination.BlockAirousLight;
import mrtjp.projectred.illumination.BlockLamp;
import mrtjp.projectred.illumination.IlluminationProxy;
import mrtjp.projectred.illumination.ItemPartButton;
import mrtjp.projectred.illumination.ItemPartFButton;
import mrtjp.projectred.illumination.LightObjCage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(
        modid = "ProjRed|Illumination",
        dependencies = "required-after:ProjRed|Core",
        acceptedMinecraftVersions = "[1.7.10]",
        name = "ProjectRed Illumination",
        version = ProjectRedCore.VERSION
)
public class ProjectRedIllumination {

    /** Blocks * */
    public static BlockLamp blockLamp = null;
    public static BlockAirousLight blockAirousLight = null;

    /** Multipart items * */
    public static ItemPartButton itemPartIllumarButton = null;
    public static ItemPartFButton itemPartIllumarFButton = null;

    public CreativeTabs tabLighting = new CreativeTabs("ill") {

        @Override
        public ItemStack getIconItemStack() {
            return LightObjCage.makeInvStack(Colors_old.RED.ordinal());
        }

        @Override
        public Item getTabIconItem() {
            return getIconItemStack().getItem();
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        IlluminationProxy.versionCheck();
        IlluminationProxy.preinit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        IlluminationProxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        IlluminationProxy.postinit();
    }
}
