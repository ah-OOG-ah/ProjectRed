package mrtjp.projectred.illumination;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Cuboid6;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import java.util.Map;

public class LightObjCage extends LightObject {

    public static final LightObjCage instance = new LightObjCage();

    public final Cuboid6[] bounds = bakedBoxes(
            new Cuboid6(3.5 / 16d, 0, 3.5 / 16d, 12.5 / 16d, 12 / 16d, 12.5 / 16d)
    );
    public final Cuboid6[] lBounds = bakedBoxes(
            new Cuboid6(
                    4.5 / 16d,
                    1.5 / 16,
                    4.5 / 16d,
                    11.5 / 16d,
                    11.5 / 16d,
                    11.5 / 16d
            )
    );

    public IIcon icon;

    public final CCModel[] bulbModels = new CCModel[6];
    public final CCModel[] chassiModels = new CCModel[6];

    @Override
    public String getItemName() {
        return "projectred.illumination.cagelamp2";
    }
    @Override
    public String getType() {
        return "pr_cagelamp2";
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
        icon = reg.registerIcon("projectred:lighting/cage_lamp");
    }

    @Override
    public void loadModels() {
        final Map<String, CCModel> models = parseModel("cagelamp");
        final CCModel chassi = models.get("chassi");
        final CCModel bulb = models.get("bulb");

        for (int s = 0; s < 6; ++s) {
            bulbModels[s] = bakeCopy(s, bulb);
            chassiModels[s] = bakeCopy(s, chassi);
        }
    }
}
