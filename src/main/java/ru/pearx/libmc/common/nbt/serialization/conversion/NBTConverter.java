package ru.pearx.libmc.common.nbt.serialization.conversion;

import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import ru.pearx.libmc.common.nbt.NBTTagCompoundBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/*
 * Created by mrAppleXZ on 03.03.18 18:12.
 */
public final class NBTConverter
{
    private NBTConverter() {}

    private static final NBTTagCompound NULL_TAG = new NBTTagCompoundBuilder().setByte("null", (byte)0).build();
    private static final List<INBTAdapter> ADAPTERS = new ArrayList<>();
    static
    {
        //todo add other adapters
        ADAPTERS.add(new NBTAdapterSimple<>(NBTTagCompound.class, NBTTagCompound.class, Constants.NBT.TAG_COMPOUND, Function.identity(), Function.identity()));
        ADAPTERS.add(new NBTAdapterSimple<>(byte[].class, NBTTagByteArray.class, Constants.NBT.TAG_BYTE_ARRAY, NBTTagByteArray::new, NBTTagByteArray::getByteArray));
        ADAPTERS.add(new NBTAdapterSimple<>(NBTTagList.class, NBTTagList.class, Constants.NBT.TAG_LIST, Function.identity(), Function.identity()));
        ADAPTERS.add(new NBTAdapterSimple<>(String.class, NBTTagString.class, Constants.NBT.TAG_STRING, NBTTagString::new, NBTTagString::getString));
        ADAPTERS.add(new NBTAdapterSimple<>(boolean.class, NBTTagByte.class, Constants.NBT.TAG_BYTE, (b) -> new NBTTagByte((byte)(b ? 1 : 0)), (tb) -> tb.getByte() == 1));
        ADAPTERS.add(new NBTAdapterSimple<>(int.class, NBTTagInt.class, Constants.NBT.TAG_INT, NBTTagInt::new, NBTTagInt::getInt));
        ADAPTERS.add(new NBTAdapterSimple<>(ResourceLocation.class, NBTTagString.class, Constants.NBT.TAG_STRING, (rl) -> new NBTTagString(rl.toString()), (t) -> new ResourceLocation(t.getString())));
        ADAPTERS.add(new NBTAdapterSimple<>(BlockPos.class, NBTTagIntArray.class, Constants.NBT.TAG_INT_ARRAY,
                (pos) -> new NBTTagIntArray(new int[] { pos.getX(), pos.getY(), pos.getZ() }),
                (lst) ->
                {
                    int[] arr = lst.getIntArray();
                    return new BlockPos(arr[0], arr[1], arr[2]);
                }));
        ADAPTERS.add(new NBTAdapter<>(Enum.class, NBTTagInt.class, Constants.NBT.TAG_INT, (en) -> new NBTTagInt(en.ordinal()), (clazz, tag) -> clazz.getEnumConstants()[tag.getInt()]));
    }

    public static List<INBTAdapter> getAdapters()
    {
        return ADAPTERS;
    }

    public static NBTTagCompound createNullTag()
    {
        return NULL_TAG.copy();
    }

    public static boolean hasNullTag(String name, NBTTagCompound tag)
    {
        return tag.hasKey(name, Constants.NBT.TAG_COMPOUND) && (tag.getCompoundTag(name).hasKey("null", Constants.NBT.TAG_BYTE));
    }

    public static boolean isNullTag(NBTBase nullTag)
    {
        return nullTag instanceof NBTTagCompound && ((NBTTagCompound)nullTag).hasKey("null", Constants.NBT.TAG_BYTE);
    }

    public <T> INBTAdapter<? super T, NBTBase> getAdapter(Class<? extends T> clazz)
    {
        for(INBTAdapter<? super T, NBTBase> ad : getAdapters())
        {
            if(ad.getType().isAssignableFrom(clazz))
            {
                return ad;
            }
        }
        throw new AdapterNotFoundException(clazz);
    }

    public static <T> int getId(Class<? extends T> clazz)
    {
        for(INBTAdapter<? super T, NBTBase> a : getAdapters())
        {
            if(a.getType().isAssignableFrom(clazz))
                return a.getNbtId(clazz);
        }
        throw new AdapterNotFoundException(clazz);
    }

    public static <T> NBTBase convertTo(Class<T> clazz, T value)
    {
        if(value == null)
            return createNullTag();
        if(value instanceof NBTBase)
            return (NBTBase) value;

        for(INBTAdapter<? super T, NBTBase> a : getAdapters())
        {
            if(a.getType().isAssignableFrom(clazz))
                return a.convertTo(value);
        }
        throw new AdapterNotFoundException(clazz);
    }

    public static <T, N extends NBTBase> T convertFrom(Class<T> to, N value)
    {
        if(isNullTag(value))
            return null;
        for(INBTAdapter<? super T, NBTBase> a : getAdapters())
        {
            if(a.getType().isAssignableFrom(to))
            {
                return (T) a.convertFrom(to, value);
            }
        }
        throw new AdapterNotFoundException(to);
    }
}
