package ru.pearx.libmc.client.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by mrAppleXZ on 17.05.17 8:10.
 */
@SideOnly(Side.CLIENT)
public interface IPXModel extends IBakedModel
{
    void bake();
    void setStack(ItemStack stack);
    ItemStack getStack();
}
