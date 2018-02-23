package ru.pearx.libmc.common.tiles;

import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.structure.blockarray.BlockArray;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMasterDefault;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 16.11.17 19:29.
 */
public class TileMultiblockMaster extends TileSyncable implements IMultiblockMasterDefault
{
    private Rotation rot;
    private List<BlockPos> slaves;
    private ResourceLocation id;
    private boolean inactive = true;
    private Multiblock multiblock;

    private AxisAlignedBB renderBB;

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
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public void setId(ResourceLocation s)
    {
        this.id = s;
        this.multiblock = Multiblock.REGISTRY.getValue(s);
    }

    @Override
    public void setIdAndUpdate(ResourceLocation s)
    {
        setId(s);
        updateMultiblock();
    }

    @Override
    public boolean isInactive()
    {
        return inactive;
    }

    @Override
    public void setInactive(boolean val)
    {
        this.inactive = val;
    }

    @Override
    public Multiblock getMultiblock()
    {
        return multiblock;
    }

    @Override
    public void updateMultiblock()
    {
        BlockArray arr = getMultiblock().getStructure();
        BlockPos from = PXLMC.transformPos(new BlockPos(arr.getMinX(), arr.getMinY(), arr.getMinZ()).subtract(getMultiblock().getMasterPos()), null, getRotation()).add(getPos());
        BlockPos to = PXLMC.transformPos(new BlockPos(arr.getMaxX(), arr.getMaxY(), arr.getMaxZ()).subtract(getMultiblock().getMasterPos()), null, getRotation()).add(getPos());
        int minX = Math.min(from.getX(), to.getX());
        int minY = Math.min(from.getY(), to.getY());
        int minZ = Math.min(from.getZ(), to.getZ());
        int maxX = Math.max(from.getX(), to.getX());
        int maxY = Math.max(from.getY(), to.getY());
        int maxZ = Math.max(from.getZ(), to.getZ());
        renderBB = new AxisAlignedBB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
    }

    @Override
    public void readCustomData(NBTTagCompound tag)
    {
        if(tag.hasKey("rotation", Constants.NBT.TAG_INT))
            setRotation(Rotation.values()[tag.getInteger("rotation")]);
        if(tag.hasKey("slaves", Constants.NBT.TAG_LIST))
        {
            List<BlockPos> slaves = new ArrayList<>();
            NBTTagList lst = tag.getTagList("slaves", Constants.NBT.TAG_INT_ARRAY);
            for (NBTBase base : lst)
            {
                int[] arr = ((NBTTagIntArray) base).getIntArray();
                slaves.add(new BlockPos(arr[0] + getPos().getX(), arr[1] + getPos().getY(), arr[2] + getPos().getZ()));
            }
            setSlavesPositions(slaves);
        }
        if(tag.hasKey("multiblock_id", Constants.NBT.TAG_STRING))
        {
            setIdAndUpdate(new ResourceLocation(tag.getString("multiblock_id")));
        }
        if(tag.hasKey("inactive", Constants.NBT.TAG_BYTE))
            setInactive(tag.getBoolean("inactive"));
    }

    @Override
    public void writeCustomData(NBTTagCompound tag)
    {
        tag.setInteger("rotation", getRotation().ordinal());
        NBTTagList lst = new NBTTagList();
        for(BlockPos slave : getSlavesPositions())
        {
            lst.appendTag(new NBTTagIntArray(new int[]{slave.getX() - getPos().getX(), slave.getY() - getPos().getY(), slave.getZ() - getPos().getZ()}));
        }
        tag.setTag("slaves", lst);
        tag.setString("multiblock_id", getId().toString());
        tag.setBoolean("inactive", inactive);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return renderBB == null ? super.getRenderBoundingBox() : renderBB;
    }
}
