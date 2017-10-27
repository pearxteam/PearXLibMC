package ru.pearx.libmc.client.gui.drawables.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.IGuiScreen;
import ru.pearx.libmc.client.gui.drawables.IGuiDrawable;

/**
 * Created by mrAppleXZ on 15.04.17 9:49.
 */
@SideOnly(Side.CLIENT)
public class ItemDrawable extends AbstractItemDrawable
{
    private ItemStack stack;

    public ItemDrawable(ItemStack stack, float scale)
    {
        this.stack = stack;
        setScale(scale);
    }

    public ItemDrawable(ItemStack stack)
    {
        this(stack, 1);
    }

    public ItemStack getStack()
    {
        return stack;
    }

    public void setStack(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public ItemStack getRenderStack()
    {
        return getStack();
    }
}
