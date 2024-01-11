package mrtjp.projectred.core;

import mrtjp.projectred.ProjectRedCore;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemDrawPlate extends ItemCraftingDamage {

    public ItemDrawPlate() {
        super("projectred.core.drawplate");
        setMaxDamage(512);
        setCreativeTab(ProjectRedCore.tabCore);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        itemIcon = reg.registerIcon("projectred:base/draw_plate");
    }
}
