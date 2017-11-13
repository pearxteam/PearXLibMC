package ru.pearx.libmc.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockSlave;

/*
 * Created by mrAppleXZ on 13.11.17 18:24.
 */
public class TileMultiblockSlave extends TileSyncable implements IMultiblockSlave
{
    private BlockPos absMasterPos;

    @Override
    public BlockPos getMasterPos()
    {
        return absMasterPos;
    }

    public void setMasterPos(BlockPos pos)
    {
        absMasterPos = pos;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        BlockPos rel = getMasterPos().subtract(getPos());
        compound.setIntArray("masterPos", new int[] {rel.getX(), rel.getY(), rel.getZ()});
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        int[] ints = compound.getIntArray("masterPos");
        setMasterPos(new BlockPos(getPos().getX() + ints[0], getPos().getY() + ints[1], getPos().getZ() + ints[2]));
    }
}
