package ru.pearx.libmc.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.ClientUtils;
import ru.pearx.libmc.client.models.IModelProvider;

import java.lang.reflect.Field;

/*
 * Created by mrAppleXZ on 10.07.17 22:11.
 */
public class ItemBase extends Item implements IModelProvider
{
    @Override
    @SideOnly(Side.CLIENT)
    public void setupModels()
    {
        ClientUtils.setModelLocation(this);
    }

    @Override
    public String getUnlocalizedName()
    {
        return super.unlocalizedName == null ? "item." + getRegistryName().getResourcePath() : super.getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getUnlocalizedName();
    }
}
