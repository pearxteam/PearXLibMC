package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.structure.blockarray.BlockArrayEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 12.11.17 16:09.
 */
public interface IMultiblockMaster extends IMultiblockPart
{
    Rotation getRotation();
    void setRotation(Rotation rot);
    List<BlockPos> getSlavesPositions();
    void setSlavesPositions(List<BlockPos> lst);
    ResourceLocation getId();
    void setId(ResourceLocation s);

    default void postForm(@Nullable EntityPlayer p) {}
    default void unform()
    {
        BlockPos.MutableBlockPos origin = new BlockPos.MutableBlockPos();
        List<BlockPos> lst = new ArrayList<>(getSlavesPositions());
        lst.add(getPos());
        Multiblock mb = Multiblock.REGISTRY.getValue(getId());
        for(BlockPos pos : lst)
        {
            origin.setPos(pos.getX() - getPos().getX() + mb.getMasterPos().getX(), pos.getY() - getPos().getY() + mb.getMasterPos().getY(), pos.getZ() - getPos().getZ() + mb.getMasterPos().getZ());
            PXLMC.transformPos(origin, null, PXLMC.getIdentityRotation(getRotation()));
            BlockArrayEntry entr = mb.getStructure().getMap().get(origin);
            getWorld().setBlockState(pos, entr.getState());
            if(entr.getTile() == null)
                getWorld().removeTileEntity(pos);
            else
                getWorld().setTileEntity(pos, entr.getTile());
        }
    }

    <T> T handleEvent(IMultiblockEvent<T> evt, IMultiblockPart part);
}
