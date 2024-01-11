package mrtjp.projectred.expansion;

import mrtjp.core.gui.GuiLib;
import mrtjp.core.gui.Slot3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ContainerAutoCrafter extends ContainerPoweredMachine {

    public EntityPlayer player;
    public TileAutoCrafter tile;

    public ContainerAutoCrafter(EntityPlayer player, TileAutoCrafter tile) {
        super(tile);
        this.player = player;
        this.tile = tile;

        List<Pair<Integer, Integer>> tmp = GuiLib.createSlotGrid(98, 22, 3, 3, 0, 0);

        for (int i = 0; i < tmp.size(); ++i) {

            final Slot3 s = new Slot3(tile, i, tmp.get(i).getLeft(), tmp.get(i).getRight());
            s.canPlaceDelegate = is -> is.getItem() instanceof ItemPlan && ItemPlan.hasRecipeInside(is);
            addSlotToContainer(s);
        }

        tmp = GuiLib.createSlotGrid(8, 80, 9, 2, 0, 0);

        for (int i = 0; i < tmp.size(); ++i) {

            addSlotToContainer(new Slot3(tile, i + 9, tmp.get(i).getLeft(), tmp.get(i).getRight()));
        }

        addPlayerInv(player, 8, 130);
    }

    public int slot = -1;

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (ICrafting i : crafters) {

            if (slot != tile.planSlot())
                i.sendProgressBarUpdate(this, 3, tile.planSlot());
            slot = tile.planSlot();
        }
    }

    @Override
    public void updateProgressBar(int id, int bar) {
        if (id == 3) {
            tile.planSlot_$eq(bar);
        }
        super.updateProgressBar(id, bar);
    }

    @Override
    public boolean doMerge(ItemStack stack, int from) {
        if (0 <= from && from < 9) { // plan slots

            return tryMergeItemStack(stack, 27, 63, false); // merge to inventory
        } else if (9 <= from && from < 27) { // storage

            if (stack.getItem() instanceof ItemPlan)
                return tryMergeItemStack(stack, 0, 9, false); // merge to plan
            return tryMergeItemStack(stack, 27, 63, false); // merge to inventory
        } else if (27 <= from && from < 63) { // player inventory

            if (stack.getItem() instanceof ItemPlan)
                return tryMergeItemStack(stack, 0, 9, false); // merge to plan
            return tryMergeItemStack(stack, 9, 27, false); // merge to storage
        }
        return false;
    }
}
