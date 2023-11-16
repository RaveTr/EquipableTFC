package com.mememan.twilightforesttweaks.common.events;

import com.mememan.twilightforesttweaks.common.network.packets.s2c.CommonConfigSyncPacket;
import com.mememan.twilightforesttweaks.manager.TFTConfigManager;
import com.mememan.twilightforesttweaks.manager.TFTNetworkManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TFTCommonMiscEvents {
	
	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerLoggedInEvent event) {
		MinecraftServer curServer = ServerLifecycleHooks.getCurrentServer();
		Entity potentialTargetPlayer = event.getEntity();
		
		if (curServer != null && curServer.isDedicatedServer() && potentialTargetPlayer != null && potentialTargetPlayer instanceof ServerPlayerEntity) {
			ServerPlayerEntity potentialTargetServerPlayer = (ServerPlayerEntity) potentialTargetPlayer;
			
			TFTNetworkManager.sendPlayerClientPacket(potentialTargetServerPlayer, new CommonConfigSyncPacket(TFTConfigManager.MAIN_COMMON.blacklistedUncraftingItems));
			TFTNetworkManager.sendPlayerClientPacket(potentialTargetServerPlayer, new CommonConfigSyncPacket(TFTConfigManager.MAIN_COMMON.blacklistedUncraftingModids));
		}
	}
}