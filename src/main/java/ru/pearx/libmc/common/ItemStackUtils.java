package ru.pearx.libmc.common;

import com.google.common.collect.Lists;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/*
 * Created by mrAppleXZ on 10.07.17 22:04.
 */
public class ItemStackUtils
{
    public static ItemStack extractAll(IItemHandler hand, int slot)
    {
        ItemStack stack = hand.getStackInSlot(slot);
        return hand.extractItem(slot, stack.getCount(), false);
    }

    public static void clear(IItemHandler hand)
    {
        for(int i = 0; i < hand.getSlots(); i++)
            extractAll(hand, i);
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

    public static boolean isCraftingMatrixMatches(@Nonnull InventoryCrafting inv, int width, int height, NonNullList<Ingredient> ings, boolean mirrored)
    {
        //copied from ShapedOreRecipe.
        for (int x = 0; x <= inv.getWidth() - width; x++)
        {
            for (int y = 0; y <= inv.getHeight() - height; ++y)
            {
                if (checkMatch(inv, width, height, ings, x, y, false))
                {
                    return true;
                }

                if (mirrored && checkMatch(inv, width, height, ings, x, y, true))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean checkMatch(InventoryCrafting inv, int width, int height, NonNullList<Ingredient> input, int startX, int startY, boolean mirror)
    {
        //copied from ShapedOreRecipe.
        for (int x = 0; x < inv.getWidth(); x++)
        {
            for (int y = 0; y < inv.getHeight(); y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Ingredient target = Ingredient.EMPTY;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = input.get(width - subX - 1 + subY * width);
                    }
                    else
                    {
                        target = input.get(subX + subY * width);
                    }
                }

                if (!target.apply(inv.getStackInRowAndColumn(x, y)))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isCraftingMatrixMatchesShapeless(InventoryCrafting craft, NonNullList<Ingredient> input, IRecipe recipe, boolean simple)
    {
        //copied from ShapelessOreRecipe.
        int ingredientCount = 0;
        RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
        List<ItemStack> items = Lists.newArrayList();

        for (int i = 0; i < craft.getSizeInventory(); ++i)
        {
            ItemStack itemstack = craft.getStackInSlot(i);
            if (!itemstack.isEmpty())
            {
                ++ingredientCount;
                if (simple)
                    recipeItemHelper.accountStack(itemstack, 1);
                else
                    items.add(itemstack);
            }
        }

        if (ingredientCount != input.size())
            return false;

        if (simple)
            return recipeItemHelper.canCraft(recipe, null);

        return RecipeMatcher.findMatches(items, input) != null;
    }
}
