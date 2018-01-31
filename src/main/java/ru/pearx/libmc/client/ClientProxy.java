package ru.pearx.libmc.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.PXLGui;
import ru.pearx.libmc.client.gui.structure.GuiStructureCreation;
import ru.pearx.libmc.client.models.IModelProvider;
import ru.pearx.libmc.common.CommonProxy;

/*
 * Created by mrAppleXZ on 24.09.17 21:58.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void openStructureCreationGui()
    {
        Minecraft.getMinecraft().displayGuiScreen(new PXLGui(new GuiStructureCreation()));
    }

    @Override
    public void setupModels(IModelProvider prov)
    {
        prov.setupModels();
    }

    @Override
    public void preInit()
    {
        if(!Minecraft.getMinecraft().getFramebuffer().isStencilEnabled())
            Minecraft.getMinecraft().getFramebuffer().enableStencil();
    }
}
