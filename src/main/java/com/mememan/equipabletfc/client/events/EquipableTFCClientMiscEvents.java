package com.mememan.equipabletfc.client.events;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import twilightforest.item.CuriosCharmItem;

public class EquipableTFCClientMiscEvents {
	
	@SubscribeEvent
	public static void onItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack targetStack = event.getItemStack();
		
		if (targetStack != null && !targetStack.isEmpty() && targetStack.getItem() instanceof CuriosCharmItem) {
			event.getToolTip().add(new StringTextComponent("Slot: ").withStyle(TextFormatting.GOLD).append(new StringTextComponent("Charm").withStyle(TextFormatting.YELLOW))); // Hardcoded cuz :shrug:
		}
	}
}
