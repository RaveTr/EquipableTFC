package com.mememan.twilightforesttweaks.common.events;

import com.mememan.twilightforesttweaks.TwilightForestTweaks;
import com.mememan.twilightforesttweaks.common.network.packets.s2c.CommonConfigSyncPacket;
import com.mememan.twilightforesttweaks.manager.TFTConfigManager;
import com.mememan.twilightforesttweaks.manager.TFTNetworkManager;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TFTCommonSetupEvents {
	
	@SubscribeEvent
	public static void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
		TFTNetworkManager.registerPackets();
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(ModConfigEvent event) {
		if (event.getConfig().getModId().equalsIgnoreCase(TwilightForestTweaks.MODID)) {
			MinecraftServer curServer = ServerLifecycleHooks.getCurrentServer();
			
			if (curServer != null && curServer.isDedicatedServer()) {
				TFTNetworkManager.sendAllClientPacket(new CommonConfigSyncPacket(TFTConfigManager.MAIN_COMMON.blacklistedUncraftingItems));
				TFTNetworkManager.sendAllClientPacket(new CommonConfigSyncPacket(TFTConfigManager.MAIN_COMMON.blacklistedUncraftingModids));
			}
		}
	}
}