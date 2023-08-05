package com.mememan.equipabletfc.manager;

import com.mememan.equipabletfc.client.events.EquipableTFCClientMiscEvents;
import com.mememan.equipabletfc.common.events.EquipableTFCCommonMiscEvents;

import net.minecraftforge.eventbus.api.IEventBus;

public class EquipableTFCEventManager {
	
	protected static void registerEvents(IEventBus modBus, IEventBus forgeBus) {
		registerClientEvents(modBus, forgeBus);
		registerCommonEvents(modBus, forgeBus);
	}
	
	private static void registerCommonEvents(IEventBus modBus, IEventBus forgeBus) {
		forgeBus.register(EquipableTFCCommonMiscEvents.class);
	}
	
	private static void registerClientEvents(IEventBus modBus, IEventBus forgeBus) {
		forgeBus.register(EquipableTFCClientMiscEvents.class);
	}
}
