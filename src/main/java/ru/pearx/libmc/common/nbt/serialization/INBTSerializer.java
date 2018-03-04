package ru.pearx.libmc.common.nbt.serialization;

import net.minecraft.nbt.NBTTagCompound;
import ru.pearx.libmc.common.tiles.syncable.WriteTarget;

/*
 * Created by mrAppleXZ on 03.03.18 14:24.
 */
public interface INBTSerializer
{
    String getName();

    boolean shouldRead(NBTTagCompound tag);
    boolean shouldWrite(NBTTagCompound tag, WriteTarget target);

    void read(NBTTagCompound tag);
    void write(NBTTagCompound tag);

    interface Reader extends INBTSerializer
    {
        int getId();

        @Override
        default boolean shouldRead(NBTTagCompound tag)
        {
            return tag.hasKey(getName(), getId()) || NBTSerializer.hasNullTag(getName(), tag);
        }

        @Override
        default boolean shouldWrite(NBTTagCompound tag, WriteTarget target)
        {
            return false;
        }

        @Override
        default void write(NBTTagCompound tag) { }
    }

    interface Writer extends INBTSerializer
    {
        @Override
        default boolean shouldRead(NBTTagCompound tag)
        {
            return false;
        }

        @Override
        default boolean shouldWrite(NBTTagCompound tag, WriteTarget target)
        {
            return true;
        }

        @Override
        default void read(NBTTagCompound tag) { }
    }

    interface ReaderWriter extends Reader, Writer
    {
        @Override
        default boolean shouldRead(NBTTagCompound tag)
        {
            return Reader.super.shouldRead(tag);
        }

        @Override
        default boolean shouldWrite(NBTTagCompound tag, WriteTarget target)
        {
            return Writer.super.shouldWrite(tag, target);
        }

        @Override
        void write(NBTTagCompound tag);

        @Override
        void read(NBTTagCompound tag);
    }
}
