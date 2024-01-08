package mrtjp.projectred.exploration;

import mrtjp.core.block.BlockDefinition;
import mrtjp.projectred.ProjectRedExploration;
import mrtjp.projectred.core.PartDefs;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * The members of this enum are in meta order - that is, the meta of OreDefs.values{}[1] is 1
 */
public enum OreDefs {

    ORERUBY("ruby_ore", 2, PartDefs.RUBY().makeStack(), 1, 4, 1, 8),
    ORESAPPHIRE("sapphire_ore", 2, PartDefs.SAPPHIRE().makeStack(), 1, 4, 1, 8),
    OREPERIDOT("peridot_ore", 2, PartDefs.PERIDOT().makeStack(), 1, 4, 1, 8),
    ORECOPPER("copper_ore", 1, null, 1, 1, 0, 0),
    ORETIN("tin_ore", 1, null, 1, 1, 0, 0),
    ORESILVER("silver_ore", 2, null, 1, 1, 0, 0),
    OREELECTROTINE("electrotine_ore", 2, PartDefs.ELECTROTINE().makeStack(), 1, 8, 1, 8);

    public String iconName;
    public final int harvest;
    public final ItemStack drop;
    public final int min;
    public final int max;
    public final int minXP;
    public final int maxXP;

    public static Block getBlock() {
        return ProjectRedExploration.blockOres;
    }

    OreDefs(String iconName, int harvest, ItemStack drop, int min, int max, int minXP, int maxXP) {
        this.iconName = iconName;
        this.harvest = harvest;
        this.drop = drop;
        this.min = min;
        this.max = max;
        this.minXP = minXP;
        this.maxXP = maxXP;
    }

    public IIcon icon = null;
    public void registerIcon(IIconRegister reg) {
        icon = reg.registerIcon("projectred:world/" + iconName);
    }

    public boolean hasDrop() {
        return drop != null;
    }

    public ItemStack makeDropStack() {
        return makeDropStack(1);
    }
    public ItemStack makeDropStack(int i) {
        return new ItemStack(drop.getItem(), i, drop.getItemDamage());
    }

}
