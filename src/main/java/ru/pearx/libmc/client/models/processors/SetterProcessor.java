package ru.pearx.libmc.client.models.processors;

import jdk.nashorn.internal.objects.annotations.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
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
 * Created by mrAppleXZ on 14.12.17 21:17.
 */
@SideOnly(Side.CLIENT)
public class SetterProcessor implements IQuadProcessor
{
    private boolean changeTint;
    private boolean changeFacing;
    private boolean changeSprite;
    private boolean changeDiffuseLighting;
    private boolean changeFormat;
    private int tintTo;
    private EnumFacing facingTo;
    private TextureAtlasSprite spriteTo;
    private boolean diffuseLightingTo;
    private VertexFormat formatTo;

    public int getTintTo()
    {
        return tintTo;
    }

    public SetterProcessor setTintTo(int tintTo)
    {
        this.tintTo = tintTo;
        this.changeTint = true;
        return this;
    }

    public EnumFacing getFacingTo()
    {
        return facingTo;
    }

    public SetterProcessor setFacingTo(EnumFacing facingTo)
    {
        this.facingTo = facingTo;
        this.changeFacing = true;
        return this;
    }

    public TextureAtlasSprite getSpriteTo()
    {
        return spriteTo;
    }

    public SetterProcessor setSpriteTo(TextureAtlasSprite spriteTo)
    {
        this.spriteTo = spriteTo;
        this.changeSprite = true;
        return this;
    }

    public boolean getDiffuseLightingTo()
    {
        return diffuseLightingTo;
    }

    public SetterProcessor setDiffuseLightingTo(boolean diffuseLightingTo)
    {
        this.diffuseLightingTo = diffuseLightingTo;
        this.changeDiffuseLighting = true;
        return this;
    }

    public VertexFormat getFormatTo()
    {
        return formatTo;
    }

    public SetterProcessor setFormatTo(VertexFormat formatTo)
    {
        this.formatTo = formatTo;
        this.changeFormat = true;
        return this;
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
                    quads.set(i, new UnpackedBakedQuad((float[][][]) OvModel.unpQuadData.get(q), changeTint ? getTintTo() : q.getTintIndex(), changeFacing ? getFacingTo() : q.getFace(), changeSprite ? getSpriteTo() : q.getSprite(), changeDiffuseLighting ? getDiffuseLightingTo() : q.shouldApplyDiffuseLighting(), changeFormat ? getFormatTo() : q.getFormat()));
                }
                catch (IllegalAccessException e)
                {
                    PXLMC.getLog().error("Can't get UnpackedBakedQuad's data!");
                }
            }
            else
            {
                quads.set(i, new BakedQuad(q.getVertexData(), changeTint ? getTintTo() : q.getTintIndex(), changeFacing ? getFacingTo() : q.getFace(), changeSprite ? getSpriteTo() : q.getSprite(), changeDiffuseLighting ? getDiffuseLightingTo() : q.shouldApplyDiffuseLighting(), changeFormat ? getFormatTo() : q.getFormat()));
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
