package ru.pearx.libmc.client.models.processors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;
import ru.pearx.libmc.client.ClientUtils;
import ru.pearx.libmc.client.models.IPXModel;
import ru.pearx.libmc.common.blocks.controllers.HorizontalFacingController;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Arrays;
import java.util.List;

/*
 * Created by mrAppleXZ on 07.07.17 9:07.
 */
@SideOnly(Side.CLIENT)
public class FacingProcessor implements IVertexProcessor
{
    private EnumFacing face;
    private Matrix4f mat_normal;

    @Override
    public void preProcess(List<BakedQuad> quads, @Nullable IBlockState state, @Nullable EnumFacing side, long rand, IPXModel model)
    {
        face = state.getValue(HorizontalFacingController.FACING_H);
        mat_normal = ClientUtils.rotationFromFace(face).getMatrix();
    }

    @Override
    public void processQuad(UnpackedBakedQuad.Builder bld, BakedQuad quad, @Nullable IBlockState state, @Nullable EnumFacing side, long rand, IPXModel model)
    {
        bld.setQuadOrientation(ClientUtils.rotationFromFace(face).rotate(quad.getFace()));
    }

    @Override
    public float[] processVertex(UnpackedBakedQuad.Builder bld, BakedQuad quad, float[] data, int vert, int element, @Nullable IBlockState state, @Nullable EnumFacing side, long rand, IPXModel model)
    {
        VertexFormatElement.EnumUsage us = bld.getVertexFormat().getElement(element).getUsage();
        if (us == VertexFormatElement.EnumUsage.POSITION)
        {
            float x = mat_normal.m00 * data[0] + mat_normal.m01 * data[1] + mat_normal.m02 * data[2] + mat_normal.m03;
            float y = mat_normal.m10 * data[0] + mat_normal.m11 * data[1] + mat_normal.m12 * data[2] + mat_normal.m13;
            float z = mat_normal.m20 * data[0] + mat_normal.m21 * data[1] + mat_normal.m22 * data[2] + mat_normal.m23;

            data[0] = x;
            data[1] = y;
            data[2] = z;
        }
        if (us == VertexFormatElement.EnumUsage.NORMAL)
        {
            float x = mat_normal.m00 * data[0] + mat_normal.m01 * data[1] + mat_normal.m02 * data[2];
            float y = mat_normal.m10 * data[0] + mat_normal.m11 * data[1] + mat_normal.m12 * data[2];
            float z = mat_normal.m20 * data[0] + mat_normal.m21 * data[1] + mat_normal.m22 * data[2];

            data[0] = x;
            data[1] = y;
            data[2] = z;
        }
        return data;
    }

    @Override
    public boolean processState()
    {
        return true;
    }

    @Override
    public boolean processStack()
    {
        return false;
    }
}
