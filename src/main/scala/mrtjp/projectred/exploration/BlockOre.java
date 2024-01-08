package mrtjp.projectred.exploration;

import mrtjp.core.block.BlockCore;
import mrtjp.core.math.MathLib;
import mrtjp.projectred.ProjectRedExploration;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockOre extends BlockCore {

    public BlockOre() {
        super("projectred.exploration.ore", Material.rock);
        setHardness(3.0f);
        setResistance(5.0f);
        setCreativeTab(ProjectRedExploration.tabExploration);
    }

    @Override
    public ArrayList<ItemStack> getDrops(
            World world,
            int x,
            int y,
            int z,
            int meta,
            int fortune
    ) {
        if (OreDefs.values().length > meta) {
            final OreDefs odef = OreDefs.values()[meta];

            int count = world.rand.nextInt(fortune + odef.max);
            if (count > odef.max) count = odef.max;
            if (count < odef.min) count = odef.min;

            final ArrayList<ItemStack> array = new ArrayList<>();
            if (odef.hasDrop()) array.add(odef.makeDropStack(count));
            else array.add(odef.makeStack(count));
            return array;
        }
        return new ArrayList<>();
    }

    @Override
    public int getExpDrop(IBlockAccess world, int meta, int fortune) {
        if (OreDefs.values().length > meta) {
            final OreDefs odef = OreDefs.values()[meta];
            return MathLib.randomFromToIntRange(odef.minXP, odef.maxXP);
        }
        return 0;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (OreDefs.values().length > meta) return OreDefs.values()[meta].icon;
        return null;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        Arrays.stream(OreDefs.values()).forEach(o -> o.registerIcon(reg));
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (OreDefs o : OreDefs.values())
            list.add(o.makeStack());
    }
}
