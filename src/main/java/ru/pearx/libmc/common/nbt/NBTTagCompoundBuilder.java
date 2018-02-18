package ru.pearx.libmc.common.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/*
 * Created by mrAppleXZ on 17.02.18 22:08.
 */
public class NBTTagCompoundBuilder
{
    private NBTTagCompound tag;

    public NBTTagCompoundBuilder()
    {
        this(new NBTTagCompound());
    }

    public NBTTagCompoundBuilder(NBTTagCompound tag)
    {
        this.tag = tag;
    }

    public NBTTagCompoundBuilder setBoolean(String key, boolean bool)
    {
        tag.setBoolean(key, bool);
        return this;
    }

    public NBTTagCompoundBuilder setInteger(String key, int i)
    {
        tag.setInteger(key, i);
        return this;
    }

    public NBTTagCompoundBuilder setByte(String key, byte b)
    {
        tag.setByte(key, b);
        return this;
    }

    public NBTTagCompoundBuilder setByteArray(String key, byte[] bts)
    {
        tag.setByteArray(key, bts);
        return this;
    }

    public NBTTagCompoundBuilder setDouble(String key, double d)
    {
        tag.setDouble(key, d);
        return this;
    }

    public NBTTagCompoundBuilder setFloat(String key, float f)
    {
        tag.setFloat(key, f);
        return this;
    }

    public NBTTagCompoundBuilder setIntArray(String key, int[] ints)
    {
        tag.setIntArray(key, ints);
        return this;
    }

    public NBTTagCompoundBuilder setLong(String key, long l)
    {
        tag.setLong(key, l);
        return this;
    }

    public NBTTagCompoundBuilder setShort(String key, short s)
    {
        tag.setShort(key, s);
        return this;
    }

    public NBTTagCompoundBuilder setString(String key, String s)
    {
        tag.setString(key, s);
        return this;
    }

    public NBTTagCompoundBuilder setTag(String key, NBTBase t)
    {
        tag.setTag(key, t);
        return this;
    }

    public NBTTagCompoundBuilder setUniqueId(String key, UUID uuid)
    {
        tag.setUniqueId(key, uuid);
        return this;
    }

    public NBTTagCompound build()
    {
        return tag;
    }
}
