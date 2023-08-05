package com.mememan.equipabletfc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mememan.equipabletfc.manager.EquipableTFCModManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EquipableTFC.MODID)
public class EquipableTFC {
	public static final String MODID = "equipabletfc";
    public static final Logger LOGGER = LogManager.getLogger();

    public EquipableTFC() {
    	IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    	IEventBus forgeBus = MinecraftForge.EVENT_BUS;
    	
    	if (modBus != null && forgeBus != null) EquipableTFCModManager.registerAll(modBus, forgeBus);
    }
}