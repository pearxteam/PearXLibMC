package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by mrAppleXZ on 01.01.18 20:39.
 */
//todo for future use
public class MultiblockCheckResult
{
    private static final MultiblockCheckResult SUCCESS = new MultiblockCheckResult("misc.multiblock.result.success", true);
    private String unlocalizedText;
    private Object[] params;
    private boolean successful;

    public MultiblockCheckResult(String unlocalizedText, boolean successful, Object... params)
    {
        setUnlocalizedText(unlocalizedText);
        setSuccessfull(successful);
        setParams(params);
    }

    public static MultiblockCheckResult wrongBlock(BlockPos pos)
    {
        return new MultiblockCheckResult("misc.multiblock.result.wrongBlock", false, pos.getX(), pos.getY(), pos.getZ());
    }

    public static MultiblockCheckResult success()
    {
        return SUCCESS;
    }

    public String getUnlocalizedText()
    {
        return unlocalizedText;
    }

    public void setUnlocalizedText(String unlocalizedText)
    {
        this.unlocalizedText = unlocalizedText;
    }

    public Object[] getParams()
    {
        return params;
    }

    public void setParams(Object[] params)
    {
        this.params = params;
    }

    public boolean isSuccessful()
    {
        return successful;
    }

    public void setSuccessfull(boolean successful)
    {
        this.successful = successful;
    }

    @SideOnly(Side.CLIENT)
    public String getText()
    {
        return I18n.format(unlocalizedText, params);
    }
}
