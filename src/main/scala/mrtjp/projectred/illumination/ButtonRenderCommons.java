package mrtjp.projectred.illumination;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.ColourMultiplier;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.TransformationList;
import codechicken.lib.vec.Translation;
import mrtjp.core.color.Colors;
import mrtjp.projectred.core.RenderHalo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public abstract class ButtonRenderCommons implements IItemRenderer {

    public final Cuboid6 invRenderBox =
            new Cuboid6(0.0, 0.375, 0.5 - 0.1875, 0.25, 0.625, 0.5 + 0.1875);
    public final Cuboid6 invLightBox = invRenderBox.copy().expand(0.025d);

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
            case ENTITY                : render(item, -0.05, 0, -0.1, 0.5); break;
            case EQUIPPED              :
            case EQUIPPED_FIRST_PERSON : render(item, 0.2, 0, 0, 1); break;
            case INVENTORY             : render(item, 0.25, 0, 0, 1); break;
            default                    :
        }
    }

    private void render(ItemStack item, double x, double y, double z, double s) {
        final int color = item.getItemDamage();
        if (0 <= color && color < 16) {
            final IconTransformation icon = new IconTransformation(ItemPartButton.icon());
            final TransformationList t = new Scale(s).with(new Translation(x, y, z));

            GL11.glPushMatrix();

            TextureUtils.bindAtlas(0);
            final CCRenderState ccrsi = CCRenderState.instance();
            ccrsi.reset();
            ccrsi.setDynamic();
            ccrsi.pullLightmap();
            ccrsi.startDrawing();

            ccrsi.setPipeline(
                    t,
                    icon,
                    new ColourMultiplier(Colors.values()[color].rgba)
            );
            BlockRenderer.renderCuboid(invRenderBox, 0);
            drawExtras(t);

            ccrsi.draw();
            RenderHalo.prepareRenderState();
            RenderHalo.renderHalo(invLightBox, color, t);
            RenderHalo.restoreRenderState();
            GL11.glPopMatrix();
        }
    }

    public void drawExtras(Transformation t) {}
}
