package com.mememan.equipabletfc.manager;

import org.apache.commons.lang3.tuple.Pair;

import com.mememan.equipabletfc.EquipableTFC;
import com.mememan.equipabletfc.config.common.EquipableTFCCommonConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class EquipableTFCConfigManager {
	public static final ForgeConfigSpec MAIN_COMMON_SPEC;
	public static final EquipableTFCCommonConfig MAIN_COMMON;
	
	static {
		final Pair<EquipableTFCCommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(EquipableTFCCommonConfig::new);
		
		MAIN_COMMON_SPEC = commonSpecPair.getRight();
		MAIN_COMMON = commonSpecPair.getLeft();
	}
	
	protected static void registerConfigs() {
		registerCommonConfig();
	}
	
	private static void registerCommonConfig() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MAIN_COMMON_SPEC, EquipableTFC.MODID + "-common.toml");
	}
}
