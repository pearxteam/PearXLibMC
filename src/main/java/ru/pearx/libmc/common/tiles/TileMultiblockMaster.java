package ru.pearx.libmc.common.tiles;

import net.minecraft.nbt.*;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMaster;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMasterDefault;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 16.11.17 19:29.
 */
public class TileMultiblockMaster extends TileSyncable implements IMultiblockMasterDefault
{
    private Rotation rot;
    private List<BlockPos> slaves;

    @Override
    public Rotation getRotation()
    {
        return rot;
    }

    @Override
    public void setRotation(Rotation rot)
    {
        this.rot = rot;
    }

    @Override
    public List<BlockPos> getSlavesPositions()
    {
        return slaves;
    }

    @Override
    public void setSlavesPositions(List<BlockPos> lst)
    {
        this.slaves = lst;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("rotation", getRotation().ordinal());
        NBTTagList lst = new NBTTagList();
        for(BlockPos slave : getSlavesPositions())
        {
           lst.appendTag(new NBTTagIntArray(new int[]{slave.getX() - getPos().getX(), slave.getY() - getPos().getY(), slave.getZ() - getPos().getZ()}));
        }
        compound.setTag("slaves", lst);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        setRotation(Rotation.values()[compound.getInteger("rotation")]);
        List<BlockPos> slaves = new ArrayList<>();
        NBTTagList lst = compound.getTagList("slaves", Constants.NBT.TAG_INT_ARRAY);
        for(NBTBase base : lst)
        {
            int[] arr = ((NBTTagIntArray) base).getIntArray();
            slaves.add(new BlockPos(arr[0] + getPos().getX(), arr[1] + getPos().getY(), arr[2] + getPos().getZ()));
        }
        setSlavesPositions(slaves);
    }
}
