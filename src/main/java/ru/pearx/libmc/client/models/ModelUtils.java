package ru.pearx.libmc.client.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Quat4f;

/**
 * Created by mrAppleXZ on 15.05.17 7:12.
 */
@SideOnly(Side.CLIENT)
public class ModelUtils
{
    public static void register(IRegistry<ModelResourceLocation, IBakedModel> registry, ModelResourceLocation loc, OvModel model)
    {
        model.bake();
        registry.putObject(loc, model);
    }

    public static void register(IRegistry<ModelResourceLocation, IBakedModel> registry, ResourceLocation loc, OvModel model)
    {
        register(registry, new ModelResourceLocation(loc, null), model);
    }
}
