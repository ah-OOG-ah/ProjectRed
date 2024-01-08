package mrtjp.projectred.illumination;

import codechicken.lib.colour.Colour;
import codechicken.lib.lighting.LightModel;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.ColourMultiplier;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.core.color.Colors_old;
import mrtjp.core.vec.InvertX$;
import mrtjp.projectred.core.RenderHalo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public abstract class LightObject {
    private ItemBaseLight item = null;
    private ItemBaseLight itemInv = null;

    public abstract String getItemName();
    public abstract String getType();

    public abstract Cuboid6 getBounds(int side);
    public abstract Cuboid6 getLBounds(int side);
    public Cuboid6[] bakedBoxes(Cuboid6 box) {
        final Cuboid6[] boxes = new Cuboid6[6];
        boxes[0] = box.copy();
        for (int s = 1; s < 6; ++s)
            boxes[s] = box.copy().apply(Rotation.sideRotations[s].at(Vector3.center));
        return boxes;
    }

    public Item getItem(boolean inv) {
        return (inv) ? itemInv : item;
    }
    public final void initServer() {
        item = createItem(false);
        itemInv = createItem(true);
    }

    public ItemStack makeStack(int color, int i) {
        return new ItemStack(item, i, color);
    }
    public ItemStack makeStack(int color) {
        return makeStack(color, 1);
    }

    public ItemStack makeInvStack(int color, int i) {
        return new ItemStack(itemInv, i, color);
    }
    public ItemStack makeInvStack(int color) {
        return makeInvStack(color, 1);
    }

    public ItemBaseLight createItem(boolean inverted) {
        return new ItemBaseLight(this, inverted);
    }
    public BaseLightPart createPart() {
        return new BaseLightFacePart(this);
    }

    public boolean canFloat() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void initClient() {
        final IItemRenderer renderer = new IItemRenderer() {

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
            public void renderItem(
                    ItemRenderType t,
                    ItemStack item,
                    Object... data
            ) {
                if (0 <= item.getItemDamage() && item.getItemDamage() < 16) // else invalid colour
                    renderInv(item.getItemDamage(), isInv(item.getItem()), t);
            }

            private boolean isInv(Item item) {
                if (item instanceof ItemBaseLight) {
                    return ((ItemBaseLight) item).inverted();
                }
                return false;
            }
        };

        MinecraftForgeClient.registerItemRenderer(item, renderer);
        MinecraftForgeClient.registerItemRenderer(itemInv, renderer);

        loadModels();
    }

    public abstract void loadModels();
    public Map<String, CCModel> parseModel(String name) {
        final Map<String, CCModel> models = CCModel.parseObjModels(
                new ResourceLocation(
                        "projectred",
                        "textures/obj/lighting/" + name + ".obj"
                ),
                7,
                InvertX$.MODULE$
        );
        for (CCModel m : models.values()) m.apply(new Translation(0.5, 0, 0.5));
        return models;
    }
    public CCModel bakeCopy(int s, CCModel m1) {
        final CCModel m = m1.copy();
        m.apply(Rotation.sideOrientation(s, 0).at(Vector3.center));
        finishModel(m);
        return m;
    }
    public CCModel finishModel(CCModel m) {
        m.computeNormals();
        m.computeLighting(LightModel.standardLightModel);
        return m.shrinkUVs(0.0005);
    }

    public abstract CCModel getModelBulb(int side);
    public abstract CCModel getModelChassi(int side);

    public CCModel getInvModelBulb() {
        return getModelBulb(0);
    }
    public CCModel getInvModelChassi() {
        return getModelChassi(0);
    }
    public Cuboid6 getInvLBounds() {
        return getLBounds(0);
    }

    public abstract IIcon getIcon();

    @SideOnly(Side.CLIENT)
    public abstract void registerIcons(IIconRegister reg);

    @SideOnly(Side.CLIENT)
    public void render(BaseLightPart part, int color, boolean isOn, Vector3 pos) {
        final IconTransformation icon = new IconTransformation(getIcon());
        final Translation t = pos.translation();
        TextureUtils.bindAtlas(0);
        getModelChassi(part.side()).render(t, icon);
        getModelBulb(part.side()).render(t, icon, cMult(color, isOn));
    }

    public Pair<Vector3, Double> getInvT(IItemRenderer.ItemRenderType t) {
        switch (t) {
            case ENTITY: return new ImmutablePair<>(new Vector3(-0.25d, 0d, -0.25d), 0.75d);
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                return new ImmutablePair<>(new Vector3(-0.15d, 0, -0.15d), 1.5d);
            case INVENTORY: return new ImmutablePair<>(new Vector3(0d, -0.05d, 0d), 1d);
            default: return new ImmutablePair<>(Vector3.zero, 1D);
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderInv(int color, boolean inverted, IItemRenderer.ItemRenderType t) {
        final IconTransformation icon = new IconTransformation(getIcon());
        final Pair<Vector3, Double> tmp = getInvT(t);
        final Vector3 pos = tmp.getLeft();
        final double scale = tmp.getRight();
        final Translation trans = new Translation(pos);

        // Repair render
        GL11.glPushMatrix();
        GL11.glTranslated(pos.x, pos.y, pos.z);
        GL11.glScaled(scale, scale, scale);
        TextureUtils.bindAtlas(0);
        final CCRenderState ccrsi = CCRenderState.instance();
        ccrsi.reset();
        ccrsi.setDynamic();
        ccrsi.pullLightmap();
        ccrsi.startDrawing();

        // Tessellate
        getInvModelChassi().render(trans, icon);
        getInvModelBulb().render(trans, icon, cMult(color, inverted));

        // Draw
        ccrsi.draw();

        // Render Halo
        if (inverted) {
            RenderHalo.prepareRenderState();
            RenderHalo.renderHalo(getInvLBounds(), color, trans);
            RenderHalo.restoreRenderState();
        }

        // Finish
        GL11.glPopMatrix();
    }

    public ColourMultiplier cMult(int color, boolean on) {
        final Colour c = Colors_old.get(color).c.copy();
        if (!on) c.multiply(Colors_old.LIGHT_GREY.c);
        return ColourMultiplier.instance(c.rgba());
    }
}
