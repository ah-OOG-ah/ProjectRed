package mrtjp.projectred.compatibility.cptcoloredlights;

import coloredlightscore.src.api.CLApi;
import mrtjp.core.color.Colors;
import mrtjp.projectred.compatibility.IPRPlugin;
import mrtjp.projectred.core.Configurator;
import mrtjp.projectred.illumination.IlluminationProxy;
import mrtjp.projectred.illumination.IlluminationProxy$;

import java.util.Arrays;

public class PluginColoredLights implements IPRPlugin {

    public static final PluginColoredLights instance = new PluginColoredLights();

    public final String PRIll_modID = "ProjRed|Illumination";
    public final String CL_modID = "easycoloredlights";

    @Override
    public String[] getModIDs() {
        return new String[]{CL_modID, PRIll_modID};
    }

    @Override
    public boolean isEnabled() {
        return Configurator.compat_ColoredLights;
    }

    @Override
    public void preInit() {
        IlluminationProxy$.MODULE$.getLightValue = (m, b) -> {
            if (!(0 <= m && m < 16)) return b;
            else {
                final Colors c = Colors.values()[m];
                return CLApi.makeRGBLightValue(c.rF, c.gF, c.bF);
            }
        };
    }

    @Override
    public void init() {}
    @Override
    public void postInit() {}

    public String desc() {
        return  "Colored Lights: Illumination lighting";
    }
}
