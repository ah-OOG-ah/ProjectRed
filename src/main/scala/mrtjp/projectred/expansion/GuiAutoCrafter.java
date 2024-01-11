package mrtjp.projectred.expansion;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.gui.GuiDraw;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrtjp.core.color.Colors;
import mrtjp.core.gui.GuiLib;
import mrtjp.core.gui.IconButtonNode;
import mrtjp.core.gui.ItemDisplayNode;
import mrtjp.core.gui.NodeGui;
import mrtjp.core.gui.TGuiBuilder;
import mrtjp.core.vec.Point;
import mrtjp.core.vec.Size;
import mrtjp.core.world.WorldLib;
import mrtjp.projectred.core.TPowerDrawPoint;
import mrtjp.projectred.core.libmc.PRResources;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.input.Keyboard;

public class GuiAutoCrafter extends NodeGui implements TGuiBuilder {

    public TileAutoCrafter tile;
    public ContainerAutoCrafter c;

    public GuiAutoCrafter(TileAutoCrafter tile, ContainerAutoCrafter c) {
        super(c, 176, 212);
        this.tile = tile;
        this.c = c;

        final IconButtonNode cycle = new IconButtonNode() {
            @Override
            public void drawButton(boolean mouseover) {
                PRResources.guiAutoCrafter().bind();
                drawTexturedModalRect(position.x, position.y, 176, 0, 14, 14);
            }
        };
        cycle.position = new Point(59, 41);
        cycle.size = new Size(14, 14);
        cycle.clickDelegate = tile::sendCyclePlanSlot;
        addChild(cycle);
    }

    @Override
    public void drawBack_Impl(Point mouse, float rframe) {
        PRResources.guiAutoCrafter().bind();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, size.width, size.height);

        final Point s = new Point(18, 18)
                .multiply(tile.planSlot() % 3, tile.planSlot() / 3)
                .add(98, 22)
                .subtract(3);
        GuiDraw.drawTexturedModalRect(s.x, s.y, 193, 0, 22, 22);

        if (((TPowerDrawPoint) tile.cond()).canWork())
            GuiDraw.drawTexturedModalRect(16, 16, 177, 18, 7, 9);
        GuiLib.drawVerticalTank(
                16,
                26,
                177,
                27,
                7,
                48,
                ((TPowerDrawPoint) tile.cond()).getChargeScaled(48)
        );

        if (((TPowerDrawPoint) tile.cond()).flow() == -1)
            GuiDraw.drawTexturedModalRect(27, 16, 185, 18, 7, 9);
        GuiLib.drawVerticalTank(27, 26, 185, 27, 7, 48, ((TPowerDrawPoint) tile.cond()).getFlowScaled(48));

        final ItemStack plan = tile.getStackInSlot(tile.planSlot());
        if (plan != null && ItemPlan.hasRecipeInside(plan))
            ItemDisplayNode.renderItem(
                    new Point(152, 58),
                    new Size(16, 16),
                    getZPosition(),
                    true,
                    ItemPlan.loadPlanOutput(plan)
            );

        GuiDraw.drawString("Auto Crafting Bench", 8, 6, Colors.GREY.argb, false);
        GuiDraw.drawString("Inventory", 8, 120, Colors.GREY.argb, false);
    }


    @Override
    public void drawFront_Impl(Point mouse, float rframe) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            GuiProjectBench.drawPlanOutputOverlay(c.slots());
    }

    @Override
    public int getID() {
        return ExpansionProxy.autoCrafterGui();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen buildGui(EntityPlayer player, MCDataInput data) {
        final TileEntity t = WorldLib.getTileEntity(player.worldObj, data.readCoord());
        if (t instanceof TileAutoCrafter) {
            return new GuiAutoCrafter((TileAutoCrafter) t, ((TileAutoCrafter) t).createContainer(player));
        }
        return null;
    }
}
