package com.mememan.twilightforesttweaks.config.common;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class TFTCommonConfig {
	public final ConfigValue<String> validSlotIdentifier;
	public final ConfigValue<List<? extends String>> blacklistedUncraftingItems;
	public final ConfigValue<List<? extends String>> blacklistedUncraftingModids;
	
	public TFTCommonConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Curios");
		
		builder.push("Slots");
		validSlotIdentifier = builder.comment("The slot identifier for the valid slot in which Twilight Forest charms can be equipped (E.G. 'Charm', 'Necklace', etc.). "
				+ "Case does not matter. Set this to 'Any' if you want all slots to be valid for all charms.")
				.define("Valid Curios Equipment Slot", "Charm");
		
		builder.pop(2);
		
		
		builder.push("Twilight Forest");
		
		builder.push("Uncrafting");
		blacklistedUncraftingItems = builder.comment("A list of items (alongside their modids) that should be blacklisted from being used \n"
				+ "in the Uncrafting Table. 'minecraft:netherite_block', for example.")
				.defineList("Blacklisted Uncrafting Items", () -> new ObjectArrayList<String>(), input -> input instanceof String);
		blacklistedUncraftingModids = builder.comment("A list of mods (using their modids) whose items should be blacklisted from being used \n"
				+ "in the Uncrafting Table. 'twilightforest', for example.")
				.defineList("Blacklisted Uncrafting Mods", () -> new ObjectArrayList<String>(), input -> input instanceof String);
		
		builder.pop(2);
	}
}
