package mrtjp.projectred.illumination;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Translation;
import codechicken.lib.render.TextureUtils;
import mrtjp.projectred.core.RenderHalo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class LampTESR extends TileEntitySpecialRenderer implements IItemRenderer {

    public static final LampTESR instance = new LampTESR();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType t) {
        return true;
    }
    @Override
    public boolean shouldUseRenderHelper(
        ItemRenderType t,
        ItemStack item,
        ItemRendererHelper helper
    ) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType t, ItemStack item, Object... data) {

        switch (t) {
            case ENTITY                : render(item, -0.15, 0, -0.15, 1);
            case EQUIPPED              : render(item, 0, 0, 0, 1);
            case EQUIPPED_FIRST_PERSON : render(item, 0, 0, 0, 1);
            case INVENTORY             : render(item, 0, -0.05, 0, 0.95);
            default                    :
        }
    }

    private void render(ItemStack item, double x, double y, double z, double s) {
        final int meta = item.getItemDamage();
        final IconTransformation icon = new IconTransformation(
            (meta > 15) ? BlockLamp.on().apply(meta % 16) : BlockLamp.off().apply(meta)
        );

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glScaled(s, s, s);
        TextureUtils.bindAtlas(0);
        final CCRenderState ccrsi = CCRenderState.instance();
        ccrsi.reset();
        ccrsi.setDynamic();
        ccrsi.pullLightmap();
        ccrsi.startDrawing();

        final Translation t = new Translation(x, y, z);
        ccrsi.setPipeline(t, icon);
        BlockRenderer.renderCuboid(Cuboid6.full, 0);
        ccrsi.draw();

        if (meta > 15) {
            RenderHalo.prepareRenderState();
            RenderHalo.renderHalo(lBounds, meta % 16, t);
            RenderHalo.restoreRenderState();
        }

        GL11.glPopMatrix();
    }

    private final Cuboid6 lBounds = Cuboid6.full.copy().expand(0.05d);

    @Override
    public void renderTileEntityAt(
            TileEntity te,
            double x,
            double y,
            double z,
            float partials
    ) {

        if (te instanceof ILight) {
            if (((ILight) te).isOn()) {
                final int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
                RenderHalo.addLight(te.xCoord, te.yCoord, te.zCoord, meta, lBounds);
            }
        }
    }
}
