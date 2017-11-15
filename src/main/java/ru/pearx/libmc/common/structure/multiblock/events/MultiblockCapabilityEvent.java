package ru.pearx.libmc.common.structure.multiblock.events;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 14.11.17 15:11.
 */
public abstract class MultiblockCapabilityEvent<T>
{
    private Capability<T> capability;
    @Nullable
    private EnumFacing facing;

    public MultiblockCapabilityEvent(Capability<T> capability, @Nullable EnumFacing facing)
    {
        this.capability = capability;
        this.facing = facing;
    }

    public Capability<T> getCapability()
    {
        return capability;
    }

    @Nullable
    public EnumFacing getFacing()
    {
        return facing;
    }

    public static class Has<T> extends MultiblockCapabilityEvent<T> implements IMultiblockEvent<Boolean>
    {
        public static final String ID = "capability_has";
        public Has(Capability<T> capability, @Nullable EnumFacing facing)
        {
            super(capability, facing);
        }

        @Override
        public String getId()
        {
            return ID;
        }
    }

    public static class Get<T> extends MultiblockCapabilityEvent<T> implements IMultiblockEvent<T>
    {
        public static final String ID = "capability_get";

        public Get(Capability<T> capability, @Nullable EnumFacing facing)
        {
            super(capability, facing);
        }

        @Override
        public String getId()
        {
            return ID;
        }
    }
}
