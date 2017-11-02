package ru.pearx.libmc.client.gui.drawables.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/*
 * Created by mrAppleXZ on 27.10.17 22:10.
 */
@SideOnly(Side.CLIENT)
public class MultiItemDrawable extends AbstractItemDrawable
{
    private List<ItemStack> stacks;
    private int changeDivider = 1000;

    public MultiItemDrawable(List<ItemStack> stacks, float scale, int changeDivider)
    {
        this.stacks = stacks;
        setScale(scale);
        setChangeDivider(changeDivider);
    }

    public MultiItemDrawable(List<ItemStack> stacks, float scale)
    {
        this.stacks = stacks;
        setScale(scale);
    }

    public MultiItemDrawable(List<ItemStack> stacks)
    {
        this.stacks = stacks;
    }

    public List<ItemStack> getStacks()
    {
        return stacks;
    }

    public int getChangeDivider()
    {
        return changeDivider;
    }

    public void setChangeDivider(int changeDivider)
    {
        this.changeDivider = changeDivider;
    }

    @Override
    public ItemStack getRenderStack()
    {
        List<ItemStack> lst = getStacks();
        return lst.get((int)(System.currentTimeMillis() / getChangeDivider() % lst.size()));
    }
}