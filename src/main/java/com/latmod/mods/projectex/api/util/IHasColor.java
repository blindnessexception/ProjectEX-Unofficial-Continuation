package com.latmod.mods.projectex.api.util;

import net.minecraft.item.EnumDyeColor;

import javax.annotation.Nonnull;

public interface IHasColor {
    @Nonnull
    @SuppressWarnings("unused")
    EnumDyeColor getColor();
}
