package com.latmod.mods.projectex;

import com.latmod.mods.projectex.block.*;
import com.latmod.mods.projectex.item.*;
import com.latmod.mods.projectex.tile.*;
import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.items.KleinStar;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID)
public class ProjectEXEventHandler
{
    private static Block withName(Block item, String name)
	{
		item.setCreativeTab(ProjectEX.TAB);
		item.setRegistryName(name);
		item.setTranslationKey(ProjectEX.MOD_ID + "." + name);
		return item;
	}

	private static Item withName(Item item, String name)
	{
		item.setCreativeTab(ProjectEX.TAB);
		item.setRegistryName(name);
		item.setTranslationKey(ProjectEX.MOD_ID + "." + name);
		return item;
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> r = event.getRegistry();

        r.register(withName(new BlockAdvancedAlchemicalChest(), "advanced_alchemical_chest"));
        GameRegistry.registerTileEntity(TileAdvancedAlchemicalChest.class, new ResourceLocation(ProjectEX.MOD_ID, "advanced_alchemical_chest"));

        r.register(withName(new BlockCompactSun(), "compact_sun"));

		if (ProjectEXConfig.items.link)
		{
			r.register(withName(new BlockEnergyLink(), "energy_link"));
			r.register(withName(new BlockLinkMK1(), "personal_link"));
			r.register(withName(new BlockLinkMK2(), "refined_link"));
			r.register(withName(new BlockLinkMK3(), "compressed_refined_link"));
			r.register(withName(new BlockTransmutationInterface(), "transmutation_interface"));
			GameRegistry.registerTileEntity(TileEnergyLink.class, new ResourceLocation(ProjectEX.MOD_ID, "energy_link"));
			GameRegistry.registerTileEntity(TileLinkMK1.class, new ResourceLocation(ProjectEX.MOD_ID, "personal_link"));
			GameRegistry.registerTileEntity(TileLinkMK2.class, new ResourceLocation(ProjectEX.MOD_ID, "refined_link"));
			GameRegistry.registerTileEntity(TileLinkMK3.class, new ResourceLocation(ProjectEX.MOD_ID, "compressed_refined_link"));
			GameRegistry.registerTileEntity(TileTransmutationInterface.class, new ResourceLocation(ProjectEX.MOD_ID, "transmutation_interface"));
		}

		if (ProjectEXConfig.items.collectors)
		{
			r.register(withName(new BlockCollector(), "collector"));
			GameRegistry.registerTileEntity(TileCollector.class, new ResourceLocation(ProjectEX.MOD_ID, "collector"));
		}

		if (ProjectEXConfig.items.relays)
		{
			r.register(withName(new BlockRelay(), "relay"));
			GameRegistry.registerTileEntity(TileRelay.class, new ResourceLocation(ProjectEX.MOD_ID, "relay"));
		}

		if (ProjectEXConfig.items.power_flowers)
		{
			r.register(withName(new BlockPowerFlower(), "power_flower"));
			GameRegistry.registerTileEntity(TilePowerFlower.class, new ResourceLocation(ProjectEX.MOD_ID, "power_flower"));
		}

		if (ProjectEXConfig.items.stone_table)
		{
			r.register(withName(new BlockStoneTable(), "stone_table"));
		}

		if (ProjectEXConfig.items.alchemy_table)
		{
			r.register(withName(new BlockAlchemyTable(), "alchemy_table"));
			GameRegistry.registerTileEntity(TileAlchemyTable.class, new ResourceLocation(ProjectEX.MOD_ID, "alchemy_table"));
		}


	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> r = event.getRegistry();

        ItemBlock advanced_alchemical_chest = new ItemBlock(ProjectEXBlocks.ADVANCED_ALCHEMICAL_CHEST) {
            @Override
            public String getItemStackDisplayName(ItemStack stack) {
                EnumDyeColor color = EnumDyeColor.byMetadata(stack.getMetadata());
                return super.getItemStackDisplayName(stack) + " (" + I18n.format("item.fireworksCharge." +
                        (!color.equals(EnumDyeColor.LIGHT_BLUE) ? color.getName() : "lightBlue")) + ")";
            }
        };
        r.register(advanced_alchemical_chest.setRegistryName("advanced_alchemical_chest"));

        ItemBlock compact_sun = new ItemBlock(ProjectEXBlocks.COMPACT_SUN) {
            @Override
            public IRarity getForgeRarity(ItemStack stack) {
                return EnumRarity.EPIC;
            }
        };
        r.register(compact_sun.setRegistryName("compact_sun"));

		if (ProjectEXConfig.items.link)
		{
			r.register(new ItemBlock(ProjectEXBlocks.ENERGY_LINK).setRegistryName("energy_link"));
			r.register(new ItemBlock(ProjectEXBlocks.PERSONAL_LINK).setRegistryName("personal_link"));
			r.register(new ItemBlock(ProjectEXBlocks.REFINED_LINK).setRegistryName("refined_link"));
			r.register(new ItemBlock(ProjectEXBlocks.COMPRESSED_REFINED_LINK).setRegistryName("compressed_refined_link"));
			r.register(new ItemBlock(ProjectEXBlocks.TRANSMUTATION_INTERFACE).setRegistryName("transmutation_interface"));
		}

		if (ProjectEXConfig.items.collectors)
		{
			r.register(new ItemBlockTier(ProjectEXBlocks.COLLECTOR).setRegistryName("collector"));
		}

		if (ProjectEXConfig.items.relays)
		{
			r.register(new ItemBlockTier(ProjectEXBlocks.RELAY).setRegistryName("relay"));
		}

		if (ProjectEXConfig.items.power_flowers)
		{
			r.register(withName(new ItemCompressedCollector(), "compressed_collector"));
			r.register(new ItemBlockTier(ProjectEXBlocks.POWER_FLOWER).setRegistryName("power_flower"));
		}

		if (ProjectEXConfig.items.stone_table)
		{
			r.register(withName(new ItemBlock(ProjectEXBlocks.STONE_TABLE), "stone_table"));
		}

		if (ProjectEXConfig.items.alchemy_table)
		{
			r.register(withName(new ItemBlock(ProjectEXBlocks.ALCHEMY_TABLE), "alchemy_table"));
		}

		if (ProjectEXConfig.items.stars)
		{
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.EIN), "magnum_star_ein"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.ZWEI), "magnum_star_zwei"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.DREI), "magnum_star_drei"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.VIER), "magnum_star_vier"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.SPHERE), "magnum_star_sphere"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.OMEGA), "magnum_star_omega"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.EIN), "colossal_star_ein"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.ZWEI), "colossal_star_zwei"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.DREI), "colossal_star_drei"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.VIER), "colossal_star_vier"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.SPHERE), "colossal_star_sphere"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.OMEGA), "colossal_star_omega"));
		}

		r.register(withName(new ItemFuel(), "fuel"));
		r.register(withName(new ItemMatter(), "matter"));

		if (ProjectEXConfig.items.clay_matter)
		{
			r.register(withName(new Item(), "clay_matter").setTranslationKey(ProjectEX.MOD_ID + ".matter.clay"));
		}

		r.register(withName(new Item(), "final_star_shard"));

		if (ProjectEXConfig.items.final_star)
		{
			r.register(withName(new ItemFinalStar(), "final_star"));
		}

		if (ProjectEXConfig.items.knowledge_sharing_book)
		{
			r.register(withName(new ItemKnowledgeSharingBook(), "knowledge_sharing_book"));
		}

		if (ProjectEXConfig.items.arcane_tablet)
		{
			r.register(withName(new ItemArcaneTablet(), "arcane_tablet"));
		}
	}

	@SubscribeEvent
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
	{
		if (ProjectEXConfig.items.stars && event.crafting.getItem() instanceof ItemMagnumStar)
		{
			IItemEmc star = (IItemEmc) event.crafting.getItem();

			for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++)
			{
				ItemStack stack = event.craftMatrix.getStackInSlot(i);

				if (stack.getItem() instanceof IItemEmc)
				{
					star.addEmc(event.crafting, ((IItemEmc) stack.getItem()).getStoredEmc(stack));
				}
			}
		}
	}

	@SubscribeEvent
	public static void addRecipes(RegistryEvent.Register<IRecipe> event)
	{
		IForgeRegistry<IRecipe> r = event.getRegistry();

		EnumMatter prevMatter = null;
		EnumTier prevTier = null;
		EnumFuel prevFuel = null;
        EnumDyeColor prevBag = null;
        String[] dyes =
                {
                        "Black",
                        "Red",
                        "Green",
                        "Brown",
                        "Blue",
                        "Purple",
                        "Cyan",
                        "LightGray",
                        "Gray",
                        "Pink",
                        "Lime",
                        "Yellow",
                        "LightBlue",
                        "Magenta",
                        "Orange",
                        "White"
                };
		Ingredient stone = Ingredient.fromStacks(new ItemStack(ObjHandler.philosStone)),
				glow = Ingredient.fromStacks(new ItemStack(Blocks.GLOWSTONE)),
                chest = Ingredient.fromItem(ProjectEXItems.ADVANCED_ALCHEMICAL_CHEST),
                dmatter = Ingredient.fromStacks(new ItemStack(ObjHandler.matter, 1, 0)),
                low = Ingredient.fromStacks(new ItemStack(ObjHandler.covalence, 1, 0)),
                medium = Ingredient.fromStacks(new ItemStack(ObjHandler.covalence, 1, 1)),
                high = Ingredient.fromStacks(new ItemStack(ObjHandler.covalence, 1, 2));

        for (EnumDyeColor bag : EnumDyeColor.values()) {
            if (prevBag != null) {
                Ingredient bagIng = Ingredient.fromStacks(new ItemStack(ObjHandler.alchBag, 1, bag.ordinal())),
                        dye = new OreIngredient("dye" + dyes[bag.getDyeDamage()]);

                NonNullList<Ingredient> list1 = NonNullList.create();
                list1.add(chest);
                list1.add(dye);
                r.register(new ShapelessRecipes("projectex:advanced_alchemical_chest", new ItemStack(ProjectEXItems.ADVANCED_ALCHEMICAL_CHEST, 1, bag.ordinal()), list1).setRegistryName("advanced_alchemical_chest/recolor_" + bag.getName()));

                NonNullList<Ingredient> list2 = NonNullList.create();
                list2.add(dmatter);
                list2.add(low);
                list2.add(dmatter);
                list2.add(medium);
                list2.add(bagIng);
                list2.add(medium);
                list2.add(high);
                list2.add(low);
                list2.add(high);
                r.register(new ShapedRecipes("projectex:advanced_alchemical_chest", 3, 3, list2, new ItemStack(ProjectEXItems.ADVANCED_ALCHEMICAL_CHEST, 1, bag.ordinal())).setRegistryName("advanced_alchemical_chest/" + bag.getName()));
            }

            prevBag = bag;
        }

		for (EnumMatter matter : EnumMatter.VALUES)
		{
			if (prevMatter != null)
			{
				Ingredient prevMatterIngredient = Ingredient.fromStacks(prevMatter.get()),
						afuel = Ingredient.fromStacks(new ItemStack(ProjectEXItems.FUEL, 1, prevMatter.ordinal()));


				NonNullList<Ingredient> listh = NonNullList.create();
				listh.add(afuel);
				listh.add(afuel);
				listh.add(afuel);
				listh.add(prevMatterIngredient);
				listh.add(prevMatterIngredient);
				listh.add(prevMatterIngredient);
				listh.add(afuel);
				listh.add(afuel);
				listh.add(afuel);
				r.register(new ShapedRecipes("projectex:matter", 3, 3, listh, matter.get()).setRegistryName("matter/" + matter.getName() + "_h"));

				NonNullList<Ingredient> listv = NonNullList.create();
				listv.add(afuel);
				listv.add(prevMatterIngredient);
				listv.add(afuel);
				listv.add(afuel);
				listv.add(prevMatterIngredient);
				listv.add(afuel);
				listv.add(afuel);
				listv.add(prevMatterIngredient);
				listv.add(afuel);
				r.register(new ShapedRecipes("projectex:matter", 3, 3, listv, matter.get()).setRegistryName("matter/" + matter.getName() + "_v"));
			}

			prevMatter = matter;
		}



		for (EnumTier tier : EnumTier.VALUES)
		{
			if (ProjectEXConfig.items.power_flowers)
			{
				r.register(new ShapedRecipes("projectex:compressed_collector", 3, 3, NonNullList.withSize(9, Ingredient.fromStacks(new ItemStack(ProjectEXItems.COLLECTOR, 1, tier.ordinal()))), new ItemStack(ProjectEXItems.COMPRESSED_COLLECTOR, 1, tier.ordinal())).setRegistryName("compressed_collector/" + tier.getName()));

				NonNullList<Ingredient> list = NonNullList.create();
				Ingredient ccollector = Ingredient.fromStacks(new ItemStack(ProjectEXItems.COMPRESSED_COLLECTOR, 1, tier.ordinal()));
				Ingredient relay = Ingredient.fromStacks(new ItemStack(ProjectEXItems.RELAY, 1, tier.ordinal()));

				list.add(ccollector);
				list.add(Ingredient.fromItem(ProjectEXItems.ENERGY_LINK));
				list.add(ccollector);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				r.register(new ShapedRecipes("projectex:power_flower", 3, 3, list, new ItemStack(ProjectEXItems.POWER_FLOWER, 1, tier.ordinal())).setRegistryName("power_flower/" + tier.getName()));
			}

			if (prevTier != null)
			{
				Ingredient matterIngredient = Ingredient.fromStacks(tier.matter.get());

				if (ProjectEXConfig.items.collectors)
				{
					NonNullList<Ingredient> list = NonNullList.create();
					list.add(Ingredient.fromStacks(new ItemStack(ProjectEXItems.COLLECTOR, 1, prevTier.ordinal())));
					list.add(matterIngredient);
					r.register(new ShapelessRecipes("projectex:collector", new ItemStack(ProjectEXItems.COLLECTOR, 1, tier.ordinal()), list).setRegistryName("collector/" + tier.getName()));
				}

				if (ProjectEXConfig.items.relays)
				{
					NonNullList<Ingredient> list = NonNullList.create();
					list.add(Ingredient.fromStacks(new ItemStack(ProjectEXItems.RELAY, 1, prevTier.ordinal())));
					list.add(matterIngredient);
					r.register(new ShapelessRecipes("projectex:relay", new ItemStack(ProjectEXItems.RELAY, 1, tier.ordinal()), list).setRegistryName("relay/" + tier.getName()));
				}
			}

			prevTier = tier;
		}

		for (EnumFuel fuel : EnumFuel.VALUES) {
			if (prevFuel != null) {
				Ingredient preFuelIngredient = Ingredient.fromStacks(prevFuel.get()),
						fuelIngredient = Ingredient.fromStacks(fuel.get());

				NonNullList<Ingredient> list = NonNullList.create();
				list.add(stone);
				list.add(preFuelIngredient);
				list.add(preFuelIngredient);
				list.add(preFuelIngredient);
				list.add(preFuelIngredient);
				r.register(new ShapelessRecipes("projectex:fuel", fuel.get(), list)
						.setRegistryName("fuel/" + prevFuel.getName() + "_to_" + fuel.getName())
				);

				list = NonNullList.create();
				list.add(stone);
				list.add(fuelIngredient);
				ItemStack res = prevFuel.get().copy();
				res.setCount(4);
				r.register(new ShapelessRecipes("projectex:fuel", res, list)
						.setRegistryName("fuel/" + fuel.getName() + "_to_" + prevFuel.getName())
				);
			}
			OreDictionary.registerOre("collectorFuels", fuel.get());
			prevFuel = fuel;
		}
	}
}