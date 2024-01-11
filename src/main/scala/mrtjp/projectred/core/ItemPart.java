package mrtjp.projectred.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.core.item.ItemCore;
import mrtjp.projectred.ProjectRedCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemPart extends ItemCore {

    public ItemPart() {
        super("projectred.core.part");
        setCreativeTab(ProjectRedCore.tabCore);
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (PartDefs i : PartDefs.values())
            list.add(i.makeStack());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister reg) {
        for (PartDefs i : PartDefs.values()) i.registerIcon(reg);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        if (PartDefs.values().length > meta)
            return PartDefs.values()[meta].icon;
        else return null;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (PartDefs.values().length > stack.getItemDamage())
            return getUnlocalizedName() + "." + PartDefs.values()[stack.getItemDamage()].toString();
        return super.getUnlocalizedName(stack);
    }
}
