package mrtjp.projectred.illumination;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.Vertex5;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Translation;
import net.minecraft.init.Blocks;

public class RenderFButton extends ButtonRenderCommons {

    public static final RenderFButton instance = new RenderFButton();

    final CCModel model = genModel(16, 2, 8);
    @Override
    public void drawExtras(Transformation t) {
        model.render(t, new IconTransformation(Blocks.redstone_torch.getIcon(0, 0)));
    }

    private CCModel genModel(int height, double x, double z) {
        final CCModel m = CCModel.quadModel(20);
        m.verts[0] = new Vertex5(7 / 16d, 10 / 16d, 9 / 16d, 7 / 16d, 8 / 16d);
        m.verts[1] = new Vertex5(9 / 16d, 10 / 16d, 9 / 16d, 9 / 16d, 8 / 16d);
        m.verts[2] = new Vertex5(9 / 16d, 10 / 16d, 7 / 16d, 9 / 16d, 6 / 16d);
        m.verts[3] = new Vertex5(7 / 16d, 10 / 16d, 7 / 16d, 7 / 16d, 6 / 16d);
        m.generateBlock(
                4,
                6 / 16d,
                (10 - height) / 16d,
                7 / 16d,
                10 / 16d,
                11 / 16d,
                9 / 16d,
                0x33
        );
        m.generateBlock(
                12,
                7 / 16d,
                (10 - height) / 16d,
                6 / 16d,
                9 / 16d,
                11 / 16d,
                10 / 16d,
                0xf
        );
        m.apply(new Translation(-0.5 + x / 16, (height - 10) / 16d, -0.5 + z / 16));
        m.computeNormals();
        m.shrinkUVs(0.0005);
        m.apply(new Scale(1.0005));
        return m;
    }
}
