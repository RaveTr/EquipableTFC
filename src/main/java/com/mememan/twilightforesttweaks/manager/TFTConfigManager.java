package com.mememan.twilightforesttweaks.manager;

import org.apache.commons.lang3.tuple.Pair;

import com.mememan.twilightforesttweaks.TwilightForestTweaks;
import com.mememan.twilightforesttweaks.config.common.TFTCommonConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class TFTConfigManager {
	public static final ForgeConfigSpec MAIN_COMMON_SPEC;
	public static final TFTCommonConfig MAIN_COMMON;
	
	static {
		final Pair<TFTCommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(TFTCommonConfig::new);
		
		MAIN_COMMON_SPEC = commonSpecPair.getRight();
		MAIN_COMMON = commonSpecPair.getLeft();
	}
	
	protected static void registerConfigs() {
		registerCommonConfig();
	}
	
	private static void registerCommonConfig() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MAIN_COMMON_SPEC, TwilightForestTweaks.MODID + "-common.toml");
	}
}