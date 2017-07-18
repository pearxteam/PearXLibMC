package ru.pearx.libmc.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * Created by mrAppleXZ on 18.07.17 11:08.
 */
public class PXLContainer extends Container
{
    protected int slotCount;
    protected int playerSlotsX, playerSlotsY;
    protected IInventory playerInventory;

    public PXLContainer(IInventory playerInventory, int slotCount, int playerSlotsX, int playerSlotsY)
    {
        this.slotCount = slotCount;
        this.playerSlotsX = playerSlotsX;
        this.playerSlotsY = playerSlotsY;
        this.playerInventory = playerInventory;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < slotCount)
            {
                if (!this.mergeItemStack(itemstack1, slotCount, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, slotCount, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    protected void addPlayerSlots()
    {
        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, playerSlotsX + j1 * 18, l * 18 + playerSlotsY));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            //y = 16 * 3 + 2 * 2 + 6
            this.addSlotToContainer(new Slot(playerInventory, i1, playerSlotsX + i1 * 18, 58 + playerSlotsY));
        }
    }
}
