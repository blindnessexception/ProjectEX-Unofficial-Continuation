package com.latmod.mods.projectex.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {

    void onPreInit(FMLPreInitializationEvent event);

    void onInit(FMLInitializationEvent event);

    void onPostInit(FMLPostInitializationEvent event);

    void updateEMC(long emc);

    EntityPlayer getClientPlayer();

}
