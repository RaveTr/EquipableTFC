package com.mememan.twilightforesttweaks.util;

import java.util.Locale;

import com.mememan.twilightforesttweaks.TwilightForestTweaks;

import net.minecraft.util.ResourceLocation;

public final class NameUtil {
	
	private NameUtil() {
		throw new IllegalAccessError("Attempted to instantiate a Utility Class!");
	}
	
	/**
	 * Returns a version of the input String in which the first letter is capitalized.
	 * @param targetString The String to capitalize the first letter of.
	 * @return A version of the input String in which the first letter is capitalized.
	 */
	public static String capitalizeFirstLetter(String targetString) {
		return targetString.replaceAll(targetString.substring(0, 1), targetString.substring(0, 1).toUpperCase(Locale.ROOT));
	}
	
	/**
	 * Prefixes {@linkplain TwilightForestTweaks#MODID Twilight Forest Tweaks' modid} onto the specified path
	 * @param path The target path.
	 * @return A new {@link ResourceLocation} in the TFT namespace.
	 */
	public static ResourceLocation prefix(String path) {
		return prefix(TwilightForestTweaks.MODID, path);
	}
	
	/**
	 * Prefixes the specified modid onto the specified path.
	 * @param modid The modid to prefix onto the path.
	 * @param path The target path.
	 * @return A new {@link ResourceLocation} with the specified modid and path.
	 */
	public static ResourceLocation prefix(String modid, String path) {
		return new ResourceLocation(modid, path);
	}
}