package mrtjp.projectred.core;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mrtjp.core.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;

public class RenderHalo {

    public static final RenderHalo instance = new RenderHalo();

    private List<LightCache> renderList = new ArrayList<>();
    private final Vector3 renderEntityPos = new Vector3();
    private final Vector3 vec = new Vector3();

    private class LightCache implements Comparable<LightCache> {

        public final BlockCoord pos;
        public final int color;
        public final Cuboid6 cube;

        public LightCache(int x, int y, int z, int c, Cuboid6 cube) {
            this(new BlockCoord(x, y, z), c, cube);
        }

        public LightCache(BlockCoord pos, int color, Cuboid6 cube) {
            this.pos = pos;
            this.color = color;
            this.cube = cube;
        }

        private double renderDist() {
            return vec.set(pos.x, pos.y, pos.z).sub(renderEntityPos).magSquared();
        }

        @Override
        public int compareTo(LightCache o) {
            return Double.compare(o.renderDist(), renderDist());
        }
    }

    public void addLight(int x, int y, int z, int color, Cuboid6 box) {
        renderList.add(new LightCache(x, y, z, color, box));
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (renderList.isEmpty()) return;
        final World w = Minecraft.getMinecraft().theWorld;
        final Entity entity = Minecraft.getMinecraft().renderViewEntity;
        renderEntityPos.set(
                entity.posX,
                entity.posY + entity.getEyeHeight(),
                entity.posZ
        );

        renderList.sort(null);

        glPushMatrix();

        // Adjust translation for camera movement between frames (using camra coordinates for numeric stability).
        // Note: When porting to MC 1.8, might want to use GlStateManager.translate() here instead.
        glTranslated(
                entity.posX - (entity.posX - entity.lastTickPosX) * event.partialTicks - entity.lastTickPosX,
                entity.posY - (entity.posY - entity.lastTickPosY) * event.partialTicks - entity.lastTickPosY,
                entity.posZ - (entity.posZ - entity.lastTickPosZ) * event.partialTicks - entity.lastTickPosZ
        );

        prepareRenderState();
        final Iterator<LightCache> it = renderList.iterator();
        final int max = (Configurator.lightHaloMax < 0) ? renderList.size() : Configurator.lightHaloMax;

        int i = 0;
        while (i < max && it.hasNext()) {
            final LightCache cc = it.next();
            renderHalo(w, cc);
            ++i;
        }

        renderList.clear();
        restoreRenderState();
        glPopMatrix();
    }

    public void prepareRenderState() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glDepthMask(false);
        final CCRenderState ccrsi = CCRenderState.instance();
        ccrsi.reset();
        ccrsi.setDynamic();
        ccrsi.startDrawing();
    }

    public void restoreRenderState() {
        CCRenderState.instance().draw();
        glDepthMask(true);
        glColor4d(1, 1, 1, 1);
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_BLEND);
    }

    private void renderHalo(World world, LightCache cc) {
        CCRenderState.instance().setBrightness(world, cc.pos.x, cc.pos.y, cc.pos.z);
        // Make sure to use camera coordinates for the halo transformation.
        final Entity entity = Minecraft.getMinecraft().renderViewEntity;
        renderHalo(
                cc.cube,
                cc.color,
                new Translation(
                        cc.pos.x - entity.posX,
                        cc.pos.y - entity.posY,
                        cc.pos.z - entity.posZ
                )
        );
    }

    public void renderHalo(Cuboid6 cuboid, int colour, Transformation t) {
        final CCRenderState ccrsi = CCRenderState.instance();
        ccrsi.reset();
        ccrsi.setPipeline(t);
        ccrsi.baseColour = Colors.values()[colour].rgba;
        ccrsi.alphaOverride = 128;
        BlockRenderer.renderCuboid(cuboid, 0);
    }
}
