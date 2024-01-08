package mrtjp.projectred.exploration;

import mrtjp.projectred.core.PartDefs;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import static mrtjp.projectred.ProjectRedExploration.armorMatrialPeridot;
import static mrtjp.projectred.ProjectRedExploration.armorMatrialRuby;
import static mrtjp.projectred.ProjectRedExploration.armorMatrialSapphire;

public class ArmorDefs {

    private static final ItemStack ruby = PartDefs.RUBY.makeStack();
    private static final ItemStack sapphire = PartDefs.SAPPHIRE.makeStack();
    private static final ItemStack peridot = PartDefs.PERIDOT.makeStack();

    public static final ArmorDef RUBYHELMET = new ArmorDef("rubyhelmet", "ruby", armorMatrialRuby, ruby);
    public static final ArmorDef RUBYCHESTPLATE = new ArmorDef("rubychestplate", "ruby", armorMatrialRuby, ruby);
    public static final ArmorDef RUBYLEGGINGS = new ArmorDef("rubyleggings", "ruby", armorMatrialRuby, ruby);
    public static final ArmorDef RUBYBOOTS = new ArmorDef("rubyboots", "ruby", armorMatrialRuby, ruby);

    public static final ArmorDef SAPPHIREHELMET = new ArmorDef("sapphirehelmet", "sapphire", armorMatrialSapphire, sapphire);
    public static final ArmorDef SAPPHIRECHESTPLATE = new ArmorDef("sapphirechestplate", "sapphire", armorMatrialSapphire, sapphire);
    public static final ArmorDef SAPPHIRELEGGINGS = new ArmorDef("sapphireleggings", "sapphire", armorMatrialSapphire, sapphire);
    public static final ArmorDef SAPPHIREBOOTS = new ArmorDef("sapphireboots", "sapphire", armorMatrialSapphire, sapphire);

    public static final ArmorDef PERIDOTHELMET = new ArmorDef("peridothelmet", "peridot", armorMatrialPeridot, peridot);
    public static final ArmorDef PERIDOTCHESTPLATE = new ArmorDef("peridotchestplate", "peridot", armorMatrialPeridot, peridot);
    public static final ArmorDef PERIDOTLEGGINGS = new ArmorDef("peridotleggings", "peridot", armorMatrialPeridot, peridot);
    public static final ArmorDef PERIDOTBOOTS = new ArmorDef("peridotboots", "peridot", armorMatrialPeridot, peridot);

    public static class ArmorDef {
        public final String unlocal;
        public final String tex;
        public final ItemArmor.ArmorMaterial mat;
        public final ItemStack repair;

        public ArmorDef(String unlocal, String tex, ItemArmor.ArmorMaterial mat, ItemStack repair) {
            this.unlocal = unlocal;
            this.tex = tex;
            this.mat = mat;
            this.repair = repair;
        }
    }
}
