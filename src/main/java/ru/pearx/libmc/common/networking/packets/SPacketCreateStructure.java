package ru.pearx.libmc.common.networking.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.tuple.Pair;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.networking.ByteBufTools;
import ru.pearx.libmc.common.structure.StructureApi;
import ru.pearx.libmc.common.structure.processors.StructureProcessor;
import ru.pearx.libmc.common.structure.processors.StructureProcessorData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by mrAppleXZ on 25.09.17 20:04.
 */
public class SPacketCreateStructure implements IMessage
{
    public String name;
    public BlockPos fromPos;
    public BlockPos toPos;
    public List<Pair<ResourceLocation, StructureProcessorData>> processors;

    public SPacketCreateStructure()
    {
    }

    @SafeVarargs
    public SPacketCreateStructure(String name, BlockPos fromPos, BlockPos toPos, Pair<ResourceLocation, StructureProcessorData>... processors)
    {
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.name = name;
        this.processors = Arrays.asList(processors);
    }

    public SPacketCreateStructure(String name, BlockPos fromPos, BlockPos toPos, List<Pair<ResourceLocation, StructureProcessorData>> processors)
    {
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.name = name;
        this.processors = processors;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);
        fromPos = ByteBufTools.readBlockPos(buf);
        toPos = ByteBufTools.readBlockPos(buf);
        int count = buf.readInt();
        List<Pair<ResourceLocation, StructureProcessorData>> lst = new ArrayList<>();
        for(int i = 0; i < count; i++)
        {
            ResourceLocation loc = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
            BlockPos pos = ByteBufTools.readBlockPos(buf);
            NBTTagCompound tag = ByteBufUtils.readTag(buf);
            StructureProcessorData dat = StructureProcessor.REGISTRY.getValue(loc).loadData(tag, pos);
            lst.add(Pair.of(loc, dat));
        }
        processors = lst;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, name);
        ByteBufTools.writeBlockPos(buf, fromPos);
        ByteBufTools.writeBlockPos(buf, toPos);
        buf.writeInt(processors.size());
        for(Pair<ResourceLocation, StructureProcessorData> p : processors)
        {
            ByteBufUtils.writeUTF8String(buf, p.getLeft().toString());
            ByteBufTools.writeBlockPos(buf, p.getRight().getAbsolutePos());
            ByteBufUtils.writeTag(buf, p.getRight().serialize());
        }
    }

    public static class Handler implements IMessageHandler<SPacketCreateStructure, IMessage>
    {
        @Override
        public IMessage onMessage(SPacketCreateStructure message, MessageContext ctx)
        {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() ->
            {
                if(ctx.getServerHandler().player.canUseCommand(2, "structure"))
                {
                    StructureApi.INSTANCE.createStructure(message.name, message.fromPos, message.toPos, ctx.getServerHandler().player.getServerWorld(), message.processors);
                }
            });
            return null;
        }
    }
}
