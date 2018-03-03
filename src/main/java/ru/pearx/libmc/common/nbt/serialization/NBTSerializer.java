package ru.pearx.libmc.common.nbt.serialization;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import ru.pearx.libmc.common.nbt.serialization.conversion.NBTConverter;

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

    private static <T> void read(Consumer<T> reader, Class<T> clazz, String name, NBTTagCompound tag)
    {
        reader.accept(NBTConverter.convertFrom(clazz, tag.getTag(name)));
    }

    private static <T> void write(Supplier<T> writer, String name, NBTTagCompound tag)
    {
        tag.setTag(name, NBTConverter.convertTo(writer.get()));
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

    public static class Writer<T extends NBTBase> implements INBTSerializer.Writer
    {
        private String name;
        private Supplier<T> writer;

        public Writer(String name, Supplier<T> writer)
        {
            this.name = name;
            this.writer = writer;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public void write(NBTTagCompound tag)
        {
            NBTSerializer.write(writer, name, tag);
        }
    }

    public static class ReaderWriter<T> implements INBTSerializer.ReaderWriter
    {
        private String name;
        private Class<T> clazz;
        private Consumer<T> reader;
        private Supplier<T> writer;

        public ReaderWriter(String name, Class<T> clazz, Consumer<T> reader, Supplier<T> writer)
        {
            this.name = name;
            this.reader = reader;
            this.writer = writer;
            this.clazz = clazz;
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
        public void write(NBTTagCompound tag)
        {
            NBTSerializer.write(writer, name, tag);
        }

        @Override
        public void read(NBTTagCompound tag)
        {
            NBTSerializer.read(reader, clazz, name, tag);
        }
    }
}
