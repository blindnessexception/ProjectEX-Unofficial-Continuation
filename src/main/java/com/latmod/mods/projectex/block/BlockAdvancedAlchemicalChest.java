package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.tile.TileAdvancedAlchemicalChest;
import moze_intel.projecte.api.state.PEStateProps;
import moze_intel.projecte.gameObjs.blocks.BlockDirection;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockAdvancedAlchemicalChest extends BlockDirection {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

    public BlockAdvancedAlchemicalChest() {
        super(Material.ROCK);
        this.setHardness(10.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PEStateProps.FACING, EnumFacing.NORTH));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileAdvancedAlchemicalChest tileEntity = (TileAdvancedAlchemicalChest) world.getTileEntity(pos);
            if (tileEntity == null) return false;
            if (!player.isSneaking()) {
                player.openGui(ProjectEX.INSTANCE, 5, world, pos.getX(), pos.getY(), pos.getZ());
                player.addStat(StatList.CHEST_OPENED);
            }
        }

        return true;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        if(!world.isRemote) {
            TileAdvancedAlchemicalChest tileEntity = (TileAdvancedAlchemicalChest) world.getTileEntity(pos);
            if (tileEntity != null && player.isSneaking()) {
                tileEntity.handleActivation(player, player.getActiveHand());
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileAdvancedAlchemicalChest) {
            ((TileAdvancedAlchemicalChest) tileEntity).handlePlace(placer, stack);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAdvancedAlchemicalChest();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            return ItemHandlerHelper.calcRedstoneFromInventory(inv);
        } else {
            return 0;
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileAdvancedAlchemicalChest tileAdvancedAlchemicalChest = (TileAdvancedAlchemicalChest) worldIn.getTileEntity(pos);

        return (tileAdvancedAlchemicalChest != null) ? MapColor.getBlockColor(tileAdvancedAlchemicalChest.getColor()) : MapColor.getBlockColor(EnumDyeColor.WHITE);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.removeTileEntity(pos);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            int meta = color.getMetadata();
            items.add(new ItemStack(this, 1, meta));
        }
    }
}
