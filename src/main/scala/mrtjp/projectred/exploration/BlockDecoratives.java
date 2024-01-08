package mrtjp.projectred.exploration;

import mrtjp.core.block.BlockCore;
import mrtjp.projectred.ProjectRedExploration;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockDecoratives extends BlockCore {

    public BlockDecoratives() {
        super("projectred.exploration.stone", Material.rock);
        setHardness(3.0f);
        setResistance(10.0f);
        setCreativeTab(ProjectRedExploration.tabExploration);
    }

    @Override
    public float getBlockHardness(World w, int x, int y, int z) {
        final int meta = w.getBlockMetadata(x, y, z);
        if (DecorativeStoneDefs.values().length > meta)
            return DecorativeStoneDefs.values()[meta].hardness;
        return super.getBlockHardness(w, x, y, z);
    }

    @Override
    public float getExplosionResistance(
            Entity e,
            World w,
            int x,
            int y,
            int z,
            double ex,
            double ey,
            double ez
    ) {
        final int meta = w.getBlockMetadata(x, y, z);
        if (DecorativeStoneDefs.values().length > meta)
            return DecorativeStoneDefs.values()[meta].explosion;
        return super.getExplosionResistance(e, w, x, y, z, ex, ey, ez);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (DecorativeStoneDefs.values().length > (meta)) return DecorativeStoneDefs.values()[meta].icon;
        return super.getIcon(side, meta);
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
        if (DecorativeStoneDefs.values().length > meta) {
            final DecorativeStoneDefs ddef = DecorativeStoneDefs.values()[meta];
            final ArrayList<ItemStack> array = new ArrayList<>();
            if (ddef.hasDrop()) array.add(ddef.makeDropStack());
            else array.add(ddef.makeStack());
            return array;
        }
        return new ArrayList<>();
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (DecorativeStoneDefs s : DecorativeStoneDefs.values())
            s.registerIcon(reg);
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (DecorativeStoneDefs s : DecorativeStoneDefs.values())
            list.add(s.makeStack());
    }
}
