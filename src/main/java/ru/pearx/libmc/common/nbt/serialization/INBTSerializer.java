package ru.pearx.libmc.common.nbt.serialization;

import net.minecraft.nbt.NBTTagCompound;
import ru.pearx.libmc.common.nbt.serialization.conversion.NBTConverter;
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
            return tag.hasKey(getName(), getId()) || NBTConverter.hasNullTag(getName(), tag);
        }
    }

    interface Writer extends INBTSerializer
    {

    }
}
