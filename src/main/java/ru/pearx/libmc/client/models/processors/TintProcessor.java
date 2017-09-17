package ru.pearx.libmc.client.models.processors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.models.IPXModel;
import ru.pearx.libmc.client.models.OvModel;

import javax.annotation.Nullable;
import java.util.List;

/*
 * Created by mrAppleXZ on 24.07.17 19:32.
 */
@SideOnly(Side.CLIENT)
public class TintProcessor implements IQuadProcessor
{
    private int index;

    public TintProcessor(int index)
    {
        this.index = index;
    }

    @Override
    public void process(List<BakedQuad> quads, @Nullable IBlockState state, @Nullable EnumFacing side, long rand, IPXModel model)
    {
        for(int i = 0; i < quads.size(); i++)
        {
            BakedQuad q = quads.get(i);
            if(q instanceof UnpackedBakedQuad)
            {
                try
                {
                    quads.set(i, new UnpackedBakedQuad((float[][][]) OvModel.unpQuadData.get(q), index, q.getFace(), q.getSprite(), q.shouldApplyDiffuseLighting(), q.getFormat()));
                }
                catch (IllegalAccessException e)
                {
                    PXLMC.getLog().error("Can't get UnpackedBakedQuad's data!");
                }
            }
            else
            {
                quads.set(i, new BakedQuad(q.getVertexData(), index, q.getFace(), q.getSprite(), q.shouldApplyDiffuseLighting(), q.getFormat()));
            }
        }
    }

    @Override
    public boolean processState()
    {
        return true;
    }

    @Override
    public boolean processStack()
    {
        return true;
    }
}
