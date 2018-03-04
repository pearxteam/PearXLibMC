package ru.pearx.libmc.common.nbt.serialization.conversion;

import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/*
 * Created by mrAppleXZ on 03.03.18 18:12.
 */
public final class NBTConverter
{
    private static List<INBTAdapter> adapters = new ArrayList<>();
    static
    {
        //todo add other adapters
        adapters.add(new NBTAdapter<>(NBTTagCompound.class, NBTTagCompound.class, Constants.NBT.TAG_COMPOUND, Function.identity(), Function.identity()));
        adapters.add(new NBTAdapter<>(byte[].class, NBTTagByteArray.class, Constants.NBT.TAG_BYTE_ARRAY, NBTTagByteArray::new, NBTTagByteArray::getByteArray));
        adapters.add(new NBTAdapter<>(NBTTagList.class, NBTTagList.class, Constants.NBT.TAG_LIST, Function.identity(), Function.identity()));
        adapters.add(new NBTAdapter<>(String.class, NBTTagString.class, Constants.NBT.TAG_STRING, NBTTagString::new, NBTTagString::getString));
        adapters.add(new NBTAdapter<>(boolean.class, NBTTagByte.class, Constants.NBT.TAG_BYTE, (b) -> new NBTTagByte((byte)(b ? 1 : 0)), (tb) -> tb.getByte() == 1));
        adapters.add(new NBTAdapter<>(int.class, NBTTagInt.class, Constants.NBT.TAG_INT, NBTTagInt::new, NBTTagInt::getInt));
        adapters.add(new NBTAdapter<>(ResourceLocation.class, NBTTagString.class, Constants.NBT.TAG_STRING, (rl) -> new NBTTagString(rl.toString()), (t) -> new ResourceLocation(t.getString())));
        adapters.add(new NBTAdapter<>(BlockPos.class, NBTTagIntArray.class, Constants.NBT.TAG_INT_ARRAY,
                (pos) -> new NBTTagIntArray(new int[] { pos.getX(), pos.getY(), pos.getZ() }),
                (lst) ->
                {
                    int[] arr = lst.getIntArray();
                    return new BlockPos(arr[0], arr[1], arr[2]);
                }));
    }

    public static List<INBTAdapter> getAdapters()
    {
        return adapters;
    }

    public static int getId(Class clazz)
    {
        for(INBTAdapter a : getAdapters())
        {
            if(a.getType().isAssignableFrom(clazz))
                return a.getNbtId();
        }
        throw new AdapterNotFoundException(clazz);
    }

    public static NBTBase convertTo(Class clazz, Object value)
    {
        if(value instanceof NBTBase)
            return (NBTBase) value;

        for(INBTAdapter a : getAdapters())
        {
            if(a.getType().isAssignableFrom(clazz))
                return a.convertTo(a.castType(value));
        }
        throw new AdapterNotFoundException(value.getClass());
    }

    public static <T, N extends NBTBase> T convertFrom(Class<T> to, N value)
    {
        for(INBTAdapter a : getAdapters())
        {
            if(a.getType().isAssignableFrom(to))
                return (T)a.convertFrom(a.castNbt(value));
        }
        throw new AdapterNotFoundException(to);
    }
}
