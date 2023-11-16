package com.mememan.twilightforesttweaks.manager;

import com.mememan.twilightforesttweaks.common.integration.curios.CuriosCompat;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

public class TFTIntegrationManager {
	
	protected static void registerCompat(IEventBus modBus, IEventBus forgeBus) {
		if (ModList.get().isLoaded("curios")) CuriosCompat.register(forgeBus);
	}
}