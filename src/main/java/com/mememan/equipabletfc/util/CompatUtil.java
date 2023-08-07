package com.mememan.equipabletfc.util;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import twilightforest.TFEventListener;
import twilightforest.TFSounds;
import twilightforest.entity.CharmEffectEntity;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.util.TFItemStackUtils;

public class CompatUtil { //TODO if-else hell impl (too lazy to do a map-based function impl) :dies:
	
	private CompatUtil() {
		throw new IllegalAccessError("Attempted to instantiate a Utility Class!");
	}

	/**
	 * Refactored impl of {@link TFEventListener#charmOfLife(PlayerEntity)} which considers Curios slots.
	 * @param targetPlayer The player to check and handle the Charm of Life behaviour for in their Curios slots.
	 * @return {@code true} if a Charm of Life is found and handled for the specified {@linkplain PlayerEntity player}, else returns {@code false}.
	 */
	public static boolean handleCOLCharmCurios(PlayerEntity targetPlayer) {
		boolean hasCOL1 = consumeCuriosItem(targetPlayer, TFItems.charm_of_life_1.get());
		boolean hasCOL2 = consumeCuriosItem(targetPlayer, TFItems.charm_of_life_2.get());

		if (hasCOL1 || hasCOL2) {
			Item curCOL = hasCOL1 ? TFItems.charm_of_life_1.get() : TFItems.charm_of_life_2.get();

			if (hasCOL1) {
				targetPlayer.setHealth(8.0F);
				targetPlayer.addEffect(new EffectInstance(Effects.REGENERATION, 100, 0));
			} else if (hasCOL2) {
				targetPlayer.setHealth((float) targetPlayer.getAttribute(Attributes.MAX_HEALTH).getBaseValue());

				targetPlayer.addEffect(new EffectInstance(Effects.REGENERATION, 600, 3));
				targetPlayer.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 600, 0));
				targetPlayer.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600, 0));
			}

			CharmEffectEntity charmEffect = new CharmEffectEntity(TFEntities.charm_effect, targetPlayer.level, targetPlayer, curCOL);

			if (!targetPlayer.level.isClientSide) targetPlayer.level.addFreshEntity(charmEffect);
			targetPlayer.level.playSound(null, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), TFSounds.CHARM_LIFE, targetPlayer.getSoundSource(), 1.0F, 1.0F);
			return true;
		} else return false;
	}

	/**
	 * Method which delegates to {@link TFItemStackUtils#consumeInventoryItem(NonNullList, Item)}, using its own {@link NonNullList} of Curios 
	 * {@linkplain ItemStack Item Stacks}.
	 * @param ownerPlayer The player from which to get the Curios {@link ItemStack}.
	 * @param targetItem The item in the {@link NonNullList} of Curios {@linkplain ItemStack Item Stacks} to check against.
	 * @return {@code true} if a match in the {@link NonNullList} of Curios {@linkplain ItemStack Item Stacks} is found (consuming the found 
	 * stack), else returns {@code false}.
	 */
	public static boolean consumeCuriosItem(PlayerEntity ownerPlayer, Item targetItem) {
		LazyOptional<IItemHandlerModifiable> curiosHandler = CuriosApi.getCuriosHelper().getEquippedCurios(ownerPlayer);
		NonNullList<ItemStack> curioStacks = NonNullList.create();

		if (curiosHandler.isPresent()) {
			IItemHandlerModifiable curioHandler = curiosHandler.orElseThrow(IllegalStateException::new);

			for (int curSlot = 0; curSlot < curioHandler.getSlots(); curSlot++) {
				if (!curioHandler.getStackInSlot(curSlot).isEmpty()) curioStacks.add(curioHandler.getStackInSlot(curSlot));
			}
		}

		return TFItemStackUtils.consumeInventoryItem(curioStacks, targetItem);
	}
}
