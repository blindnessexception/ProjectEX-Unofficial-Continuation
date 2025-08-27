package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.tile.TileAdvancedAlchemicalChest;
import com.latmod.mods.projectex.tile.TileAlchemyTable;
import com.latmod.mods.projectex.tile.TileLink;
import moze_intel.projecte.gameObjs.container.AlchBagContainer;
import moze_intel.projecte.gameObjs.gui.GUIAlchChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ProjectEXGuiHandler implements IGuiHandler
{
	public static final int LINK = 1;
	public static final int STONE_TABLE = 2;
	public static final int ARCANE_TABLET = 3;
	public static final int ALCHEMY_TABLE = 4;
    public static final int ADVANCED_ALCHEMICAL_CHEST = 5;

	public static void open(EntityPlayer player, int id, int x, int y, int z)
	{
		player.openGui(ProjectEX.INSTANCE, id, player.world, x, y, z);
	}

	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == LINK)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

			if (tileEntity instanceof TileLink)
			{
				return new ContainerLink(player, (TileLink) tileEntity);
			}
		}
		else if (id == STONE_TABLE)
		{
			return new ContainerStoneTable(player);
		}
		else if (id == ARCANE_TABLET)
		{
			return new ContainerArcaneTablet(player);
		}
		else if (id == ALCHEMY_TABLE)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

			if (tileEntity instanceof TileAlchemyTable)
			{
				return new ContainerAlchemyTable(player, (TileAlchemyTable) tileEntity);
			}
		}
        else if (id == ADVANCED_ALCHEMICAL_CHEST)
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

            if (tile instanceof TileAdvancedAlchemicalChest) {
                IItemHandlerModifiable inventory = (IItemHandlerModifiable) ((TileAdvancedAlchemicalChest) tile).getBag();
                return new ContainerAdvancedAlchemicalChest(player.inventory, EnumHand.OFF_HAND, (TileAdvancedAlchemicalChest) tile, inventory);
            }
        }

		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
        if (id == ADVANCED_ALCHEMICAL_CHEST)
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

            if (tile instanceof TileAdvancedAlchemicalChest) {
                IItemHandlerModifiable inventory = (IItemHandlerModifiable) ((TileAdvancedAlchemicalChest) tile).getBag();
                return new GuiAdvancedAlchChest(player.inventory, EnumHand.OFF_HAND, (TileAdvancedAlchemicalChest) tile, inventory);
            }
        }
		Object container = getServerGuiElement(id, player, world, x, y, z);
		return getClientGuiElement0(id, (Container) container);
	}

	@Nullable
	private Object getClientGuiElement0(int id, @Nullable Container container)
	{
		if (id == LINK)
		{
			if (container instanceof ContainerLink)
			{
				return new GuiLink((ContainerLink) container);
			}
		}
		else if (id == STONE_TABLE)
		{
			if (container instanceof ContainerStoneTable)
			{
				return new GuiStoneTable((ContainerStoneTable) container);
			}
		}
		else if (id == ARCANE_TABLET)
		{
			if (container instanceof ContainerArcaneTablet)
			{
				return new GuiArcaneTablet((ContainerArcaneTablet) container);
			}
		}
		else if (id == ALCHEMY_TABLE)
		{
			if (container instanceof ContainerAlchemyTable)
			{
				return new GuiAlchemyTable((ContainerAlchemyTable) container);
			}
		}

        return null;
	}
}