package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.pearx.libmc.common.items.PXLItems;
import ru.pearx.libmc.common.structure.multiblock.events.*;

/*
 * Created by mrAppleXZ on 13.11.17 19:06.
 */

/**
 * An {@link IMultiblockMaster} with default event handlers.
 */
public interface IMultiblockMasterDefault extends IMultiblockMaster
{
    @Override
    default <T>T handleEvent(IMultiblockEvent<T> evt, IMultiblockPart part)
    {
        if(!isInactive())
        {
            switch (evt.getId())
            {
                case MultiblockBreakEvent.ID:
                {
                    handleBreak((MultiblockBreakEvent) evt, part);
                    if (!part.getWorld().isRemote)
                        unform();
                    break;
                }
                case MultiblockActivatedEvent.ID:
                    return evt.cast(handleActivated((MultiblockActivatedEvent) evt, part));
                case MultiblockCapabilityEvent.Has.ID:
                    return evt.cast(hasCapability((MultiblockCapabilityEvent.Has<?>) evt, part));
                case MultiblockCapabilityEvent.Get.ID:
                    return evt.cast(getCapability((MultiblockCapabilityEvent.Get<T>) evt, part));
                case MultiblockPickBlockEvent.ID:
                    return evt.cast(getPickBlock((MultiblockPickBlockEvent) evt, part));
                case MultiblockGetFaceShapeEvent.ID:
                    return evt.cast(getFaceShape((MultiblockGetFaceShapeEvent) evt, part));
            }
        }
        return null;
    }

    default void handleBreak(MultiblockBreakEvent evt, IMultiblockPart part) {}
    default boolean handleActivated(MultiblockActivatedEvent evt, IMultiblockPart part) {return false;}
    default boolean hasCapability(MultiblockCapabilityEvent.Has<?> evt, IMultiblockPart part) {return false;}
    default <T>T getCapability(MultiblockCapabilityEvent.Get<T> evt, IMultiblockPart part) {return null;}
    default BlockFaceShape getFaceShape(MultiblockGetFaceShapeEvent evt, IMultiblockPart part) {return null;}

    default ItemStack getPickBlock(MultiblockPickBlockEvent evt, IMultiblockPart part)
    {
        ItemStack st = new ItemStack(PXLItems.multiblock);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("multiblock", getId().toString());
        st.setTagCompound(tag);
        return st;
    }
}
