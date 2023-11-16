package com.mememan.twilightforesttweaks.common.integration.curios;

import net.minecraftforge.eventbus.api.IEventBus;

public class CuriosCompat {
	
	public static void register(IEventBus forgeBus) {
		registerCuriosEvents(forgeBus);
	}
	
	private static void registerCuriosEvents(IEventBus forgeBus) {
		forgeBus.register(CuriosCompatEvents.class);
	}
}