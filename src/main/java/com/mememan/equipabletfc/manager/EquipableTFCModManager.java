package com.mememan.equipabletfc.manager;

import net.minecraftforge.eventbus.api.IEventBus;

public class EquipableTFCModManager {
	
	public static final void registerAll(IEventBus modBus, IEventBus forgeBus) {
		EquipableTFCConfigManager.registerConfigs();
		EquipableTFCEventManager.registerEvents(modBus, forgeBus);
	}
}
