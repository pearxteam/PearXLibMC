package ru.pearx.libmc.common.networking.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;

/*
 * Created by mrAppleXZ on 24.09.17 21:57.
 */
public class CPacketOpenStructureCreationGui implements IMessage
{
    @Override
    public void fromBytes(ByteBuf buf)
    {

    }

    @Override
    public void toBytes(ByteBuf buf)
    {

    }

    public static class Handler implements IMessageHandler<CPacketOpenStructureCreationGui, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketOpenStructureCreationGui message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                PXLMC.PROXY.openStructureCreationGui();
            });
            return null;
        }
    }
}
