package mrtjp.projectred.exploration;

import mrtjp.projectred.ProjectRedExploration;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * The members of this enum are in meta order - that is, the meta of DecorativeStoneDefs.values{}[1] is 1
 */
public enum DecorativeStoneDefs {

    MARBLE("marble", 2, 1.0f, 14.0f, null),
    MARBLEBRICK("marble_brick", 2, 1.0f, 14.0f, null),
    BASALTCOBBLE("basalt_cobble", 2, 2.5f, 14.0f, null),
    BASALT("basalt", 2, 2.5f, 16, BASALTCOBBLE.makeStack()),
    BASALTBRICK("basalt_brick", 2, 2.5f, 20, null),
    RUBYBLOCK("ruby_block", 2, 5.0f, 10.0f, null),
    SAPPHIREBLOCK("sapphire_block", 2, 5.0f, 10.0f, null),
    PERIDOTBLOCK("peridot_block", 2, 5.0f, 10.0f, null),
    COPPERBLOCK("copper_block", 2, 5.0f, 10.0f, null),
    TINBLOCK("tin_block", 2, 5.0f, 10.0f, null),
    SILVERBLOCK("silver_block", 2, 5.0f, 10.0f, null),
    ELECTROTINEBLOCK("electrotine_block", 2, 5.0f, 10.0f, null);

    public static Block getBlock() {
        return ProjectRedExploration.blockDecoratives;
    }

    public static Item getItem() {
        return Item.getItemFromBlock(getBlock());
    }

    public static DecorativeStoneDefs apply(int meta) {
        if (values().length > meta) {
            return values()[meta];
        }
        return null;
    }

    public String iconName;
    public final int harvest;
    public final float hardness;
    public final float explosion;
    public final ItemStack drop;
    public IIcon icon = null;

    DecorativeStoneDefs(String iconName, int harvest, float hardness, float explosion, ItemStack drop) {
        this.iconName = iconName;
        this.harvest = harvest;
        this.hardness = hardness;
        this.explosion = explosion;
        this.drop = drop;
    }

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

    @Override
    public String toString() {
        return getItem().getUnlocalizedName(makeStack());
    }

    public ItemStack makeStack() {
        return makeStack(1);
    }
    public ItemStack makeStack(int i) {
        return new ItemStack(getItem(), i, ordinal());
    }
}
