package com.mememan.twilightforesttweaks.common.integration.curios;

import com.mememan.twilightforesttweaks.manager.TFTConfigManager;
import com.mememan.twilightforesttweaks.util.CompatUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;
import twilightforest.item.CuriosCharmItem;

public class CuriosCompatEvents {

	@SubscribeEvent
	public static void onDropRulesEvent(DropRulesEvent event) {
		Entity targetEntity = event.getEntity();

		if (targetEntity instanceof PlayerEntity) {			
			PlayerEntity targetPlayer = (PlayerEntity) targetEntity;
			CompoundNBT persistentPlayerData = CompatUtil.getPersistentPlayerData(targetPlayer);
			
			if (CompatUtil.validateEntityForCharmUse(targetPlayer) && persistentPlayerData.contains(CuriosCompatConstants.CHARM_INV_TAG) && !persistentPlayerData.getList(CuriosCompatConstants.CHARM_INV_TAG, 10).isEmpty()) {
				CuriosApi.getCuriosHelper().getEquippedCurios(targetPlayer).ifPresent(modifiable -> {
					for (int curSlot = 0; curSlot < modifiable.getSlots(); curSlot++) {
						final int curSlotFinalized = curSlot;
						
						event.addOverride(targetStack -> targetStack == modifiable.getStackInSlot(curSlotFinalized), DropRule.ALWAYS_KEEP);
					}
				});
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		LivingEntity targetDeadEntity = event.getEntityLiving();
		
		if (CompatUtil.validateEntityForCharmUse(targetDeadEntity)) {
			PlayerEntity targetDeadPlayer = (PlayerEntity) targetDeadEntity;
			
			if (CompatUtil.handleCOLCharmCurios(targetDeadPlayer)) event.setCanceled(true);
			if (!targetDeadEntity.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
				CompatUtil.handleCOKCharmCurios(targetDeadPlayer, false);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		PlayerEntity targetPlayer = event.getPlayer();
		
		if (targetPlayer instanceof ServerPlayerEntity) {
			ServerPlayerEntity targetServerPlayer = (ServerPlayerEntity) targetPlayer;
			
			if (!event.isEndConquered()) CompatUtil.handleCOKCharmCurios(targetServerPlayer, true);
		}
	}

	@SubscribeEvent
	public static void onCurioEquipEvent(CurioEquipEvent event) {
		ItemStack targetStack = event.getStack();
		SlotContext targetSlot = event.getSlotContext();
		LivingEntity owner = targetSlot.getWearer();

		if (targetStack != null && owner != null) {
			if (owner.isAlive() && !targetStack.isEmpty() && (targetSlot.getIdentifier().equalsIgnoreCase(TFTConfigManager.MAIN_COMMON.validSlotIdentifier.get()) || TFTConfigManager.MAIN_COMMON.validSlotIdentifier.get().equalsIgnoreCase("Any")) && targetStack.getItem() instanceof CuriosCharmItem && event.hasResult()) event.setResult(Result.ALLOW);
		}
	}
}
