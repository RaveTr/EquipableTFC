package com.mememan.equipabletfc.common.events;

import com.mememan.equipabletfc.util.CompatUtil;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import twilightforest.item.CuriosCharmItem;

public class EquipableTFCCommonMiscEvents {
	
	@SubscribeEvent
	public static void onCurioEquipEvent(CurioEquipEvent event) {
		ItemStack targetStack = event.getStack();
		SlotContext targetSlot = event.getSlotContext();
		LivingEntity owner = targetSlot.getWearer();
		
		if (targetStack != null && owner != null) {
			if (owner.isAlive() && !targetStack.isEmpty() && targetSlot.getIdentifier().equalsIgnoreCase("charm") && targetStack.getItem() instanceof CuriosCharmItem && event.hasResult()) event.setResult(Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		LivingEntity targetDeadEntity = event.getEntityLiving();
		
		if (targetDeadEntity != null && targetDeadEntity instanceof PlayerEntity) {
			if (CompatUtil.handleCOLCharmCurios((PlayerEntity) targetDeadEntity)) event.setCanceled(true);
		}
	}
}
