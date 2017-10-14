package ru.pearx.libmc.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Created by mrAppleXZ on 10.07.17 22:04.
 */
public class ItemStackUtils
{
    public static void extractAll(IItemHandler hand, int slot)
    {
        ItemStack stack = hand.getStackInSlot(slot);
        if(!stack.isEmpty())
        {
            hand.extractItem(slot, stack.getCount(), false);
        }
    }

    public static void drop(IItemHandler hand, World world, BlockPos pos)
    {
        for(int i = 0; i < hand.getSlots(); i++)
        {
            ItemStack stack = hand.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, stack));
            }
        }
    }

    public static final List<ItemStack> EMPTY_LIST = Collections.singletonList(ItemStack.EMPTY);
    public static List<ItemStack> getIngredientItems(Ingredient ing)
    {
        ItemStack[] stacks = ing.getMatchingStacks();
        if (stacks.length == 0)
            return EMPTY_LIST;
        else
        {
            List<ItemStack> matching = new ArrayList<>();
            for (ItemStack stack : stacks)
            {
                NonNullList<ItemStack> subs = NonNullList.create();
                stack.getItem().getSubItems(stack.getItem().getCreativeTab() == null ? CreativeTabs.SEARCH : stack.getItem().getCreativeTab(), subs);
                for (ItemStack sub : subs)
                    if (ing.apply(sub))
                        matching.add(sub);
            }
            return matching;
        }
    }

}
