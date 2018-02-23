package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.blocks.BlockMultiblockPart;
import ru.pearx.libmc.common.structure.blockarray.BlockArray;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Created by mrAppleXZ on 14.11.17 13:34.
 */
public class Multiblock extends IForgeRegistryEntry.Impl<Multiblock>
{
    public static final IForgeRegistry<Multiblock> REGISTRY = new RegistryBuilder<Multiblock>().setName(new ResourceLocation(PXLMC.MODID, "multiblock")).setType(Multiblock.class).create();

    private BlockArray structure;
    private BlockPos masterPos;
    private IBlockState masterState;
    private IBlockState slaveState;
    private ModelResourceLocation itemModel;
    private CreativeTabs itemCreativeTab;

    public BlockArray getStructure()
    {
        return structure;
    }

    public void setStructure(BlockArray structure)
    {
        this.structure = structure;
    }

    public BlockPos getMasterPos()
    {
        return masterPos;
    }

    public void setMasterPos(BlockPos masterPos)
    {
        this.masterPos = masterPos;
    }

    public IBlockState getMasterState()
    {
        return masterState;
    }

    public void setMasterState(IBlockState masterState)
    {
        this.masterState = masterState;
    }

    public IBlockState getSlaveState()
    {
        if(slaveState == null)
            slaveState = getMasterState().withProperty(BlockMultiblockPart.TYPE, BlockMultiblockPart.Type.SLAVE);
        return slaveState;
    }

    public ModelResourceLocation getItemModel()
    {
        return itemModel;
    }

    public void setItemModel(ModelResourceLocation itemModel)
    {
        this.itemModel = itemModel;
    }

    public CreativeTabs getItemCreativeTab()
    {
        return itemCreativeTab;
    }

    public void setItemCreativeTab(CreativeTabs itemCreativeTab)
    {
        this.itemCreativeTab = itemCreativeTab;
    }

    public String getName()
    {
        return I18n.translateToLocalFormatted("multiblock." + getRegistryName() + ".name");
    }

    public void form(World w, BlockPos zeroPos, Rotation rot, @Nullable EntityPlayer pl)
    {
        BlockPos.MutableBlockPos absMasterPos = new BlockPos.MutableBlockPos(getMasterPos());
        absMasterPos = PXLMC.transformPos(absMasterPos, null, rot);
        absMasterPos.setPos(absMasterPos.getX() + zeroPos.getX(), absMasterPos.getY() + zeroPos.getY(), absMasterPos.getZ() + zeroPos.getZ());

        BlockPos.MutableBlockPos absPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos relPos = new BlockPos.MutableBlockPos();
        List<BlockPos> slaves = new ArrayList<>();
        for (BlockPos p : getStructure().getMap().keySet())
        {
            relPos.setPos(p.getX(), p.getY(), p.getZ());
            relPos = PXLMC.transformPos(relPos, null, rot);
            absPos.setPos(relPos.getX() + zeroPos.getX(), relPos.getY() + zeroPos.getY(), relPos.getZ() + zeroPos.getZ());
            if (!p.equals(getMasterPos()))
            {
                w.setBlockState(absPos, getSlaveState());
                TileEntity te = w.getTileEntity(absPos);
                if (te instanceof IMultiblockSlave)
                {
                    ((IMultiblockSlave) te).setMasterPos(absMasterPos);
                }
                w.notifyBlockUpdate(absPos, w.getBlockState(absPos), w.getBlockState(absPos), 2);
                slaves.add(absPos.toImmutable());
            }
        }

        w.setBlockState(absMasterPos, getMasterState());
        TileEntity te = w.getTileEntity(absMasterPos);
        if (te instanceof IMultiblockMaster)
        {
            IMultiblockMaster master = (IMultiblockMaster) te;
            master.setRotation(rot);
            master.setSlavesPositions(slaves);
            master.setIdAndUpdate(getRegistryName());
            master.postForm(pl);
            master.setInactive(false);
            w.notifyBlockUpdate(absMasterPos, w.getBlockState(absMasterPos), w.getBlockState(absMasterPos), 2);
        }
    }

    public Optional<Rotation> checkMultiblock(World w, BlockPos pos, @Nullable EntityPlayer p, @Nullable EnumHand hand)
    {
        return getStructure().check(w, pos);
    }

    public Optional<Rotation> tryForm(World w, BlockPos pos, @Nullable EntityPlayer p, @Nullable EnumHand hand)
    {
        Optional<Rotation> opt = checkMultiblock(w, pos, p, hand);
        opt.ifPresent(rotation -> form(w, pos, rotation, p));
        return opt;
    }

    public static <T>T sendEventToMaster(IBlockAccess world, BlockPos pos, IMultiblockEvent<T> evt, T def)
    {
        try
        {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IMultiblockPart)
            {
                IMultiblockPart part = (IMultiblockPart) te;
                IMultiblockMaster master = part.getMaster();
                if (master != null)
                {
                    return master.handleEvent(evt, part);
                }
            }
        }
        catch(Exception ignored) { }
        return def;
    }
}
