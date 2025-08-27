package com.latmod.mods.projectex.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCompactSun extends Block {

    public BlockCompactSun() {
        super(Material.ROCK);
        this.setLightLevel(15.0f / 16.0f);
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.YELLOW;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    public static boolean adjacent(@Nullable World worldIn, BlockPos pos) {
        return adjacent(worldIn, pos, null);
    }

    public static boolean adjacent(@Nullable World worldIn, BlockPos pos, @Nullable EnumFacing filterDirection) {
        if (worldIn == null) return false;

        for (EnumFacing dir : EnumFacing.VALUES) {
            if (filterDirection != null && dir != filterDirection) continue;
            if (worldIn.getBlockState(pos.offset(dir)).getBlock() instanceof BlockCompactSun) return true;
        }

        return false;
    }
}
