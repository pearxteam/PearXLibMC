package ru.pearx.libmc.common.tiles.syncable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.networking.packets.CPacketUpdateTileEntitySyncable;

import javax.annotation.Nullable;

/**
 * Created by mrAppleXZ on 11.04.17 20:28.
 */
public abstract class TileSyncable extends TileEntity
{
    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.writeToNBT(new NBTTagCompound());
        writeCustomData(tag, WriteTarget.FULL_UPDATE);
        return tag;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        readCustomData(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        writeCustomData(compound, WriteTarget.SAVE);
        return compound;
    }

    public abstract void readCustomData(NBTTagCompound tag);

    public abstract void writeCustomData(NBTTagCompound tag, WriteTarget target, String... data);

    public NBTTagCompound writeCustomData(WriteTarget target, String... data)
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeCustomData(tag, target, data);
        return tag;
    }

    public void sendUpdates(EntityPlayer player, NBTTagCompound tag)
    {
        if(getWorld() instanceof WorldServer)
        {
            PlayerChunkMapEntry entr = ((WorldServer) getWorld()).getPlayerChunkMap().getEntry(getPos().getX() >> 4, getPos().getZ() >> 4);
            if(entr != null)
            {
                for (EntityPlayerMP p : entr.players)
                {
                    if(player == null || player != p)
                        PXLMC.NETWORK.sendTo(new CPacketUpdateTileEntitySyncable(getPos(), tag), p);
                }
            }
        }
    }
}
