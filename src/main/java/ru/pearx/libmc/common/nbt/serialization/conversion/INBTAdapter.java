package ru.pearx.libmc.common.nbt.serialization.conversion;

import net.minecraft.nbt.NBTBase;

/*
 * Created by mrAppleXZ on 03.03.18 18:41.
 */
public interface INBTAdapter<T, N extends NBTBase>
{
    Class<T> getType();
    int getNbtId(Class<? extends T> clazzToConvert);
    N convertTo(T o);
    T convertFrom(Class<? extends T> clazzToConvert, N n);
}
