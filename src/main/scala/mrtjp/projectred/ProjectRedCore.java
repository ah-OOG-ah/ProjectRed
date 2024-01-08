package mrtjp.projectred;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import mrtjp.projectred.core.Configurator;
import mrtjp.projectred.core.CoreProxy;
import mrtjp.projectred.core.ItemDataCard;
import mrtjp.projectred.core.ItemDrawPlate;
import mrtjp.projectred.core.ItemPart;
import mrtjp.projectred.core.ItemScrewdriver;
import mrtjp.projectred.core.ItemWireDebugger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = "ProjRed|Core",
    version = ProjectRedCore.VERSION,
    dependencies = "required-after:Forge;" +
        "required-after:ForgeMultipart;" +
        "required-after:MrTJPCoreMod",
    guiFactory = "mrtjp.projectred.core.GuiConfigFactory",
    acceptedMinecraftVersions = "[1.7.10]",
    name = "ProjectRed Core"
)
public class ProjectRedCore {

    public static final Logger log = LogManager.getFormatterLogger("ProjRed|Core");
    public static final String VERSION = "GRADLETOKEN_VERSION";

    /** Items * */
    public static ItemPart itemPart = null;
    public static ItemDrawPlate itemDrawPlate = null;
    public static ItemScrewdriver itemScrewdriver = null;
    public static ItemWireDebugger itemWireDebugger = null;
    public static ItemDataCard itemDataCard = null;

    public CreativeTabs tabCore = new CreativeTabs("core") {

        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(
                    ProjectRedCore.itemScrewdriver
            );
        }

        @Override
        public Item getTabIconItem() {
            return getIconItemStack().getItem();
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configurator.loadConfig();
        CoreProxy.versionCheck();
        CoreProxy.preinit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        CoreProxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CoreProxy.postinit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {}
}
