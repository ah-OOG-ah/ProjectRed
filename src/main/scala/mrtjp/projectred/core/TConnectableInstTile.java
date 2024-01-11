package mrtjp.projectred.core;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.PartMap;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import mrtjp.core.block.InstancedBlockTile;
import mrtjp.core.world.WorldLib;
import mrtjp.projectred.api.IConnectable;
import mrtjp.projectred.core.libmc.PRLib;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TConnectableInstTile extends InstancedBlockTile implements IConnectable {

    /**
     * From TTileAcquisitions
     **/

    public TMultiPart getStraightCenter(int s) {
        final BlockCoord pos = posOfInternal().offset(s);
        final TileMultipart t = PRLib.getMultipartTile(world(), pos);
        if (t != null) return t.partMap(6);
        return null;
    }

    public TMultiPart getStraight(int s, int edgeRot) {
        final BlockCoord pos = posOfStraight(s);
        final TileMultipart t = PRLib.getMultipartTile(world(), pos);
        if (t != null) return t.partMap(Rotation.rotateSide(s ^ 1, edgeRot));
        return null;
    }

    public TMultiPart getCorner(int s, int edgeRot) {
        final BlockCoord pos = posOfCorner(s, edgeRot);
        final TileMultipart t = PRLib.getMultipartTile(world(), pos);
        if (t != null) return t.partMap(s ^ 1);
        return null;
    }

    public BlockCoord posOfStraight(int s) {
        return new BlockCoord(this).offset(s);
    }

    public BlockCoord posOfCorner(int s, int edgeRot) {
        return new BlockCoord(this).offset(s).offset(Rotation.rotateSide(s ^ 1, edgeRot));
    }

    public BlockCoord posOfInternal() {
        return new BlockCoord(this);
    }

    public int rotFromStraight(int s, int edgeRot) {
        return Rotation.rotationTo(Rotation.rotateSide(s ^ 1, edgeRot), s ^ 1);
    }

    public int rotFromCorner(int s, int edgeRot) {
        return Rotation.rotationTo(s ^ 1, Rotation.rotateSide(s ^ 1, edgeRot) ^ 1);
    }

    public void notifyStraight(int s) {
        final BlockCoord pos = posOfStraight(s);
        world().notifyBlockOfNeighborChange(pos.x, pos.y, pos.z, getBlockType());
    }

    public void notifyCorner(int s, int edgeRot) {
        final BlockCoord pos = posOfCorner(s, edgeRot);
        world().notifyBlockOfNeighborChange(pos.x, pos.y, pos.z, getBlockType());
    }

    /** End TTileAcquisitions **/

    /** From TTileConnectable **/

    /**
     * -> full block connection mask
     * <p>
     * 0000 0000 EEEE WWWW SSSS NNNN UUUU DDDD | 00FF FFFF EEEE WWWW SSSS NNNN
     * UUUU DDDD
     * <p>
     * For a full block, you can have a full 6 sides of connection, with 5 on
     * each side.
     * <p>
     * First 8 nibbles, straight connections, of nibble of 1 << r where r is a
     * Rotation.rotationTo(blockSide, edgeSide) D - Down U - Up N - North S -
     * South W - West E - East F - Straight connections to center part or another
     * full block
     * <p>
     * Second 8 nibbles, corner face connections: D - Down U - Up N - North S -
     * South W - West E - East
     */
    public long connMap = 0L;

    @Override
    public boolean connectStraight(IConnectable part, int s, int edgeRot) {
        if (canConnectPart(part, s, edgeRot)) {
            final long old = connMap;

            if (edgeRot > -1) connMap |= (0x1 << edgeRot) << s * 4;
            else connMap |= 0x1000000 << s * 4;

            if (old != connMap) onMaskChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean connectCorner(IConnectable part, int s, int edgeRot) {
        if (canConnectPart(part, s, edgeRot)) {
            final long old = connMap;
            connMap |= (0x100000000L << edgeRot) << s * 4;
            if (old != connMap) onMaskChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean connectInternal(IConnectable part, int r) {
        return false;
    }

    @Override
    public boolean canConnectCorner(int r) {
        return false;
    }

    public abstract boolean canConnectPart(IConnectable part, int s, int edgeRot);

    /*public void onMaskChanged() {
    }*/

    public boolean outsideCornerEdgeOpen(int s, int edgeRot) {
        final BlockCoord pos = posOfInternal().offset(s);
        if (world().isAirBlock(pos.x, pos.y, pos.z)) return true;
        else {
            final int side1 = s ^ 1;
            final int side2 = Rotation.rotateSide(s ^ 1, edgeRot);
            final TileMultipart t = PRLib.getMultipartTile(world(), pos);
            if (t != null) {
                return t.partMap(side1) == null && t.partMap(side2) == null && t.partMap(
                        PartMap.edgeBetween(side1, side2)
                ) == null;
            }
            return false;
        }
    }

    public boolean discoverStraightCenter(int s) {
        final TMultiPart ic = getStraightCenter(s);
        if (ic instanceof IConnectable) {
            return canConnectPart((IConnectable) ic, s, -1) && ((IConnectable) ic).connectStraight(this, s ^ 1, -1);
        }
        return discoverStraightOverride(s);
    }

    public boolean discoverStraight(int s, int edgeRot) {
        final TMultiPart ic = getStraight(s, edgeRot);
        if (ic instanceof IConnectable) {
            return canConnectPart((IConnectable) ic, s, edgeRot) && ((IConnectable) ic).connectStraight(this, rotFromStraight(s, edgeRot), edgeRot);
        }
        return false;
    }

    public boolean discoverCorner(int s, int edgeRot) {
        final TMultiPart ic = getCorner(s, edgeRot);
        if (ic instanceof IConnectable) {
            return canConnectPart((IConnectable) ic, s, edgeRot)
                    && outsideCornerEdgeOpen(s, edgeRot)
                    && ((IConnectable) ic).canConnectCorner(rotFromCorner(s, edgeRot))
                    && ((IConnectable) ic).connectCorner(this, rotFromCorner(s, edgeRot), -1);
        }
        return false;
    }

    public boolean discoverStraightOverride(int s) { // TODO remove to discoverStraightCenterOVerride
        final BlockCoord pos = posOfInternal().offset(s);
        final TConnectableInstTile t = WorldLib.getTileEntity(getWorldObj(), pos, TConnectableInstTile.class);
        if (t != null && canConnectPart(t, s, -1))
            return t.connectStraight(this, s ^ 1, -1);
        return false;
    }

    public boolean updateExternals() {

        long connMap2 = 0L;

        for (int s = 0; s < 6; ++s) {
            if (discoverStraightCenter(s)) connMap2 |= 0x1000000 << s;

            for (int edgeRot = 0; edgeRot < 4; ++edgeRot)
                if (discoverStraight(s, edgeRot))
                    connMap2 |= (0x1 << edgeRot) << s * 4;

            for (int edgeRot = 0; edgeRot < 4; ++edgeRot)
                if (discoverCorner(s, edgeRot))
                    connMap2 |= (0x100000000L << edgeRot) << s * 4;
        }

        if (connMap != connMap2) {
            connMap = connMap2;
            onMaskChanged();
            return true;
        }
        return false;
    }

    public boolean maskConnects(int s) {
        return (connMap & (0xf0000000fL << (s * 4) | 0x1000000L << s)) != 0;
    }

    public boolean maskConnectsStraightCenter(int s) {
        return (connMap & 0x1000000L << s) != 0;
    }

    public boolean maskConnectsStraight(int s, int edgeRot) {
        return (connMap & ((1 << edgeRot) << s * 4)) != 0;
    }

    public boolean maskConnectsCorner(int s, int edgeRot) {
        return (connMap & ((0x100000000L << s * 4) << edgeRot)) != 0;
    }

    /** End TTileConnectable **/

    public boolean clientNeedsMap() {
        return false;
    }

    @Override
    public void save(NBTTagCompound tag) {
        super.save(tag);
        tag.setLong("connMap", connMap);
    }

    @Override
    public void load(NBTTagCompound tag) {
        super.load(tag);
        connMap = tag.getLong("connMap");
    }

    @Override
    public void read(MCDataInput in, int key) {
        if (key == 31) {
            if (world().isRemote) {
                connMap = in.readLong();
            }
        } else {
            super.read(in, key);
        }
    }

    public void sendConnUpdate() {
        if (clientNeedsMap()) streamToSend(writeStream(31).writeLong(connMap)).sendToChunk();
    }

    //@Override
    public void onMaskChanged() {
        //super.onMaskChanged();
        sendConnUpdate();
    }

    @Override
    public void onNeighborChange(Block b) {
        super.onNeighborChange(b);
        if (!world().isRemote) if (updateExternals()) sendConnUpdate();
    }

    @Override
    public void onBlockPlaced(
            int side,
            int meta,
            EntityPlayer player,
            ItemStack stack,
            Vector3 hit
    ) {
        super.onBlockPlaced(side, meta, player, stack, hit);
        if (!world().isRemote) if (updateExternals()) sendConnUpdate();
    }

    @Override
    public void onBlockRemoval() {
        super.onBlockRemoval();
        WorldLib.bulkBlockUpdate(world(), xCoord, yCoord, zCoord, getBlock());
    }
}
