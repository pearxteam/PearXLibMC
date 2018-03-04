package ru.pearx.libmc.common.nbt.serialization;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import ru.pearx.lib.PXL;
import ru.pearx.libmc.common.nbt.serialization.conversion.NBTConverter;
import ru.pearx.libmc.common.tiles.syncable.WriteTarget;

import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Created by mrAppleXZ on 03.03.18 14:33.
 */
public final class NBTSerializer
{
    private NBTSerializer()
    {
    }

    public static NBTTagCompound createNullTag()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("null", (byte)0);
        return tag;
    }

    public static boolean hasNullTag(String name, NBTTagCompound tag)
    {
        return tag.hasKey(name, Constants.NBT.TAG_COMPOUND) && (tag.getCompoundTag(name).hasKey("null", Constants.NBT.TAG_BYTE));
    }

    private static <T> void read(Consumer<T> reader, Class<T> clazz, String name, NBTTagCompound tag)
    {
        if(hasNullTag(name, tag))
            reader.accept(null);
        else
            reader.accept(NBTConverter.convertFrom(clazz, tag.getTag(name)));
    }

    private static <T> void write(Supplier<T> writer, Class<T> clazz, String name, NBTTagCompound tag)
    {
        NBTBase t = NBTConverter.convertTo(clazz, writer.get());
        if(t == null)
            tag.setTag(name, createNullTag());
        else
            tag.setTag(name, t);
    }

    public static class Reader<T> implements INBTSerializer.Reader
    {
        private String name;
        private Class<T> clazz;
        private Consumer<T> reader;

        public Reader(String name, Class<T> clazz, Consumer<T> reader)
        {
            this.name = name;
            this.clazz = clazz;
            this.reader = reader;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public int getId()
        {
            return NBTConverter.getId(clazz);
        }

        @Override
        public void read(NBTTagCompound tag)
        {
            NBTSerializer.read(reader, clazz, name, tag);
        }
    }

    public static class Writer<T> implements INBTSerializer.Writer
    {
        private String name;
        private Class<T> clazz;
        private Supplier<T> writer;
        private WriteTarget[] targets;

        public Writer(String name, Class<T> clazz, Supplier<T> writer, WriteTarget... targets)
        {
            this.name = name;
            this.clazz = clazz;
            this.writer = writer;
            this.targets = targets;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public boolean shouldWrite(NBTTagCompound tag, WriteTarget target)
        {
            return INBTSerializer.Writer.super.shouldWrite(tag, target) && (targets.length == 0 || PXL.arrayContains(targets, target));
        }

        @Override
        public void write(NBTTagCompound tag)
        {
            NBTSerializer.write(writer, clazz, name, tag);
        }
    }

    public static class ReaderWriter<T> implements INBTSerializer.ReaderWriter
    {
        private String name;
        private Class<T> clazz;
        private Consumer<T> reader;
        private Supplier<T> writer;
        private WriteTarget[] targets;

        public ReaderWriter(String name, Class<T> clazz, Consumer<T> reader, Supplier<T> writer, WriteTarget... targets)
        {
            this.name = name;
            this.reader = reader;
            this.writer = writer;
            this.clazz = clazz;
            this.targets = targets;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public int getId()
        {
            return NBTConverter.getId(clazz);
        }

        @Override
        public boolean shouldWrite(NBTTagCompound tag, WriteTarget target)
        {
            return INBTSerializer.ReaderWriter.super.shouldWrite(tag, target) && (targets.length == 0 || PXL.arrayContains(targets, target));
        }

        @Override
        public void write(NBTTagCompound tag)
        {
            NBTSerializer.write(writer, clazz, name, tag);
        }

        @Override
        public void read(NBTTagCompound tag)
        {
            NBTSerializer.read(reader, clazz, name, tag);
        }
    }
}
