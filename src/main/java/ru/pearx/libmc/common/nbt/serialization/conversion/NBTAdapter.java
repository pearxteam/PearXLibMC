package ru.pearx.libmc.common.nbt.serialization.conversion;

import net.minecraft.nbt.NBTBase;

import java.util.function.Function;

/*
 * Created by mrAppleXZ on 03.03.18 19:14.
 */
public class NBTAdapter<T, N extends NBTBase> implements INBTAdapter<T, N>
{
    private Class<T> type;
    private Function<T, N> converterTo;
    private Function<N, T> converterFrom;
    private int nbtId;

    public NBTAdapter(Class<T> type, Class<N> nbtClass, int nbtId, Function<T, N> converterTo, Function<N, T> converterFrom)
    {
        this.type = type;
        this.nbtId = nbtId;
        this.converterFrom = converterFrom;
        this.converterTo = converterTo;
    }

    @Override
    public Class<T> getType()
    {
        return type;
    }

    @Override
    public N convertTo(T o)
    {
        return converterTo.apply(o);
    }

    @Override
    public T convertFrom(N n)
    {
        return converterFrom.apply(n);
    }

    @Override
    public int getNbtId()
    {
        return nbtId;
    }
}
