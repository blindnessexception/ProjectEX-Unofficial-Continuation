package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.tile.TileAdvancedAlchemicalChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;

public class GuiAdvancedAlchChest extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation("projecte".toLowerCase(), "textures/gui/alchchest.png");

    public GuiAdvancedAlchChest(InventoryPlayer invPlayer, EnumHand hand, TileAdvancedAlchemicalChest tileEntity, IItemHandlerModifiable invBag) {
        super(new ContainerAdvancedAlchemicalChest(invPlayer, hand, tileEntity, invBag));
        this.xSize = 255;
        this.ySize = 230;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
    }
}
