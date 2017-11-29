package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.structure.BlockPair;
import ru.pearx.libmc.common.structure.blockarray.BlockArrayEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 12.11.17 16:09.
 */
public interface IMultiblockMaster extends IMultiblockPart
{
    //todo store original blocks+tiles? maybe. i'm too beautiful butterfly now.
    Rotation getRotation();
    void setRotation(Rotation rot);
    List<BlockPos> getSlavesPositions();
    void setSlavesPositions(List<BlockPos> lst);
    ResourceLocation getId();
    void setId(ResourceLocation s);
    boolean isInactive();
    void setInactive(boolean val);


    default void postForm(@Nullable EntityPlayer p) {}
    default void unform()
    {
        setInactive(true);
        BlockPos.MutableBlockPos origin = new BlockPos.MutableBlockPos();
        List<BlockPos> lst = new ArrayList<>(getSlavesPositions());
        lst.add(getPos());
        Multiblock mb = Multiblock.REGISTRY.getValue(getId());
        Rotation rot = PXLMC.getIdentityRotation(getRotation());
        for(BlockPos pos : lst)
        {
            origin.setPos(pos.getX() - getPos().getX(), pos.getY() - getPos().getY(), pos.getZ() - getPos().getZ());
            PXLMC.transformPos(origin, null, rot);
            origin.setPos(origin.getX() + + mb.getMasterPos().getX(), origin.getY() + mb.getMasterPos().getY(), origin.getZ() + + mb.getMasterPos().getZ());
            BlockArrayEntry entr = mb.getStructure().getMap().get(origin);
            System.out.println(origin + " ");
            getWorld().setBlockState(pos, entr.getState().withRotation(getRotation()));
            if(entr.getTile() == null)
                getWorld().removeTileEntity(pos);
            else
            {
                TileEntity te = entr.getTile();
                te.rotate(getRotation());
                getWorld().setTileEntity(pos, te);
            }
        }
    }

    <T> T handleEvent(IMultiblockEvent<T> evt, IMultiblockPart part);
}
