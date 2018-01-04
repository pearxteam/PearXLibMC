package ru.pearx.libmc.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.ClientUtils;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 24.12.17 10:45.
 */
public class ItemMultiblock extends ItemBase
{
    public ItemMultiblock()
    {
        setRegistryName(new ResourceLocation(PXLMC.MODID, "multiblock"));
        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("multiblock", Constants.NBT.TAG_STRING) ? Multiblock.REGISTRY.getValue(new ResourceLocation(stack.getTagCompound().getString("multiblock"))).getName() : super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setupModels()
    {
        ClientUtils.setModelLocation(this);
    }

    public ItemStack newStack(ResourceLocation multiblockId)
    {
        return newStack(multiblockId.toString());
    }

    public ItemStack newStack(String multiblockId)
    {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("multiblock", multiblockId);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("multiblock", Constants.NBT.TAG_STRING))
        {
            ResourceLocation id = new ResourceLocation(stack.getTagCompound().getString("multiblock"));
            Multiblock mb = Multiblock.REGISTRY.getValue(id);
            if(mb != null)
            {
                mb.form(worldIn, pos.offset(facing), PXLMC.getRotation(player.getHorizontalFacing()), player);
            }
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        for (Multiblock mb : Multiblock.REGISTRY)
        {
            if(isInCreativeTab(tab, mb))
            {
                ItemStack stack = new ItemStack(this);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("multiblock", mb.getRegistryName().toString());
                stack.setTagCompound(tag);
                items.add(stack);
            }
        }
    }

    protected boolean isInCreativeTab(CreativeTabs tab, Multiblock mb)
    {
        return mb.getItemCreativeTab() != null && (tab == CreativeTabs.SEARCH || mb.getItemCreativeTab() == tab);
    }
}
