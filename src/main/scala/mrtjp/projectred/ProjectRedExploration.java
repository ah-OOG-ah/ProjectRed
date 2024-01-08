package mrtjp.projectred;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mrtjp.projectred.exploration.BlockBarrel;
import mrtjp.projectred.exploration.BlockDecorativeWalls;
import mrtjp.projectred.exploration.BlockDecoratives;
import mrtjp.projectred.exploration.BlockLily;
import mrtjp.projectred.exploration.BlockOre;
import mrtjp.projectred.exploration.ExplorationProxy;
import mrtjp.projectred.exploration.ItemAthame;
import mrtjp.projectred.exploration.ItemBackpack;
import mrtjp.projectred.exploration.ItemGemArmor;
import mrtjp.projectred.exploration.ItemGemAxe;
import mrtjp.projectred.exploration.ItemGemHoe;
import mrtjp.projectred.exploration.ItemGemPickaxe;
import mrtjp.projectred.exploration.ItemGemSaw;
import mrtjp.projectred.exploration.ItemGemShovel;
import mrtjp.projectred.exploration.ItemGemSickle;
import mrtjp.projectred.exploration.ItemGemSword;
import mrtjp.projectred.exploration.ItemLilySeeds;
import mrtjp.projectred.exploration.ItemWoolGin;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import tconstruct.library.tools.ToolMaterial;

@Mod(
        modid = "ProjRed|Exploration",
        dependencies = "required-after:ProjRed|Core",
        acceptedMinecraftVersions = "[1.7.10]",
        name = "ProjectRed Exploration",
        version = ProjectRedCore.VERSION
)
public class ProjectRedExploration {

    /** Blocks * */
    public static BlockOre blockOres = null;
    public static BlockDecoratives blockDecoratives = null;
    public static BlockDecorativeWalls blockDecorativeWalls = null;
    public static BlockLily blockLily = null;
    public static BlockBarrel blockBarrel = null;

    /** Materials * */
    public static Item.ToolMaterial toolMaterialRuby = null;
    public static Item.ToolMaterial toolMaterialSapphire = null;
    public static Item.ToolMaterial toolMaterialPeridot = null;
    public static ArmorMaterial armorMatrialRuby = null;
    public static ArmorMaterial armorMatrialSapphire = null;
    public static ArmorMaterial armorMatrialPeridot = null;

    /** Items * */
    public static ItemWoolGin itemWoolGin = null;
    public static ItemBackpack itemBackpack = null;
    public static ItemAthame itemAthame = null;
    public static ItemGemAxe itemRubyAxe = null;
    public static ItemGemAxe itemSapphireAxe = null;
    public static ItemGemAxe itemPeridotAxe = null;
    public static ItemGemHoe itemRubyHoe = null;
    public static ItemGemHoe itemSapphireHoe = null;
    public static ItemGemHoe itemPeridotHoe = null;
    public static ItemGemPickaxe itemRubyPickaxe = null;
    public static ItemGemPickaxe itemSapphirePickaxe = null;
    public static ItemGemPickaxe itemPeridotPickaxe = null;
    public static ItemGemShovel itemRubyShovel = null;
    public static ItemGemShovel itemSapphireShovel = null;
    public static ItemGemShovel itemPeridotShovel = null;
    public static ItemGemSword itemRubySword = null;
    public static ItemGemSword itemSapphireSword = null;
    public static ItemGemSword itemPeridotSword = null;
    public static ItemGemSaw itemGoldSaw = null;
    public static ItemGemSaw itemRubySaw = null;
    public static ItemGemSaw itemSapphireSaw = null;
    public static ItemGemSaw itemPeridotSaw = null;
    public static ItemGemSickle itemWoodSickle = null;
    public static ItemGemSickle itemStoneSickle = null;
    public static ItemGemSickle itemIronSickle = null;
    public static ItemGemSickle itemGoldSickle = null;
    public static ItemGemSickle itemRubySickle = null;
    public static ItemGemSickle itemSapphireSickle = null;
    public static ItemGemSickle itemPeridotSickle = null;
    public static ItemGemSickle itemDiamondSickle = null;
    public static ItemLilySeeds itemLilySeed = null;
    public static ItemGemArmor itemRubyHelmet = null;
    public static ItemGemArmor itemRubyChestplate = null;
    public static ItemGemArmor itemRubyLeggings = null;
    public static ItemGemArmor itemRubyBoots = null;
    public static ItemGemArmor itemSapphireHelmet = null;
    public static ItemGemArmor itemSapphireChestplate = null;
    public static ItemGemArmor itemSapphireLeggings = null;
    public static ItemGemArmor itemSapphireBoots = null;
    public static ItemGemArmor itemPeridotHelmet = null;
    public static ItemGemArmor itemPeridotChestplate = null;
    public static ItemGemArmor itemPeridotLeggings = null;
    public static ItemGemArmor itemPeridotBoots = null;

    public static CreativeTabs tabExploration = new CreativeTabs("exploration") {
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(Blocks.grass);
        }
        @Override
        public Item getTabIconItem() {
            return getIconItemStack().getItem();
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ExplorationProxy.versionCheck();
        ExplorationProxy.preinit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ExplorationProxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ExplorationProxy.postinit();
    }
}
