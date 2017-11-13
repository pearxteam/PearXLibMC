package ru.pearx.libmc.common.structure.multiblock;

import ru.pearx.libmc.common.structure.multiblock.events.MultiblockActivatedEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockBreakEvent;

/*
 * Created by mrAppleXZ on 13.11.17 19:06.
 */
public interface IMultiblockMasterDefault extends IMultiblockMaster
{
    @Override
    default void handleEvent(IMultiblockEvent evt)
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
                handleActivated((MultiblockActivatedEvent) evt);
                break;
            }
        }
    }

    default void handleBreak(MultiblockBreakEvent evt) {}
    default void handleActivated(MultiblockActivatedEvent evt) {}
}
