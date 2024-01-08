package mrtjp.projectred.compatibility.cptcoloredlights;

import mrtjp.projectred.compatibility.IPRPlugin;
import mrtjp.projectred.core.Configurator;
import mrtjp.projectred.illumination.IlluminationProxy;

import java.util.Arrays;

public class PluginColoredLights extends IPRPlugin {

    public static final String PRIll_modID = "ProjRed|Illumination";
    public static final String CL_modID = "easycoloredlights";

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
        IlluminationProxy.getLightValue() = (m, b) => {
            if (!(0 until 16 contains m)) b
      else {
                val c = Colors(m)
                CLApi.makeRGBLightValue(c.rF, c.gF, c.bF)
            }
        }
    }

    override def init() {}
    override def postInit() {}

    override def desc() = "Colored Lights: Illumination lighting"
}
