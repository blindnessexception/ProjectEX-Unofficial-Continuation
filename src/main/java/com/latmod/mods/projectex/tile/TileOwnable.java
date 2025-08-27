package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.api.util.NBTNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileOwnable extends TileEntity {
    public UUID owner = new UUID(0L, 0L);
    public String ownerName = "";

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasUniqueId(NBTNames.OWNER)) owner = compound.getUniqueId(NBTNames.OWNER);
        if (compound.hasKey(NBTNames.OWNER_NAME, Constants.NBT.TAG_STRING)) ownerName = compound.getString(NBTNames.OWNER_NAME);

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setUniqueId(NBTNames.OWNER, owner);
        compound.setString(NBTNames.OWNER_NAME, ownerName);

        return super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public void setOwner(EntityPlayer entityPlayer) {
        this.owner = entityPlayer.getUniqueID();
        this.ownerName = entityPlayer.getName();
        this.markDirty();
    }

    public enum ActivationType {
        DISPLAY_NAME,
        CHECK_OWNERSHIP
    }

    public boolean handleActivation(EntityPlayer player, ActivationType activationType) {
        switch (activationType) {
            case DISPLAY_NAME:
                player.sendStatusMessage(new TextComponentString(ownerName), true);
                break;
            case CHECK_OWNERSHIP:
                if (!owner.equals(player.getUniqueID())) {
                    player.sendStatusMessage(new TextComponentString(TextFormatting.RED + ownerName), true);
                    return false;
                }
                break;
        }

        return true;
    }

    public void handlePlace(@Nullable EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer) setOwner((EntityPlayer) entityLiving);
    }

    public IBlockState getBlockState() {
        return this.world.getBlockState(this.pos);
    }
}
