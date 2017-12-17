package ru.pearx.libmc.common.structure.multiblock.events;

import net.minecraft.block.state.IBlockState;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;

/*
 * Created by mrAppleXZ on 13.11.17 19:09.
 */
public class MultiblockBreakEvent implements IMultiblockEvent<Void>
{
    public static final String ID = "break";
    private IBlockState state;

    public MultiblockBreakEvent(IBlockState state)
    {
        this.state = state;
    }
    public IBlockState getState()
    {
        return state;
    }

    @Override
    public String getId()
    {
        return ID;
    }
}
