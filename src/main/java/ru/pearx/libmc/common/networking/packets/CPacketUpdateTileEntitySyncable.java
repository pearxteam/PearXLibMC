package ru.pearx.libmc.common.networking.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.common.networking.ByteBufTools;
import ru.pearx.libmc.common.tiles.syncable.TileSyncable;

/*
 * Created by mrAppleXZ on 17.02.18 21:42.
 */
public class CPacketUpdateTileEntitySyncable implements IMessage
{
    private BlockPos pos;
    private NBTTagCompound tag;

    public CPacketUpdateTileEntitySyncable()
    {
    }

    public CPacketUpdateTileEntitySyncable(BlockPos pos, NBTTagCompound tag)
    {
        this.pos = pos;
        this.tag = tag;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = ByteBufTools.readBlockPos(buf);
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufTools.writeBlockPos(buf, pos);
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class Handler implements IMessageHandler<CPacketUpdateTileEntitySyncable, IMessage>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(CPacketUpdateTileEntitySyncable message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
               TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
               if(te instanceof TileSyncable)
                   ((TileSyncable) te).readCustomData(message.tag);
            });
            return null;
        }
    }
}
