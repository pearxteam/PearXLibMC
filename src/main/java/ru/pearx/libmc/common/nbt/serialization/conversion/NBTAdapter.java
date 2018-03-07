package ru.pearx.libmc.common.nbt.serialization.conversion;

import net.minecraft.nbt.NBTBase;

import java.util.function.BiFunction;
import java.util.function.Function;

/*
 * Created by mrAppleXZ on 06.03.18 21:56.
 */
public class NBTAdapter<T, N extends NBTBase> implements INBTAdapter<T, N>
{
    private Class<T> type;
    private Function<T, N> converterTo;
    private BiFunction<Class<? extends T>, N, T> converterFrom;
    private Function<Class<? extends T>, Integer> idGetter;

    public NBTAdapter(Class<T> type, Class<N> nbtClass, Function<Class<? extends T>, Integer> idGetter, Function<T, N> converterTo, BiFunction<Class<? extends T>, N, T> converterFrom)
    {
        this.type = type;
        this.idGetter = idGetter;
        this.converterFrom = converterFrom;
        this.converterTo = converterTo;
    }

    public NBTAdapter(Class<T> type, Class<N> nbtClass, int id, Function<T, N> converterTo, BiFunction<Class<? extends T>, N, T> converterFrom)
    {
        this.type = type;
        this.idGetter = (clazz) -> id;
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
        if(o == null)
            return null;
        return converterTo.apply(o);
    }

    @Override
    public T convertFrom(Class<? extends T> clazzToConvert, N n)
    {
        return converterFrom.apply(clazzToConvert, n);
    }

    @Override
    public int getNbtId(Class<? extends T> clazzToConvert)
    {
        return idGetter.apply(clazzToConvert);
    }
}
