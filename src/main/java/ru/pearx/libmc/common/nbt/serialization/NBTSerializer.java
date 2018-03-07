package ru.pearx.libmc.common.nbt.serialization;

import net.minecraft.nbt.NBTTagCompound;
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

    private static <T> void read(Consumer<T> reader, Class<T> clazz, String name, NBTTagCompound tag)
    {
        reader.accept(NBTConverter.convertFrom(clazz, tag.getTag(name)));
    }

    private static <T> void write(Supplier<T> writer, Class<T> clazz, String name, NBTTagCompound tag)
    {
        tag.setTag(name, NBTConverter.convertTo(clazz, writer.get()));
    }

    private static boolean shouldWrite(WriteTarget[] targets, WriteTarget target)
    {
        return (targets.length == 0 || PXL.arrayContains(targets, target));
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
        public boolean shouldWrite(NBTTagCompound tag, WriteTarget target)
        {
            return false;
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

        @Override
        public void write(NBTTagCompound tag)
        {

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
        public boolean shouldRead(NBTTagCompound tag)
        {
            return false;
        }

        @Override
        public boolean shouldWrite(NBTTagCompound tag, WriteTarget target)
        {
            return NBTSerializer.shouldWrite(targets, target);
        }

        @Override
        public void read(NBTTagCompound tag)
        {

        }

        @Override
        public void write(NBTTagCompound tag)
        {
            NBTSerializer.write(writer, clazz, name, tag);
        }
    }

    public static class ReaderWriter<T> implements INBTSerializer.Reader, INBTSerializer.Writer
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
            return NBTSerializer.shouldWrite(targets, target);
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
