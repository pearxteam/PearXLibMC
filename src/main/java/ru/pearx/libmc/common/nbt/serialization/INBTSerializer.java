package ru.pearx.libmc.common.nbt.serialization;

import net.minecraft.nbt.NBTTagCompound;

/*
 * Created by mrAppleXZ on 03.03.18 14:24.
 */
public interface INBTSerializer
{
    String getName();

    boolean shouldRead(NBTTagCompound tag);
    boolean shouldWrite(NBTTagCompound tag);

    void read(NBTTagCompound tag);
    void write(NBTTagCompound tag);

    interface Reader extends INBTSerializer
    {
        int getId();

        @Override
        default boolean shouldRead(NBTTagCompound tag)
        {
            return tag.hasKey(getName(), getId());
        }

        @Override
        default boolean shouldWrite(NBTTagCompound tag)
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
        default boolean shouldWrite(NBTTagCompound tag)
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
        default boolean shouldWrite(NBTTagCompound tag)
        {
            return Writer.super.shouldWrite(tag);
        }

        @Override
        void write(NBTTagCompound tag);

        @Override
        void read(NBTTagCompound tag);
    }
}
