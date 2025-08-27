package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.tile.TileAdvancedAlchemicalChest;
import moze_intel.projecte.gameObjs.container.AlchBagContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ContainerAdvancedAlchemicalChest extends AlchBagContainer {
    TileAdvancedAlchemicalChest blockEntity;

    public ContainerAdvancedAlchemicalChest(InventoryPlayer invPlayer, EnumHand hand, TileAdvancedAlchemicalChest blockEntity, IItemHandlerModifiable invBag) {
        super(invPlayer, hand, invBag, false);
        ++blockEntity.numPlayersUsing;
        this.blockEntity = blockEntity;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        --this.blockEntity.numPlayersUsing;
    }
}