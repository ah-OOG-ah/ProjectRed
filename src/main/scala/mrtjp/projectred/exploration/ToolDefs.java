package mrtjp.projectred.exploration;

import mrtjp.projectred.core.PartDefs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static mrtjp.projectred.ProjectRedExploration.toolMaterialPeridot;
import static mrtjp.projectred.ProjectRedExploration.toolMaterialRuby;
import static mrtjp.projectred.ProjectRedExploration.toolMaterialSapphire;

import static net.minecraft.item.Item.ToolMaterial.EMERALD;
import static net.minecraft.item.Item.ToolMaterial.GOLD;
import static net.minecraft.item.Item.ToolMaterial.IRON;
import static net.minecraft.item.Item.ToolMaterial.STONE;
import static net.minecraft.item.Item.ToolMaterial.WOOD;

public class ToolDefs {
    private static final ItemStack wood = new ItemStack(Blocks.planks);
    private static final ItemStack flint = new ItemStack(Items.flint);
    private static final ItemStack iron = new ItemStack(Items.iron_ingot);
    private static final ItemStack gold = new ItemStack(Items.gold_ingot);
    private static final ItemStack ruby = PartDefs.RUBY.makeStack();
    private static final ItemStack sapphire = PartDefs.SAPPHIRE.makeStack();
    private static final ItemStack peridot = PartDefs.PERIDOT.makeStack();
    private static final ItemStack diamond = new ItemStack(Items.diamond);

    public static final ToolDef RUBYAXE = new ToolDef("axeruby", toolMaterialRuby, ruby);
    public static final ToolDef SAPPHIREAXE = new ToolDef("axesapphire", toolMaterialSapphire, sapphire);
    public static final ToolDef PERIDOTAXE = new ToolDef("axeperidot", toolMaterialPeridot, peridot);

    public static final ToolDef RUBYPICKAXE = new ToolDef("pickaxeruby", toolMaterialRuby, ruby);
    public static final ToolDef SAPPHIREPICKAXE = new ToolDef("pickaxesapphire", toolMaterialSapphire, sapphire);
    public static final ToolDef PERIDOTPICKAXE = new ToolDef("pickaxeperidot", toolMaterialPeridot, peridot);

    public static final ToolDef RUBYSHOVEL = new ToolDef("shovelruby", toolMaterialRuby, ruby);
    public static final ToolDef SAPPHIRESHOVEL = new ToolDef("shovelsapphire", toolMaterialSapphire, sapphire);
    public static final ToolDef PERIDOTSHOVEL = new ToolDef("shovelperidot", toolMaterialPeridot, peridot);

    public static final ToolDef RUBYSWORD = new ToolDef("swordruby", toolMaterialRuby, ruby);
    public static final ToolDef SAPPHIRESWORD = new ToolDef("swordsapphire", toolMaterialSapphire, sapphire);
    public static final ToolDef PERIDOTSWORD = new ToolDef("swordperidot", toolMaterialPeridot, peridot);

    public static final ToolDef RUBYHOE = new ToolDef("hoeruby", toolMaterialRuby, ruby);
    public static final ToolDef SAPPHIREHOE = new ToolDef("hoesapphire", toolMaterialSapphire, sapphire);
    public static final ToolDef PERIDOTHOE = new ToolDef("hoeperidot", toolMaterialPeridot, peridot);

    public static final ToolDef WOODSAW = new ToolDef("sawwood", WOOD, wood);
    public static final ToolDef STONESAW = new ToolDef("sawstone", STONE, flint);
    public static final ToolDef IRONSAW = new ToolDef("sawiron", IRON, iron);
    public static final ToolDef GOLDSAW = new ToolDef("sawgold", GOLD, gold);
    public static final ToolDef RUBYSAW = new ToolDef("sawruby", toolMaterialRuby, ruby);
    public static final ToolDef SAPPHIRESAW = new ToolDef("sawsapphire", toolMaterialSapphire, sapphire);
    public static final ToolDef PERIDOTSAW = new ToolDef("sawperidot", toolMaterialPeridot, peridot);
    public static final ToolDef DIAMONDSAW = new ToolDef("sawdiamond", EMERALD, diamond);

    public static final ToolDef WOODSICKLE = new ToolDef("sicklewood", WOOD, wood);
    public static final ToolDef STONESICKLE = new ToolDef("sicklestone", STONE, flint);
    public static final ToolDef IRONSICKLE = new ToolDef("sickleiron", IRON, iron);
    public static final ToolDef GOLDSICKLE = new ToolDef("sicklegold", GOLD, gold);
    public static final ToolDef RUBYSICKLE = new ToolDef("sickleruby", toolMaterialRuby, ruby);
    public static final ToolDef SAPPHIRESICKLE = new ToolDef("sicklesapphire", toolMaterialSapphire, sapphire);
    public static final ToolDef PERIDOTSICKLE = new ToolDef("sickleperidot", toolMaterialPeridot, peridot);
    public static final ToolDef DIAMONDSICKLE = new ToolDef("sicklediamond", EMERALD, diamond);

    public static class ToolDef {
        String unlocal;
        Item.ToolMaterial mat;
        ItemStack repair;
        public ToolDef(String unlocal, Item.ToolMaterial mat, ItemStack repair) {
            this.unlocal = unlocal;
            this.mat = mat;
            this. repair = repair;
        }
    }
}
