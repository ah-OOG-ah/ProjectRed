package mrtjp.projectred.illumination;

import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import mrtjp.projectred.core.IProxy;
import org.luaj.vm2.ast.Str;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mrtjp.projectred.ProjectRedIllumination.*;

public class IlluminationProxy_server implements MultiPartRegistry.IPartFactory, IProxy {

    public List<LightObject> lights =
            Arrays.asList(LightObjLantern.instance, LightObjFixture, LightObjFallout, LightObjCage);

    @Override
    public void preinit() {}

    @Override
    public void init() {
        // possible mis-translation
        List<String> tmp = lights.stream().map(LightObject::getType).collect(Collectors.toList());
        tmp.add("pr_lightbutton");
        tmp.add("pr_flightbutton");
        MultiPartRegistry.registerParts(
                this,
                tmp.toArray(new String[0])
        );
        for (LightObject l : lights) l.initServer();

        itemPartIllumarButton = new ItemPartButton();
        itemPartIllumarFButton = new ItemPartFButton();

        blockLamp = new BlockLamp();
        blockLamp.addSingleTile(TileLamp.class);

        blockAirousLight = new BlockAirousLight();
        blockAirousLight.bindTile(TileAirousLight.class);

        IlluminationRecipes.initRecipes();

        LightMicroMaterial.register();
    }

    @Override
    public void postinit() {}

    @Override
    public TMultiPart createPart(String name, boolean client) {
        switch (name) {
            case "pr_lightbutton": return new LightButtonPart();
            case "pr_flightbutton": return new FLightButtonPart();
            default: return getLight(name);
        }
    }

    private TMultiPart getLight(String name) {
        LightObject tmp = lights.stream().filter(l -> l.getType().equals(name)).findFirst().orElse(null);
        if (tmp != null) return tmp.createPart();
        return null;
    }

    @Override
    public String version() {
        return  "@VERSION@";
    }
    @Override
    public String build() {
        return  "@BUILD_NUMBER@";
    }
}
