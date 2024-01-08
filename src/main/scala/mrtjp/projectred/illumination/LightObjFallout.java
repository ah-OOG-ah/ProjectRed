package mrtjp.projectred.illumination;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Cuboid6;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import java.util.Map;

public class LightObjFallout extends LightObject {

    public static final LightObjFallout instance = new LightObjFallout();

    public final Cuboid6[] bounds = bakedBoxes(
            new Cuboid6(2 / 16d, 0, 2 / 16d, 14 / 16d, 11 / 16d, 14 / 16d)
    );
    public final Cuboid6[] lBounds = bakedBoxes(
            new Cuboid6(4 / 16d, 1.5 / 16, 4 / 16d, 12 / 16d, 10 / 16d, 12 / 16d)
                    .expand(-0.002D)
    );

    public IIcon icon;

    public final CCModel[] bulbModels = new CCModel[6];
    public final CCModel[] chassiModels = new CCModel[6];

    @Override
    public String getItemName() {
        return  "projectred.illumination.cagelamp";
    }
    @Override
    public String getType() {
        return  "pr_cagelamp";
    }

    @Override
    public Cuboid6 getBounds(int side) {
        return bounds[side];
    }
    @Override
    public Cuboid6 getLBounds(int side) {
        return lBounds[side];
    }

    @Override
    public CCModel getModelBulb(int side) {
        return bulbModels[side];
    }
    @Override
    public CCModel getModelChassi(int side) {
        return chassiModels[side];
    }

    @Override
    public IIcon getIcon() {
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        icon = reg.registerIcon("projectred:lighting/fallout");
    }

    @Override
    public void loadModels() {
        final Map<String, CCModel> models = parseModel("fallout");
        final CCModel chassi = models.get("chassi");
        final CCModel bulb = models.get("bulb");

        for (int s = 0; s < 6; ++s) {
            bulbModels[s] = bakeCopy(s, bulb);
            chassiModels[s] = bakeCopy(s, chassi);
        }
    }
}
