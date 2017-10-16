package ru.pearx.libmc.client.gui.controls.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.pearx.libmc.client.TessellatorUtils;
import ru.pearx.libmc.client.gui.controls.Control;
import ru.pearx.libmc.common.structure.blockarray.BlockArray;
import ru.pearx.libmc.common.structure.blockarray.BlockArrayEntry;

import javax.annotation.Nullable;
import java.util.Map;

/*
 * Created by mrAppleXZ on 14.10.17 19:07.
 */
public class BlockArrayShowcase extends AbstractShowcase
{
    public static class BlockArrayBlockAccess implements IBlockAccess
    {
        private BlockArray arr;
        public BlockArrayBlockAccess(BlockArray arr)
        {
            this.arr = arr;
        }

        @Nullable
        @Override
        public TileEntity getTileEntity(BlockPos pos)
        {
            BlockArrayEntry entr = arr.getMap().get(pos);
            return entr == null ? null : entr.getTile();
        }

        @Override
        public int getCombinedLight(BlockPos pos, int lightValue)
        {
            return 0;
        }

        @Override
        public IBlockState getBlockState(BlockPos pos)
        {
            BlockArrayEntry entr = arr.getMap().get(pos);
            return entr == null ? Blocks.AIR.getDefaultState() : entr.getState();
        }

        @Override
        public boolean isAirBlock(BlockPos pos)
        {
            return getBlockState(pos).getBlock() == Blocks.AIR;
        }

        @Override
        public Biome getBiome(BlockPos pos)
        {
            return Biomes.PLAINS;
        }

        @Override
        public int getStrongPower(BlockPos pos, EnumFacing direction)
        {
            return 0;
        }

        @Override
        public WorldType getWorldType()
        {
            return WorldType.FLAT;
        }

        @Override
        public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
        {
            return getBlockState(pos).isSideSolid(this, pos, side);
        }
    }

    private BlockArray array;
    private BlockArrayBlockAccess access;

    public BlockArrayShowcase(BlockArray array)
    {
        this.array = array;
        this.access = new BlockArrayBlockAccess(array);
        this.scale = 50;
    }

    @Override
    public void render()
    {
        //todo: make tesrs support
        GlStateManager.pushMatrix();
        BlockRendererDispatcher blockRender = Minecraft.getMinecraft().getBlockRendererDispatcher();
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bld = tes.getBuffer();

        GlStateManager.enableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.translate(getWidth() / 2, getHeight() / 2, 100);
        GlStateManager.scale(scale, -scale, scale);
        GlStateManager.rotate(rotX, 1, 0, 0);
        GlStateManager.rotate(rotY, 0, 1, 0);
        float x = -((array.getMaxX() - array.getMinX() + 1) / 2f + array.getMinX());
        float y = -((array.getMaxY() - array.getMinY() + 1) / 2f + array.getMinY());
        float z = -((array.getMaxZ() - array.getMinZ() + 1) / 2f + array.getMinZ());
        GlStateManager.translate(x, y, z);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        bld.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        for(Map.Entry<BlockPos, BlockArrayEntry> entr : array.getMap().entrySet())
        {
            blockRender.renderBlock(entr.getValue().getState(), entr.getKey(), access, bld);
            if(entr.getValue().getTESR() != null)
            {
                entr.getValue().getTile().setPos(entr.getKey());
                entr.getValue().getTile().setWorld(Minecraft.getMinecraft().world);
                GlStateManager.pushMatrix();
                GlStateManager.translate(entr.getKey().getX(), entr.getKey().getY(), entr.getKey().getZ());
                entr.getValue().getTESR().render(entr.getValue().getTile(), 0, 0, 0, 0, 0, 1);
                GlStateManager.popMatrix();
            }
        }
        tes.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }
}
