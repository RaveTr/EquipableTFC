package com.mememan.twilightforesttweaks.manager;

import net.minecraftforge.eventbus.api.IEventBus;

public class TFTModManager {
	
	public static final void registerAll(IEventBus modBus, IEventBus forgeBus) {
		TFTConfigManager.registerConfigs();
		TFTEventManager.registerEvents(modBus, forgeBus);
		TFTIntegrationManager.registerCompat(modBus, forgeBus);
	}
}