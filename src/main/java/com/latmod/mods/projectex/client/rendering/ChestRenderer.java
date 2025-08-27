package com.latmod.mods.projectex.client.rendering;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.api.state.ProjectEXStateProps;
import com.latmod.mods.projectex.block.BlockAdvancedAlchemicalChest;
import com.latmod.mods.projectex.tile.TileAdvancedAlchemicalChest;
import moze_intel.projecte.api.state.PEStateProps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("FieldCanBeLocal")
@SideOnly(Side.CLIENT)
public class ChestRenderer extends TileEntitySpecialRenderer<TileAdvancedAlchemicalChest> {
    private final ResourceLocation chestTexture = new ResourceLocation(ProjectEX.MOD_ID, "textures/blocks/advanced_alchemical_chest/chest.png");
    private final ModelChest model = new ModelChest();

    public void render(@Nonnull TileAdvancedAlchemicalChest chestTile, double x, double y, double z, float partialTicks, int destroyStage, float unused) {
        EnumFacing direction = null;
        EnumDyeColor latchColor = null;
        if (chestTile.getWorld() != null && !chestTile.isInvalid()) {
            IBlockState state = chestTile.getBlockState();
            boolean blockCheck = state.getBlock() instanceof BlockAdvancedAlchemicalChest;
            direction = blockCheck ? state.getValue(PEStateProps.FACING) : null;
            latchColor = blockCheck ? chestTile.getColor() : null;
        }
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 4.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else this.bindTexture(chestTexture);

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(x, y + (double)1.0F, z + (double)1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        short angle = 0;
        if (direction != null) {
            switch (direction) {
                case NORTH:
                    angle = 180;
                    break;
                case SOUTH:
                    break;
                case WEST:
                    angle = 90;
                    break;
                case EAST:
                    angle = -90;
            }
        }

        GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        float adjustedLidAngle = chestTile.prevLidAngle + (chestTile.lidAngle - chestTile.prevLidAngle) * partialTicks;
        adjustedLidAngle = 1.0F - adjustedLidAngle;
        adjustedLidAngle = 1.0F - adjustedLidAngle * adjustedLidAngle * adjustedLidAngle;
        this.model.chestLid.rotateAngleX = -(adjustedLidAngle * (float)Math.PI / 2.0F);
        this.model.chestKnob.rotateAngleX = this.model.chestLid.rotateAngleX;
        this.model.chestLid.render(0.0625F);
        this.model.chestBelow.render(0.0625F);
        if (latchColor != null) {
            float[] latchRGB = latchColor.getColorComponentValues();
            GlStateManager.color(latchRGB[0], latchRGB[1], latchRGB[2]);
        }
        this.model.chestKnob.render(0.0625F);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
