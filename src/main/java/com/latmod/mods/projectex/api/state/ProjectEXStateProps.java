package com.latmod.mods.projectex.api.state;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;

public abstract class ProjectEXStateProps {
    public static final IProperty<EnumDyeColor> BAG_COLOR = PropertyEnum.create("bag_color", EnumDyeColor.class);
}
