package com.mememan.twilightforesttweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mememan.twilightforesttweaks.manager.TFTModManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TwilightForestTweaks.MODID)
public class TwilightForestTweaks {
	public static final String MODID = "twilightforesttweaks";
    public static final Logger LOGGER = LogManager.getLogger();

    public TwilightForestTweaks() {
    	IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    	IEventBus forgeBus = MinecraftForge.EVENT_BUS;
    	
    	if (modBus != null && forgeBus != null) TFTModManager.registerAll(modBus, forgeBus);
    }
}