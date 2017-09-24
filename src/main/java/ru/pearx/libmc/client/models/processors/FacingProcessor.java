package ru.pearx.libmc.client.models.processors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.models.IPXModel;
import ru.pearx.libmc.common.blocks.controllers.HorizontalFacingController;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/*
 * Created by mrAppleXZ on 07.07.17 9:07.
 */
@SideOnly(Side.CLIENT)
public class FacingProcessor implements IVertexProcessor
{
    private EnumFacing face;
    private Matrix4f mat;

    @Override
    public void preProcess(List<BakedQuad> quads, @Nullable IBlockState state, @Nullable EnumFacing side, long rand, IPXModel model)
    {
        face = state.getValue(HorizontalFacingController.FACING_H);
        mat = TRSRTransformation.getMatrix(face);
    }

    @Override
    public float[] processVertex(UnpackedBakedQuad.Builder bld, BakedQuad quad, float[] data, int vert, int element, @Nullable IBlockState state, @Nullable EnumFacing side, long rand, IPXModel model)
    {
        if (bld.getVertexFormat().getElement(element).getUsage() == VertexFormatElement.EnumUsage.POSITION)
        {
            float x = mat.m00*data[0] + mat.m01*data[1] + mat.m02*data[2] + mat.m03;
            float y = mat.m10*data[0] + mat.m11*data[1] + mat.m12*data[2] + mat.m13;
            float z = mat.m20*data[0] + mat.m21*data[1] + mat.m22*data[2] + mat.m23;
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
