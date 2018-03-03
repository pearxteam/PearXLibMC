package ru.pearx.libmc.common.tiles.syncable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import ru.pearx.lib.PXL;
import ru.pearx.libmc.common.nbt.serialization.INBTSerializer;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 02.03.18 21:17.
 */
public class TileSyncableComposite extends TileSyncable
{
    private List<INBTSerializer> serializers = new ArrayList<>();

    public List<INBTSerializer> getSerializers()
    {
        return serializers;
    }


    public void sendUpdates(EntityPlayer p, String... dataToSend)
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeCustomData(tag, dataToSend);
        sendUpdates(p, tag);
    }

    @Override
    public void readCustomData(NBTTagCompound tag)
    {
        for(INBTSerializer s : getSerializers())
            if(s.shouldRead(tag))
                s.read(tag);
    }

    @Override
    public void writeCustomData(NBTTagCompound tag, String... data)
    {
        for(INBTSerializer s : getSerializers())
            if(s.shouldWrite(tag) && (data.length == 0 || PXL.arrayContains(data, s.getName())))
                s.write(tag);
    }
}
