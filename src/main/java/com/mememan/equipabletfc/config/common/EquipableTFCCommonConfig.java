package com.mememan.equipabletfc.config.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class EquipableTFCCommonConfig {
	public final ConfigValue<String> validSlotIdentifier;
	
	public EquipableTFCCommonConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Slots");
		validSlotIdentifier = builder.comment("The slot identifier for the valid slot in which Twilight Forest charms can be equipped (E.G. 'Charm', 'Necklace', etc.). "
				+ "Case does not matter.")
				.define("Valid Curios Equipment Slot", "Charm");
		builder.pop();
	}

}
