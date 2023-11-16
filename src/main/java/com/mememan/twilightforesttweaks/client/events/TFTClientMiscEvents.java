package com.mememan.twilightforesttweaks.client.events;

import com.mememan.twilightforesttweaks.manager.TFTConfigManager;
import com.mememan.twilightforesttweaks.util.NameUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import twilightforest.item.CuriosCharmItem;

public class TFTClientMiscEvents {
	
	@SubscribeEvent
	public static void onItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack targetStack = event.getItemStack();
		
		if (targetStack != null && !targetStack.isEmpty() && targetStack.getItem() instanceof CuriosCharmItem) {
			event.getToolTip().add(new StringTextComponent("Slot: ").withStyle(TextFormatting.GOLD).append(new StringTextComponent(NameUtil.capitalizeFirstLetter(TFTConfigManager.MAIN_COMMON.validSlotIdentifier.get().toLowerCase())).withStyle(TextFormatting.YELLOW))); // Hardcoded cuz :shrug:
		}
	}
}