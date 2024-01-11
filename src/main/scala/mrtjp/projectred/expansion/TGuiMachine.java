package mrtjp.projectred.expansion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Implementers of this interface must also extend TileMachine. The Scala trait did too, but I can't enforce that because
 * TileMachine is an abstract class. Additionally, onBlockActivated needs to be manually called in onBlockActivated.
 */
public interface TGuiMachine {

    default boolean onBlockActivated(World tileWorld, EntityPlayer player, int side) {
        if (!player.isSneaking()) {
            if (!tileWorld.isRemote) openGui(player);
            return true;
        }
        return false;
    }

    void openGui(EntityPlayer player);

    Container createContainer(EntityPlayer player);
}
