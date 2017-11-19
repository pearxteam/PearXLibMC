package ru.pearx.libmc.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMaster;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockSlave;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockActivatedEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockBreakEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockPickBlockEvent;
import ru.pearx.libmc.common.tiles.TileMultiblockSlave;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 13.11.17 17:42.
 */
public class BlockMultiblockSlave extends BlockMultiblockPart
{
    public BlockMultiblockSlave()
    {
        super(Material.ROCK);
        setRegistryName("multiblock_slave");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileMultiblockSlave();
    }
}
