package com.latmod.mods.projectex.proxy;

import com.latmod.mods.projectex.ProjectEXKeyBindings;
import com.latmod.mods.projectex.client.rendering.ChestRenderer;
import com.latmod.mods.projectex.integration.PersonalEMC;
import com.latmod.mods.projectex.item.ProjectEXItems;
import com.latmod.mods.projectex.tile.TileAdvancedAlchemicalChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author LatvianModder
 */
public class ClientProxy implements IProxy
{
    @Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void updateEMC(long emc)
	{
		PersonalEMC.get(Minecraft.getMinecraft().player).setEmc(emc);
	}

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        // Register Tile Renders
        ClientRegistry.bindTileEntitySpecialRenderer(TileAdvancedAlchemicalChest.class, new ChestRenderer());
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        ProjectEXKeyBindings.init();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        // BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        itemColors.registerItemColorHandler((stack, tintIndex)
                -> EnumDyeColor.byMetadata(stack.getMetadata()).getColorValue(), ProjectEXItems.ADVANCED_ALCHEMICAL_CHEST);
    }
}