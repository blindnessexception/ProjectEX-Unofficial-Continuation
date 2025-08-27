package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXEventHandler;
import com.latmod.mods.projectex.api.state.ProjectEXStateProps;
import com.latmod.mods.projectex.block.BlockAdvancedAlchemicalChest;
import com.latmod.mods.projectex.block.ProjectEXBlocks;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.state.PEStateProps;
import moze_intel.projecte.gameObjs.items.AlchemicalBag;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileAdvancedAlchemicalChest extends TileOwnable implements IItemHandler, ITickable {
    protected IItemHandler itemHandlerCapability;
    protected EnumDyeColor color;

    private int tickSinceSync;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return !newSate.getBlock().equals(oldState.getBlock());
    }

    public @Nullable IItemHandler getBag() {
        if (this.itemHandlerCapability == null) {
            @Nullable EntityPlayer player = this.world.getPlayerEntityByUUID(this.owner);
            if (player == null) return null;
            this.itemHandlerCapability = player.getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null).getBag(this.getColor());
            return this.itemHandlerCapability;
        }

        return this.itemHandlerCapability;
    }

    public EnumDyeColor getColor() {
        return this.color;
    }

    @Override
    public void handlePlace(@Nullable EntityLivingBase entityLiving, ItemStack stack) {
        super.handlePlace(entityLiving, stack);
        this.color = EnumDyeColor.byMetadata(stack.getMetadata());

        this.markDirty();
    }

    public boolean handleActivation(EntityPlayer player, EnumHand hand) {
        if (!super.handleActivation(player, ActivationType.CHECK_OWNERSHIP)) {
            return false;
        }

        ItemStack itemStack = player.getHeldItem(hand);
        if (itemStack.isEmpty()) {
            player.sendStatusMessage(new TextComponentString("Current Color: " + this.getColor().getName().toUpperCase()), true);
            return false;
        } else if (itemStack.getItem() instanceof AlchemicalBag) {
            EnumDyeColor dyeColor = EnumDyeColor.byMetadata(itemStack.getItemDamage());
            this.color = dyeColor;
            this.itemHandlerCapability = null;
            this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
            this.markDirty();
            player.sendStatusMessage(new TextComponentString("Color set to " + dyeColor.getName().toUpperCase()), true);
        } else {
            player.sendStatusMessage(new TextComponentString("Invalid item!"), true);
        }

        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings({"unchecked"})
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? (T) this.getBag() : super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        updateChest();
    }

    @Override
    public int getSlots() {
        @Nullable IItemHandler bag = this.getBag();
        return bag == null ? 0 : bag.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        @Nullable IItemHandler bag = this.getBag();
        return bag == null ? ItemStack.EMPTY : bag.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        @Nullable IItemHandler bag = this.getBag();
        return bag == null ? stack : bag.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        @Nullable IItemHandler bag = this.getBag();
        return bag == null ? ItemStack.EMPTY : bag.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        @Nullable IItemHandler bag = this.getBag();
        return bag == null ? 0 : bag.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        @Nullable IItemHandler bag = this.getBag();
        return bag != null && bag.isItemValid(slot, stack);
    }

    protected void updateChest() {
        if (++this.tickSinceSync % 20 * 4 == 0) {
            this.world.addBlockEvent(this.pos, ProjectEXBlocks.ADVANCED_ALCHEMICAL_CHEST, 1, this.numPlayersUsing);
        }

        this.prevLidAngle = this.lidAngle;
        float angleIncrement = 0.1F;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            this.world.playSound(null, this.pos, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            if (this.numPlayersUsing > 0) {
                this.lidAngle += angleIncrement;
            } else {
                this.lidAngle -= angleIncrement;
            }
            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }
            if (this.lidAngle < 0.5F && this.prevLidAngle >= 0.5F) {
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        switch (id) {
            case 1:
                this.numPlayersUsing = type;
                return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.color = EnumDyeColor.byMetadata(compound.getByte("bag_color"));

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setByte("bag_color", (byte) this.color.getMetadata());

        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound nbtTag = pkt.getNbtCompound();

        this.itemHandlerCapability = null;
        this.color = EnumDyeColor.byMetadata(nbtTag.getByte("bag_color"));

        super.onDataPacket(net, pkt);
    }
}
