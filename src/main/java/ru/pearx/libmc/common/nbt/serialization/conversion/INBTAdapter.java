package ru.pearx.libmc.common.nbt.serialization.conversion;

import net.minecraft.nbt.NBTBase;

/*
 * Created by mrAppleXZ on 03.03.18 18:41.
 */
public interface INBTAdapter<T, N extends NBTBase>
{
    Class<T> getType();
    int getNbtId();
    N convertTo(T o);
    T convertFrom(N n);

    default T castType(Object o)
    {
        return (T)o;
    }

    default N castNbt(NBTBase b)
    {
        return (N)b;
    }
}
