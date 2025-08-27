package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.tile.TileOwnable;
import com.latmod.mods.projectex.tile.TileTransmutationInterface;
import moze_intel.projecte.gameObjs.items.PhilosophersStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTransmutationInterface extends Block {

    public static final PropertyBool FILTER = PropertyBool.create("filter");

    public BlockTransmutationInterface() {
        super(Material.ROCK, MapColor.RED);
        setHardness(2F);
        setLightLevel(15);
        setDefaultState(getBlockState().getBaseState().withProperty(FILTER, true));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileTransmutationInterface();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (playerIn.getHeldItem(hand).getItem() instanceof PhilosophersStone) {
                if (tileEntity instanceof TileTransmutationInterface)
                    ((TileTransmutationInterface) tileEntity).toggleFilter(playerIn);
            } else {
                if (tileEntity instanceof TileTransmutationInterface)
                    return ((TileTransmutationInterface) tileEntity).handleActivation(playerIn, TileOwnable.ActivationType.DISPLAY_NAME);
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileTransmutationInterface) {
            ((TileTransmutationInterface) tileEntity).handlePlace(placer, stack);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FILTER) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FILTER);
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }
}
