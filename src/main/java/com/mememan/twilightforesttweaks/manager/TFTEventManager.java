package com.mememan.twilightforesttweaks.manager;

import com.mememan.twilightforesttweaks.client.events.TFTClientMiscEvents;
import com.mememan.twilightforesttweaks.client.events.TFTClientSetupEvents;
import com.mememan.twilightforesttweaks.common.events.TFTCommonMiscEvents;
import com.mememan.twilightforesttweaks.common.events.TFTCommonSetupEvents;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class TFTEventManager {
	
	protected static void registerEvents(IEventBus modBus, IEventBus forgeBus) {
		registerClientEvents(modBus, forgeBus);
		registerCommonEvents(modBus, forgeBus);
	}
	
	private static void registerClientEvents(IEventBus modBus, IEventBus forgeBus) {
		if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
			modBus.register(TFTClientSetupEvents.class);
			
			forgeBus.register(TFTClientMiscEvents.class);
		}
	}
	
	private static void registerCommonEvents(IEventBus modBus, IEventBus forgeBus) {
		modBus.register(TFTCommonSetupEvents.class);
		
		forgeBus.register(TFTCommonMiscEvents.class);
	}
}