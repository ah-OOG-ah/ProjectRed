package mrtjp.projectred.illumination;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;

public class LightObjLantern extends LightObject {

    public static final LightObjLantern instance = new LightObjLantern();

    private final Cuboid6 bounds = new Cuboid6(0.35d, 0.25d, 0.35d, 0.65d, 0.75d, 0.65d);
    private final Cuboid6 lBounds = bounds.copy().expand(-1 / 64d);

    public IIcon icon = null;

    public CCModel bulbModel = null;
    public final CCModel[] chassiModels = new CCModel[7];

    @Override
    public String getItemName() {
        return  "projectred.illumination.lantern";
    }
    @Override
    public String getType() {
        return "pr_lantern";
    }

    @Override
    public Cuboid6 getBounds(int side) {
        return bounds;
    }
    @Override
    public Cuboid6 getLBounds(int side) {
        return lBounds;
    }

    @Override
    public BaseLightPart createPart() {
        return new BaseLightPart(this);
    }

    @Override
    public CCModel getModelBulb(int side) {
        return bulbModel;
    }
    @Override
    public CCModel getModelChassi(int side) {
        return chassiModels[side];
    }
    @Override
    public CCModel getInvModelChassi() {
        return chassiModels[6];
    }

    @Override
    public IIcon getIcon() {
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        icon = reg.registerIcon("projectred:lighting/lantern");
    }

    @Override
    public Pair<Vector3, Double> getInvT(ItemRenderType t) {
        if (t == INVENTORY) {
            return new ImmutablePair<>(new Vector3(0D, -0.05D, 0D), 1.5D);
        }
        return super.getInvT(t);
    }

    @Override
    public void loadModels() {
        final Map<String, CCModel> models = parseModel("lantern");

        final CCModel bulb = models.get("bulb");
        final CCModel body = models.get("body");
        final CCModel top = models.get("standtop");
        final CCModel topRing = models.get("goldringtop");
        final CCModel bottom = models.get("standbottom");
        final CCModel bottomRing = models.get("goldringbottom");
        final CCModel side = models.get("standside");

        bulbModel = bulb;
        chassiModels[0] = CCModel.combine(Arrays.asList(body, bottom, bottomRing));
        chassiModels[1] = CCModel.combine(Arrays.asList(body, top, topRing));
        chassiModels[6] = CCModel.combine(Arrays.asList(body, topRing)); // Inv model

        for (int s = 2; s < 6; ++s) {
            final CCModel mSide = side.copy().apply(
                    Rotation
                            .sideOrientation(0, Rotation.rotationTo(0, s))
                            .at(Vector3.center)
            );
            final CCModel mRing = topRing.copy().apply(
                    Rotation
                            .sideOrientation(0, Rotation.rotationTo(0, s))
                            .at(Vector3.center)
            );
            chassiModels[s] = CCModel.combine(Arrays.asList(body, mSide, mRing));
        }

        Arrays.stream(chassiModels).forEach(this::finishModel);
        finishModel(bulbModel);
    }
}
