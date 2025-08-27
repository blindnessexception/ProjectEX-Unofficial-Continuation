package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.api.util.NBTNames;
import com.latmod.mods.projectex.block.BlockTransmutationInterface;
import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class TileTransmutationInterface extends TileOwnable implements IItemHandler, ITickable {
    private boolean isDirty = false;
    private ItemStack[] stack;

    private ItemStack[] fetchKnowledge() {
        if (stack != null)
            return stack;
        IKnowledgeProvider provider = PersonalEMC.get(world, owner);
        return provider != null ? stack = provider.getKnowledge().toArray(new ItemStack[0]) : new ItemStack[]{};
    }

    private int getCount(@Nullable IKnowledgeProvider provider, int slot) {
        if (provider == null)
            return 0;

        long emc = provider.getEmc();
        if (emc < 0)
            return 0;
        long value = ProjectEAPI.getEMCProxy().getValue(fetchKnowledge()[slot]);
        if (value < 0)
            return 0;

        // return emc.divide(value).min(BigInteger.valueOf(ProjectEXConfig.general.emc_link_max_out)).intValue();
        return Math.toIntExact(Math.min(emc / value, ProjectEXConfig.general.emc_link_max_out));
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void onLoad() {
        if (world.isRemote)
            world.tickableTileEntities.remove(this);

        validate();
    }

    @Override
    public void update() {
        stack = null;

        if (isDirty) {
            isDirty = false;
            world.markChunkDirty(pos, this);
        }
    }

    public void toggleFilter(EntityPlayer player) {
        if (world == null || world.isRemote || owner == null) return;
        boolean status = getFilterStatus();
        if (status) {
            setFilterStatus(false);
            player.sendStatusMessage(new TextComponentString("NBT Filter " + TextFormatting.RED + "Disabled"), true);
        } else {
            setFilterStatus(true);
            player.sendStatusMessage(new TextComponentString("NBT Filter " + TextFormatting.GREEN + "Enabled"), true);
        }
    }

    public void setFilterStatus(boolean status) {
        if (world == null || world.isRemote) return;
        world.setBlockState(getPos(), getBlockType().getDefaultState().withProperty(BlockTransmutationInterface.FILTER, status));
    }

    public boolean getFilterStatus() {
        return world.getBlockState(getPos()).getValue(BlockTransmutationInterface.FILTER);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this : super.getCapability(capability, side);
    }

    public boolean hasNoOwner() {
        return owner.getLeastSignificantBits() == 0L && owner.getMostSignificantBits() == 0L;
    }

    @Override
    public void markDirty() {
        isDirty = true;
    }

    @Override
    public int getSlots() {
        return fetchKnowledge().length + 1;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return !oldState.getBlock().equals(newSate.getBlock());
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (world.isRemote || hasNoOwner() || slot < 1 || fetchKnowledge().length < slot)
            return ItemStack.EMPTY;
        IKnowledgeProvider provider = PersonalEMC.get(world, owner);
        int count = getCount(provider, slot - 1);
        if (count < 1)
            return ItemStack.EMPTY;
        ItemStack item = stack[slot - 1].copy();
        item.setCount(count);
        return item;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (world.isRemote || slot != 0 || hasNoOwner() || !isItemValid(slot, stack) || stack.isEmpty())
            return stack;
        fetchKnowledge();

        int count = stack.getCount();
        stack = ItemHandlerHelper.copyStackWithSize(stack, 1);
        if (count < 1)
            return stack;

        if (getFilterStatus() && !stack.hasTagCompound()) return stack;
        if (simulate)
            return ItemStack.EMPTY;

        IKnowledgeProvider provider = PersonalEMC.get(world, owner);
        long emc = ProjectEAPI.getEMCProxy().getSellValue(stack);
        if (provider == null)
            return stack;
        long sum = emc * count;
        PersonalEMC.add(provider, sum);

        EntityPlayerMP player = Objects.requireNonNull(world.getMinecraftServer()).getPlayerList().getPlayerByUUID(owner);
        if (provider.addKnowledge(stack))
            provider.sync(player);

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        IKnowledgeProvider provider = PersonalEMC.get(world, owner);
        if (world.isRemote || hasNoOwner() || slot < 1 || fetchKnowledge().length < slot || provider == null)
            return ItemStack.EMPTY;

        amount = Math.min(amount, getCount(provider, slot - 1));
        if (amount < 1)
            return ItemStack.EMPTY;
        ItemStack item = stack[slot - 1].copy();
        item.setCount(amount);
        if (simulate)
            return item;

        long emc = ProjectEAPI.getEMCProxy().getValue(item);
        long sum = emc * amount;
        PersonalEMC.remove(provider, sum);

        return item;
    }

    @Override
    public int getSlotLimit(int slot) {
        return ProjectEXConfig.general.emc_link_max_out;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return ProjectEAPI.getEMCProxy().hasValue(stack);
    }
}
