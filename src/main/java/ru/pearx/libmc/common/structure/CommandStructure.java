package ru.pearx.libmc.common.structure;

import com.sun.jna.Structure;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ru.pearx.libmc.PXLMC;

import javax.annotation.Nullable;
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
                    if(args.length >= 8)
                    {
                            String name = args[1];
                            BlockPos from = parseBlockPos(sender, args, 2, false);
                            BlockPos to = parseBlockPos(sender, args, 5, false);
                            StructureApi.createStructure(name, from, to, sender.getEntityWorld());
                            notifyCommandListener(sender, this, "command.pxlmc_structure.success");
                            return;
                    }
                    break;
                }
                case "spawn":
                {
                    if (args.length >= 5)
                    {
                        String name = args[1];
                        BlockPos pos = parseBlockPos(sender, args, 2, false);
                        try
                        {
                            StructureApi.spawnStructure(StructureApi.getStructureNbt(name), pos, sender.getEntityWorld());
                            notifyCommandListener(sender, this, "command.pxlmc_structure.success");
                            return;
                        }
                        catch (IOException e)
                        {
                            PXLMC.getLog().error("An IOException occurred when spawning a structure.", e);
                            throw new CommandException("command.pxlmc_structure.ioexception", e.getMessage());
                        }
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
            case "create":
                if(args.length >= 3 && args.length <= 5)
                {
                    return getTabCompletionCoordinate(args, 2, targetPos);
                }
                if(args.length >= 6 && args.length <= 8)
                {
                    return getTabCompletionCoordinate(args, 5, targetPos);
                }
                break;
            case "spawn":
                if(args.length >= 3 && args.length <= 5)
                {
                    return getTabCompletionCoordinate(args, 2, targetPos);
                }
                break;
        }
        return Collections.emptyList();
    }
}
