package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.item.ProjectEXItems;
import moze_intel.projecte.gameObjs.ObjHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public enum EnumTier implements IStringSerializable
{
	BASIC("basic", () -> new ItemStack(Blocks.DIAMOND_BLOCK), ProjectEXConfig.tiers.basic, 0xD3D3D3),
	DARK("dark", () -> new ItemStack(ObjHandler.matter, 1, 0), ProjectEXConfig.tiers.dark, 0x000000),
	RED("red", () -> new ItemStack(ObjHandler.matter, 1, 1), ProjectEXConfig.tiers.red, 0xFF0000),
	MAGENTA("magenta", EnumMatter.MAGENTA, ProjectEXConfig.tiers.magenta, 0xFF00FF),
	PINK("pink", EnumMatter.PINK, ProjectEXConfig.tiers.pink, 0xFFC0CB),
	PURPLE("purple", EnumMatter.PURPLE, ProjectEXConfig.tiers.purple, 0x800080),
	VIOLET("violet", EnumMatter.VIOLET, ProjectEXConfig.tiers.violet, 0xEE82EE),
	BLUE("blue", EnumMatter.BLUE, ProjectEXConfig.tiers.blue, 0x0000FF),
	CYAN("cyan", EnumMatter.CYAN, ProjectEXConfig.tiers.cyan, 0x00FFFF),
	GREEN("green", EnumMatter.GREEN, ProjectEXConfig.tiers.green, 0x008000),
	LIME("lime", EnumMatter.LIME, ProjectEXConfig.tiers.lime, 0x00FF00),
	YELLOW("yellow", EnumMatter.YELLOW, ProjectEXConfig.tiers.yellow, 0xFFFF00),
	ORANGE("orange", EnumMatter.ORANGE, ProjectEXConfig.tiers.orange, 0xFFA500),
	WHITE("white", EnumMatter.WHITE, ProjectEXConfig.tiers.white, 0xFFFFFF),
	FADING("fading", EnumMatter.FADING, ProjectEXConfig.tiers.fading, 0xA9A9A9),
	FINAL("final", () -> new ItemStack(ProjectEXItems.FINAL_STAR_SHARD), ProjectEXConfig.tiers.final_tier);

	public static final EnumTier[] VALUES = values();

	public static EnumTier byMeta(int meta)
	{
		return meta < 0 || meta >= VALUES.length ? BASIC : VALUES[meta];
	}

	private final String name;
	public final Supplier<ItemStack> matter;
	public final ProjectEXConfig.BlockTier properties;
	private final int color;
	private float counter = 0.0f;

	EnumTier(String n, Supplier<ItemStack> ma, ProjectEXConfig.BlockTier b)
	{
		name = n;
		matter = ma;
		properties = b;
		color = 0;
	}

	EnumTier(String n, Supplier<ItemStack> ma, ProjectEXConfig.BlockTier b, int c)
	{
		name = n;
		matter = ma;
		properties = b;
		color = c;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public int getColor()
	{
		if (this.equals(FINAL))
		{
			counter += 0.01f;
			counter %= 2;
			return MathHelper.hsvToRGB(255 * zigzag(counter), 1, 1);
		}
		return color;
	}

	private float zigzag(float x) {
		return Math.abs((x % 2.0f) - 1.0f);
	}
}