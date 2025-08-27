package com.latmod.mods.projectex.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ServerProxy implements IProxy
{
	@Nullable
    @Override
	public EntityPlayer getClientPlayer()
	{
		return null;
	}

    @Override
	public void updateEMC(long emc)
	{
	}

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
    }
}