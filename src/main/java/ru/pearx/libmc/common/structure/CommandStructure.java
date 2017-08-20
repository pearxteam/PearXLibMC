package ru.pearx.libmc.common.structure;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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
        if(args.length < 7)
            throw new WrongUsageException(getUsage(sender));
        try
        {
            String name = args[0];
            BlockPos from = new BlockPos(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            BlockPos to = new BlockPos(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
            StructureApi.createStructure(name, from, to, sender.getEntityWorld());
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("command.pxlmc_structure.nan");
        }
    }
}
