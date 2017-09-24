package ru.pearx.libmc.common.structure;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.networking.packets.CPacketOpenStructureCreationGui;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Created by mrAppleXZ on 21.08.17 0:24.
 */
public class CommandStructure extends CommandBase
{
    @Override
    public String getName()
    {
        return "pxlmc_structure";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "command.pxlmc_structure.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length >= 1)
        {
            switch (args[0])
            {
                case "create":
                {
                    if(sender instanceof EntityPlayerMP)
                    {
                        PXLMC.NETWORK.sendTo(new CPacketOpenStructureCreationGui(), (EntityPlayerMP) sender);
                    }
                    break;
                }
                case "spawn":
                {
                    if (args.length >= 5)
                    {
                        String name = args[1];
                        BlockPos pos = parseBlockPos(sender, args, 2, false);
                        NBTTagCompound tag;
                        try
                        {
                            try
                            {
                                tag = StructureApi.INSTANCE.getStructureNbt(new ResourceLocation(name));
                            }
                            catch(FileNotFoundException e)
                            {
                                tag = StructureApi.INSTANCE.getStructureNbt(name);
                            }
                        }
                        catch (IOException e)
                        {
                            PXLMC.getLog().error("An IOException occurred when spawning a structure.", e);
                            throw new CommandException("command.pxlmc_structure.ioexception", e.toString());
                        }
                        StructureApi.INSTANCE.spawnStructure(tag, pos, (WorldServer)sender.getEntityWorld(), sender.getEntityWorld().rand);
                        notifyCommandListener(sender, this, "command.pxlmc_structure.success");
                        return;
                    }
                }
            }
        }
        throw new WrongUsageException(getUsage(sender));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return Arrays.asList("create", "spawn");
        }
        switch (args[0])
        {
            case "spawn":
                if(args.length >= 3 && args.length <= 5)
                {
                    return getTabCompletionCoordinate(args, 2, targetPos);
                }
                break;
        }
        return Collections.emptyList();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
}
