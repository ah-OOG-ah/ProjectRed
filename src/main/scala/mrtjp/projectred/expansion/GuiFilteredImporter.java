package mrtjp.projectred.expansion;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.gui.GuiDraw;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.core.color.Colors;
import mrtjp.core.gui.IconButtonNode;
import mrtjp.core.gui.NodeGui;
import mrtjp.core.gui.TGuiBuilder;
import mrtjp.core.resource.ResourceLib;
import mrtjp.core.vec.Point;
import mrtjp.core.vec.Size;
import mrtjp.core.world.WorldLib;
import mrtjp.projectred.core.libmc.PRResources;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class GuiFilteredImporter extends NodeGui implements TGuiBuilder {

    public TileFilteredImporter tile;

    public GuiFilteredImporter(Container c, TileFilteredImporter tile) {
        super(c, 176, 168);
        this.tile = tile;


        final IconButtonNode color = new IconButtonNode() {

            @Override
            public void drawButton(boolean mouseover) {
                if (tile.colour() == -1) {
                    ResourceLib.guiExtras().bind();
                    GuiDraw.drawTexturedModalRect(position.x, position.y, 40, 2, 11, 11);
                } else
                    GuiDraw.drawRect(
                            position.x + 2,
                            position.y + 2,
                            8,
                            8,
                            Colors.values()[tile.colour()].argb
                    );
            }

            @Override
            public void onButtonClicked() {
                tile.clientCycleColourUp();
            }
        };
        color.position = new Point(133, 37);
        color.size = new Size(13, 13);
        addChild(color);
    }

    @Override
    public void drawBack_Impl(Point mouse, float frame) {
        PRResources.guiFilteredImporter().bind();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 176, 168);
        GuiDraw.drawString("Filtered Importer", 8, 6, Colors.GREY.argb, false);
        GuiDraw.drawString("Inventory", 8, 75, Colors.GREY.argb, false);
    }

    @Override
    public int getID() {
        return ExpansionProxy.filteredImporterGui();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen buildGui(EntityPlayer player, MCDataInput data) {
        final TileFilteredImporter t = WorldLib.getTileEntity(
            player.worldObj,
            data.readCoord(),
            TileFilteredImporter.class
        );
        if (t != null) return new GuiFilteredImporter(t.createContainer(player), t);
        return null;
    }
}
