package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.structure.blockarray.BlockArrayEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 12.11.17 16:09.
 */

/**
 * A multiblock master part. Handles all the events, unforms the multiblock, contains all the properties, etc...
 */
public interface IMultiblockMaster extends IMultiblockPart
{
    /**
     * Gets the multiblock's rotation.
     */
    Rotation getRotation();

    /**
     * Sets the multiblock's rotation.
     */
    void setRotation(Rotation rot);

    /**
     * Gets the slave blocks positions.
     */
    List<BlockPos> getSlavesPositions();

    /**
     * Sets the slave blocks positions.
     */
    void setSlavesPositions(List<BlockPos> lst);

    /**
     * Gets the multiblock's ID.
     */
    ResourceLocation getId();

    /**
     * Sets the multiblock's ID.
     */
    void setId(ResourceLocation s);

    /**
     * Returns true if the multiblock is inactive and will be unformed as soon as possible.
     */
    boolean isInactive();

    /**
     * Sets the multiblock inactivity value.
     * @param val Set to true if the multiblock is inactive and will be removed as soon as possible.
     */
    void setInactive(boolean val);

    /**
     * Updates the multiblock. Called when the multiblock is formed and loaded from NBT.
     */
    void updateMultiblock();


    /**
     * An event that occurs when the multiblock is formed, but not updated for the first time.
     * @param p The player that formed the multiblock.
     */
    default void postForm(@Nullable EntityPlayer p) {}

    /**
     * Unforms the multiblock. Normally should be called when the {@link ru.pearx.libmc.common.structure.multiblock.events.MultiblockBreakEvent} received.
     */
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

    /**
     * Gets the master part.
     * @return Itself.
     */
    @Override
    default IMultiblockMaster getMaster()
    {
        return this;
    }

    /**
     * Gets the original pos in the structure from a position in the world.
     * @param trans Position in the world.
     * @return Original position in the structure
     */
    default BlockPos getOriginalPos(BlockPos trans)
    {
        return PXLMC.transformPos(trans.subtract(getPos()), null, PXLMC.getIdentityRotation(getRotation()));
    }

    /**
     * Gets the original {@link BlockArrayEntry} from a position in the world.
     * @param trans Position in the world/
     * @return Original {@link BlockArrayEntry} in the structure.
     */
    default BlockArrayEntry getOriginalEntry(BlockPos trans)
    {
        //todo cache the multiblock?
        return Multiblock.REGISTRY.getValue(getId()).getStructure().getMap().get(getOriginalPos(trans));
    }

    /**
     * Handles a multiblock event sent by one of the multiblock's parts.
     * @see IMultiblockEvent
     * @param evt An event.
     * @param part A part that sent an event.
     * @param <T> Return type of the event.
     * @return Event result.
     */
    <T> T handleEvent(IMultiblockEvent<T> evt, IMultiblockPart part);
}
