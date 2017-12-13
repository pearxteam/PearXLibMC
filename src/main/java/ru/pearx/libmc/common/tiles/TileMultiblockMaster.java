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
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMaster;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMasterDefault;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;

import javax.swing.plaf.multi.MultiButtonUI;
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
    private boolean inactive;

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
    public void update()
    {
        Multiblock mb = Multiblock.REGISTRY.getValue(getId());
        BlockArray arr = mb.getStructure();
        BlockPos from = PXLMC.transformPos(new BlockPos(arr.getMinX(), arr.getMinY(), arr.getMinZ()).subtract(mb.getMasterPos()), null, getRotation()).add(getPos());
        BlockPos to = PXLMC.transformPos(new BlockPos(arr.getMaxX(), arr.getMaxY(), arr.getMaxZ()).subtract(mb.getMasterPos()), null, getRotation()).add(getPos());
        int minX = Math.min(from.getX(), to.getX());
        int minY = Math.min(from.getY(), to.getY());
        int minZ = Math.min(from.getZ(), to.getZ());
        int maxX = Math.max(from.getX(), to.getX());
        int maxY = Math.max(from.getY(), to.getY());
        int maxZ = Math.max(from.getZ(), to.getZ());
        renderBB = new AxisAlignedBB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
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
        compound.setString("multiblock_id", getId().toString());
        compound.setBoolean("inactive", inactive);
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
        setId(new ResourceLocation(compound.getString("multiblock_id")));
        setInactive(compound.getBoolean("inactive"));
        update();
    }

    public BlockPos getOriginalPos(BlockPos trans)
    {
        return PXLMC.transformPos(trans.subtract(getPos()), null, PXLMC.getIdentityRotation(rot));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return renderBB;
    }


}
