package ru.pearx.libmc.common.structure.multiblock;

import net.minecraftforge.common.capabilities.Capability;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockActivatedEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockBreakEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockCapabilityEvent;

/*
 * Created by mrAppleXZ on 13.11.17 19:06.
 */
public interface IMultiblockMasterDefault extends IMultiblockMaster
{
    @Override
    default <T>T handleEvent(IMultiblockEvent<T> evt, IMultiblockSlave slave)
    {
        switch (evt.getId())
        {
            case MultiblockBreakEvent.ID:
            {
                handleBreak((MultiblockBreakEvent) evt);
                break;
            }
            case MultiblockActivatedEvent.ID:
            {
                return evt.cast(handleActivated((MultiblockActivatedEvent)evt));
            }
            case MultiblockCapabilityEvent.Has.ID:
            {
                return evt.cast(hasCapability((MultiblockCapabilityEvent.Has<?>) evt));
            }
            case MultiblockCapabilityEvent.Get.ID:
            {
                return evt.cast(getCapability((MultiblockCapabilityEvent.Get<T>) evt));
            }
        }
        return null;
    }

    default void handleBreak(MultiblockBreakEvent evt) {}
    default boolean handleActivated(MultiblockActivatedEvent evt) {return false;}
    default boolean hasCapability(MultiblockCapabilityEvent.Has<?> evt) {return false;}
    default <T>T getCapability(MultiblockCapabilityEvent.Get<T> evt) {return null;}
}
