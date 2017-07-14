package ru.pearx.libmc.client.models.processors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;
import ru.pearx.libmc.common.blocks.controllers.HorizontalFacingController;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/*
 * Created by mrAppleXZ on 07.07.17 9:07.
 */
@SideOnly(Side.CLIENT)
public class FacingProcessor implements IQuadProcessor
{
    @Override
    public void process(List<BakedQuad> quads, @Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if (state != null)
        {
            EnumFacing face = state.getValue(HorizontalFacingController.FACING_H);
            Matrix4f trans = TRSRTransformation.getMatrix(face);
            for (int iq = 0; iq < quads.size(); iq++)
            {
                BakedQuad q = quads.get(iq);
                UnpackedBakedQuad.Builder bld = new UnpackedBakedQuad.Builder(q.getFormat());
                bld.setQuadTint(1);
                bld.setQuadOrientation(q.getFace());
                bld.setTexture(q.getSprite());
                bld.setApplyDiffuseLighting(q.shouldApplyDiffuseLighting());
                for (int i = 0; i < 4; i++)
                {
                    for (int e = 0; e < q.getFormat().getElementCount(); e++)
                    {
                        float[] lst = new float[q.getFormat().getElement(e).getElementCount()];
                        LightUtil.unpack(q.getVertexData(), lst, q.getFormat(), i, e);
                        if (q.getFormat().getElement(e).getUsage() == VertexFormatElement.EnumUsage.POSITION)
                        {
                            Vector3f vec = new Vector3f(lst[0], lst[1], lst[2]);
                            ForgeHooksClient.transform(vec, trans);
                            lst[0] = vec.x;
                            lst[1] = vec.y;
                            lst[2] = vec.z;
                        }
                        bld.put(e, lst);
                    }
                }
                quads.set(iq, bld.build());
            }
        }
    }
}
