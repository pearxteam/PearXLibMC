package ru.pearx.libmc.client.gui.controls.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import ru.pearx.lib.Color;
import ru.pearx.lib.math.MathUtils;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.IGuiScreen;
import ru.pearx.libmc.client.gui.drawables.item.ItemDrawable;
import ru.pearx.libmc.common.structure.blockarray.BlockArray;
import ru.pearx.libmc.common.structure.blockarray.BlockArrayEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Created by mrAppleXZ on 14.10.17 19:07.
 */
public class BlockArrayShowcase extends AbstractShowcase
{
    public static final float[][] HIGHLIGHT_MODEL =
            {
                    {-0.1f, 1.1f, -0.1f},
                    {1.1f, 1.1f, -0.1f},
                    {1.1f, -0.1f, -0.1f},
                    {-0.1f, -0.1f, -0.1f},

                    {1.1f, 1.1f, -0.1f},
                    {1.1f, 1.1f, 1.1f},
                    {1.1f, -0.1f, 1.1f},
                    {1.1f, -0.1f, -0.1f},

                    {-0.1f, 1.1f, 1.1f},
                    {-0.1f, 1.1f, -0.1f},
                    {-0.1f, -0.1f, -0.1f},
                    {-0.1f, -0.1f, 1.1f},

                    {1.1f, 1.1f, 1.1f},
                    {-0.1f, 1.1f, 1.1f},
                    {-0.1f, -0.1f, 1.1f},
                    {1.1f, -0.1f, 1.1f},

                    {1.1f, -0.1f, 1.1f},
                    {-0.1f, -0.1f, 1.1f},
                    {-0.1f, -0.1f, -0.1f},
                    {1.1f, -0.1f, -0.1f},

                    {-0.1f, 1.1f, 1.1f},
                    {1.1f, 1.1f, 1.1f},
                    {1.1f, 1.1f, -0.1f},
                    {-0.1f, 1.1f, -0.1f},
            };
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
    private boolean stacks;
    private boolean highlight;
    private ResourceLocation buttonTex;
    private Button buttonStacks;
    private Button buttonHighlight;
    private List<ItemDrawable> stackList = new ArrayList<>();
    private Color col = Color.fromARGB(128, 32, 32, 32);

    public BlockArrayShowcase(ResourceLocation buttonTex, BlockArray array)
    {
        this(buttonTex, array, false);
    }

    public BlockArrayShowcase(ResourceLocation buttonTex, BlockArray array, boolean highlightButton)
    {
        this.array = array;
        this.buttonTex = buttonTex;
        this.access = new BlockArrayBlockAccess(array);
        this.rotX = 45;
        this.rotY = 45;
        int size = 12;
        buttonStacks = new Button(buttonTex, "", () -> stacks = !stacks)
        {
            @Override
            public String getText()
            {
                return stacks ? "▲" : "▼";
            }

            @Override
            public int getX()
            {
                return getParent().getWidth() - 3 - getWidth();
            }

            @Override
            public int getY()
            {
                return 3;
            }
        };
        buttonStacks.setSize(size, size);

        if(highlightButton)
        {
            highlight = true;
            buttonHighlight = new Button(buttonTex, "", () -> highlight = !highlight)
            {
                @Override
                public String getText()
                {
                    return highlight ? "□" : "■";
                }

                @Override
                public int getX()
                {
                    return getParent().getWidth() - 3 * 2 - getWidth() * 2;
                }

                @Override
                public int getY()
                {
                    return 3;
                }
            };
            buttonHighlight.setSize(size, size);
        }

        for(BlockArrayEntry entr : array.getMap().values())
        {
            if(!entr.getStack().isEmpty())
            {
                boolean res = false;
                for (ItemDrawable draw : stackList)
                {
                    ItemStack stack = draw.getStack();
                    if (stack.getItem() == entr.getStack().getItem() && stack.getItemDamage() == entr.getStack().getItemDamage() && ItemStack.areItemStackShareTagsEqual(stack, entr.getStack()))
                    {
                        stack.grow(entr.getStack().getCount());
                        res = true;
                    }
                }
                if(!res)
                {
                    stackList.add(new ItemDrawable(entr.getStack().copy(), 1.5f));
                }
            }
        }
    }

    @Override
    public void init()
    {
        int sizeX = array.getMaxX() - array.getMinX() + 1;
        int sizeY = array.getMaxY() - array.getMinY() + 1;
        int w = (int)((getWidth() / sizeX) * 0.6f);
        int h = (int)((getHeight() / sizeY) * 0.6f);
        this.scale = Math.min(w, h);
        getControls().add(buttonStacks);
        if(buttonHighlight != null)
            getControls().add(buttonHighlight);
    }

    @Override
    public void render()
    {
        {
            //todo: make tesrs support
            GlStateManager.pushMatrix();
            BlockRendererDispatcher blockRender = Minecraft.getMinecraft().getBlockRendererDispatcher();
            Tessellator tes = Tessellator.getInstance();
            BufferBuilder bld = tes.getBuffer();

            GlStateManager.translate(getWidth() / 2, getHeight() / 2, 200);
            GlStateManager.scale(scale, -scale, scale);
            GlStateManager.rotate(rotX, 1, 0, 0);
            GlStateManager.rotate(rotY, 0, 1, 0);
            float x = -((array.getMaxX() - array.getMinX() + 1) / 2f + array.getMinX());
            float y = -((array.getMaxY() - array.getMinY() + 1) / 2f + array.getMinY());
            float z = -((array.getMaxZ() - array.getMinZ() + 1) / 2f + array.getMinZ());
            GlStateManager.translate(x, y, z);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            bld.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            for (Map.Entry<BlockPos, BlockArrayEntry> entr : array.getMap().entrySet())
            {
                blockRender.renderBlock(entr.getValue().getState(), entr.getKey(), access, bld);
                /*if (entr.getValue().getTESR() != null)
                {
                    entr.getValue().getTile().setPos(entr.getKey());
                    entr.getValue().getTile().setWorld(Minecraft.getMinecraft().world);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(entr.getKey().getX(), entr.getKey().getY(), entr.getKey().getZ());
                    entr.getValue().getTESR().render(entr.getValue().getTile(), 0, 0, 0, 0, 0, 1);
                    GlStateManager.popMatrix();
                }*/
            }
            tes.draw();
            if (highlight)
            {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                bld.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                for (float[] vert : HIGHLIGHT_MODEL)
                {
                    bld.pos(vert[0], vert[1], vert[2]).color(0.3f, 0.3f, 1f, MathHelper.sin(MathUtils.toRadians(System.currentTimeMillis() / 5 % 360)) * 0.125f + 0.425f).endVertex();
                }
                tes.draw();
                GlStateManager.disableBlend();
                GlStateManager.enableTexture2D();
            }
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 500);
        if(stacks)
        {
            GlStateManager.enableBlend();
            DrawingTools.drawGradientRect(0, 0, getWidth(), getHeight(), col);
            GlStateManager.disableBlend();
            int x = 8;
            int y = 12;
            int w = getWidth() - x*2;

            IGuiScreen scr = getGuiScreen();
            for(ItemDrawable draw : stackList)
            {
                draw.draw(scr, x, y);
                x += 24;
                if(x > w)
                {
                    x = 8;
                    y += 24;
                }
            }
        }
    }

    @Override
    public void render2()
    {
        if(stacks)
        {
            int x = 8;
            int y = 12;
            int w = getWidth() - x*2;
            Point p = getPosOnScreen();
            IGuiScreen scr = getGuiScreen();
            for(ItemDrawable draw : stackList)
            {
                draw.drawTooltip(scr, x, y, getLastMouseX(), getLastMouseY(), p.getX(), p.getY());
                x += 24;
                if(x > w)
                {
                    x = 8;
                    y += 24;
                }
            }
        }
    }

    @Override
    public void postRender()
    {
        GlStateManager.popMatrix();
    }
}
