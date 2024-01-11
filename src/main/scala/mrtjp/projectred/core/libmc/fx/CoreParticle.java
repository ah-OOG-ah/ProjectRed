package mrtjp.projectred.core.libmc.fx;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import mrtjp.core.color.Colors;
import mrtjp.core.color.Colors_old;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CoreParticle extends EntityFX {
    public CoreParticle(World w, double px, double py, double pz) {
        super(w, 0, 0, 0, 0, 0, 0);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;

        this.setPosition(px, py, pz);
        this.noClip = true;
        this.isDead = false;
        this.particleMaxAge = 20 + rand.nextInt(20);
        this.particleGravity = 1;
    }

    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    public float a = 1.0f;

    public float scaleX = 0.2f;
    public float scaleY = 0.2f;
    public float scaleZ = 0.2f;

    private boolean ignoreMaxAge = false;

    public boolean ignoreNoLogics = false;
    public boolean ignoreVelocity = false;

    @Override
    public boolean isBurning() {
        return false;
    }

    public List<ParticleLogic> logics = new ArrayList<>();

    public void setTextureByName(String name) {
        particleIcon = ParticleIconRegistry.instance.getIcon(name);
    }

    public void setRGBColorF(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setPRColor(Colors color) {
        setRGBColorF(
                (color.c.r & 0xff) / 255f,
                (color.c.g & 0xff) / 255f,
                (color.c.b & 0xff) / 255f
        );
    }

    @Deprecated
    public void setPRColor(Colors_old color) {
        setPRColor(Colors.values()[color.ordinal()]);
    }

    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.scaleZ = scale;
    }

    public void setIgnoreMaxAge(boolean ignore) {
        this.ignoreMaxAge = ignore;
        this.particleAge = 0;
    }

    public CoreParticle add(ParticleLogic logic) {
        this.logics.add(logic);
        this.logics.sort(LogicComparator::compare);
        return this;
    }

    public CoreParticle addAll(Collection<ParticleLogic> it) {
        this.logics.addAll(it);
        this.logics.sort(LogicComparator::compare);
        return this;
    }

    @Override
    public int getBrightnessForRender(float par1) {
        float f = (particleAge + par1) / particleMaxAge;
        if (f < 0.0f) f = 0.0f;
        if (f > 1.0f) f = 1.0f;

        final int i = super.getBrightnessForRender(par1);
        int j = i & 0xff;
        final int k = i >> 16 & 0xff;
        j += (int) (f * 15.0f * 16.0f);
        if (j > 240) j = 240;

        return j | k << 16;
    }

    @Override
    public float getBrightness(float par1) {
        float f = (particleAge + par1) / particleMaxAge;
        if (f < 0.0f) f = 0.0f;
        if (f > 1.0f) f = 1.0f;
        final float f1 = super.getBrightness(par1);
        return f1 * f + (1.0f - f);
    }

    @Override
    public void onUpdate() {
        ticksExisted += 1;
        prevDistanceWalkedModified = distanceWalkedModified;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        prevRotationPitch = rotationPitch;
        prevRotationYaw = rotationYaw;

        if (!ignoreVelocity) moveEntity(motionX, motionY, motionZ);

        iterate();

        logics = logics.stream().filter(pl -> !pl.getFinished()).collect(Collectors.toCollection(ArrayList::new));

        super.particleAge = this.particleAge + 1;
        if (this.particleAge - 1 > this.particleMaxAge && !this.ignoreMaxAge || !this.ignoreNoLogics && this.logics.isEmpty())
            setDead();
    }

    private void iterate() {
        for (ParticleLogic l : logics) if (!l.getFinished()) {
            l.onUpdate(worldObj, this);
            if (l.isFinalLogic()) return;
        }
    }

    public Vector3 position() {
        return new Vector3(posX, posY, posZ);
    }

    public BlockCoord blockPosition() {
        return new BlockCoord(
                (int) Math.floor(posX),
                (int) Math.floor(posY),
                (int) Math.floor(posZ)
        );
    }

    @Override
    public void entityInit() {}

    @Override
    public int getFXLayer() {
        return 2;
    }

    @Override
    public void renderParticle(
            Tessellator tessellator,
            float partialframe,
            float cosyaw,
            float cospitch,
            float sinyaw,
            float sinsinpitch,
            float cossinpitch
    ) {
        if (!worldObj.isRemote) return;

        final float f11 = (float) (prevPosX + (posX - prevPosX) * partialframe - EntityFX.interpPosX);
        final float f12 = (float) (prevPosY + (posY - prevPosY) * partialframe - EntityFX.interpPosY);
        final float f13 = (float) (prevPosZ + (posZ - prevPosZ) * partialframe - EntityFX.interpPosZ);

        if (particleIcon == null) return;

        tessellator.setBrightness(251658480);
        tessellator.setColorRGBA_F(r, g, b, a);

        final float min_u = particleIcon.getMinU();
        final float min_v = particleIcon.getMinV();
        final float max_u = particleIcon.getMaxU();
        final float max_v = particleIcon.getMaxV();

        tessellator.addVertexWithUV(
                f11 - cosyaw * scaleX - sinsinpitch * scaleX,
                f12 - cospitch * scaleY,
                f13 - sinyaw * scaleZ - cossinpitch * scaleZ,
                max_u,
                max_v
        );
        tessellator.addVertexWithUV(
                f11 - cosyaw * scaleX + sinsinpitch * scaleX,
                f12 + cospitch * scaleY,
                f13 - sinyaw * scaleZ + cossinpitch * scaleZ,
                max_u,
                min_v
        );
        tessellator.addVertexWithUV(
                f11 + cosyaw * scaleX + sinsinpitch * scaleX,
                f12 + cospitch * scaleY,
                f13 + sinyaw * scaleZ + cossinpitch * scaleZ,
                min_u,
                min_v
        );
        tessellator.addVertexWithUV(
                f11 + cosyaw * scaleX - sinsinpitch * scaleX,
                f12 - cospitch * scaleY,
                f13 + sinyaw * scaleZ - cossinpitch * scaleZ,
                min_u,
                max_v
        );
    }
}
