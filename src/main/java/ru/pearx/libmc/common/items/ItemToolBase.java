package ru.pearx.libmc.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.ClientUtils;
import ru.pearx.libmc.client.models.IModelProvider;

import java.util.Set;

/*
 * Created by mrAppleXZ on 14.11.17 9:08.
 */
public class ItemToolBase extends ItemTool implements IModelProvider
{
    protected ItemToolBase(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn)
    {
        super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
    }

    public ItemToolBase(ToolMaterial materialIn, Set<Block> effectiveBlocksIn)
    {
        super(materialIn, effectiveBlocksIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setupModels()
    {
        ClientUtils.setModelLocation(this);
    }
}
