package com.mememan.twilightforesttweaks.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mememan.twilightforesttweaks.common.integration.curios.CuriosCompatConstants;
import com.mememan.twilightforesttweaks.common.registry.TFTTags;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.capability.CurioItemCapability;
import top.theillusivec4.curios.common.capability.CurioItemCapability.Provider;
import twilightforest.TFEventListener;
import twilightforest.TFSounds;
import twilightforest.entity.CharmEffectEntity;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.util.TFItemStackUtils;

public final class CompatUtil { //TODO if-else hell impl (too lazy to do a map-based function impl) :dies:

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
				targetPlayer.setHealth((float) targetPlayer.getAttributeBaseValue(Attributes.MAX_HEALTH));

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
	 * Refactored impl of {@link TFEventListener#charmOfKeeping(PlayerEntity)} which considers Curios slots.
	 * @param targetPlayer The player to check and handle the Charm of Keeping behaviour for in their Curios slots.
	 * @param shouldPerformRespawnOperations Whether the point at which this method is called should instead account for restoring a player's items 
	 * rather than storing them.
	 */
	public static void handleCOKCharmCurios(PlayerEntity targetPlayer, boolean shouldPerformRespawnOperations) {
		boolean hasCOK3 = consumeCuriosItem(targetPlayer, TFItems.charm_of_keeping_3.get());
		boolean hasCOK2 = hasCOK3 || consumeCuriosItem(targetPlayer, TFItems.charm_of_keeping_2.get());
		boolean hasCOK1 = hasCOK2 || consumeCuriosItem(targetPlayer, TFItems.charm_of_keeping_1.get());
		PlayerInventory targetPlayerInventory = targetPlayer.inventory;
		PlayerInventory clonedFakeInventory = new PlayerInventory(null);
		ListNBT persistentInventoryNBT = new ListNBT();
		Item usedCOK = hasCOK1 ? TFItems.charm_of_keeping_1.get() : hasCOK2 ? TFItems.charm_of_keeping_2.get() : hasCOK3 ? TFItems.charm_of_keeping_3.get() : null; // Hardcoded +1 (Laziness + 1 too :p)
		NonNullList<ItemStack> hotbarOnlyItems = targetPlayerInventory.items.stream()
				.filter(targetStack -> targetPlayerInventory.items.indexOf(targetStack) < 10)
				.collect(Collectors.toCollection(NonNullList::create));
		
		if (!shouldPerformRespawnOperations) {
			// Only retains offhand, armor, and selected item in the player's inventory
			if (hasCOK1) {
				restoreItemsFrom(clonedFakeInventory, targetPlayerInventory.armor, null);
				restoreItemsFrom(clonedFakeInventory, targetPlayerInventory.offhand, null);
				
				int curSelectedStack = targetPlayerInventory.selected;
				
				if (PlayerInventory.isHotbarSlot(curSelectedStack)) {
					clonedFakeInventory.setItem(curSelectedStack, targetPlayerInventory.getItem(curSelectedStack));
					targetPlayerInventory.setItem(curSelectedStack, ItemStack.EMPTY);
				}
				
				targetPlayerInventory.armor.forEach(targetStack -> targetPlayerInventory.armor.set(targetPlayerInventory.armor.indexOf(targetStack), ItemStack.EMPTY));
				targetPlayerInventory.offhand.forEach(targetStack -> targetPlayerInventory.offhand.set(targetPlayerInventory.offhand.indexOf(targetStack), ItemStack.EMPTY));
			}
			
			if (hasCOK2) {
				restoreItemsFrom(clonedFakeInventory, hotbarOnlyItems, null); // Retains tier 1 + hotbar slot items
				hotbarOnlyItems.forEach(targetStack -> hotbarOnlyItems.set(hotbarOnlyItems.indexOf(targetStack), ItemStack.EMPTY));
			} else if (hasCOK3) restoreItemsFrom(clonedFakeInventory, targetPlayerInventory.items, null); // Retains whole inventory (tier 2 + rest of inventory)
			
	/*		getAllDefaultVanillaCompartments(targetPlayerInventory).forEach(targetStack -> {
				if (targetStack.getItem().is(TFTTags.Items.RETAIN_ON_DEATH)) {
					clonedFakeInventory.setItem(targetPlayerInventory.findSlotMatchingItem(targetStack), targetStack);
					targetPlayerInventory.setItem(targetPlayerInventory.findSlotMatchingItem(targetStack), ItemStack.EMPTY);
				}
			});*/
			
			if (!clonedFakeInventory.isEmpty()) {
				clonedFakeInventory.save(persistentInventoryNBT);
				getPersistentPlayerData(targetPlayer).put(CuriosCompatConstants.CHARM_INV_TAG, persistentInventoryNBT);
			}
		} else {
			CompoundNBT persistentNBTData = getPersistentPlayerData(targetPlayer);
			
			if (targetPlayer != null && !targetPlayer.level.isClientSide && persistentNBTData.contains(CuriosCompatConstants.CHARM_INV_TAG)) {
				ListNBT invData = persistentNBTData.getList(CuriosCompatConstants.CHARM_INV_TAG, 10);
				
				loadInventoryFrom(invData, targetPlayer.inventory);
				getPersistentPlayerData(targetPlayer).getList(CuriosCompatConstants.CHARM_INV_TAG, 10).clear();
				getPersistentPlayerData(targetPlayer).remove(CuriosCompatConstants.CHARM_INV_TAG);
			}
			
			if (usedCOK != null) {
				CharmEffectEntity mainCOKEffect = new CharmEffectEntity(TFEntities.charm_effect, targetPlayer.level, targetPlayer, usedCOK);
				targetPlayer.level.addFreshEntity(mainCOKEffect);
				
				CharmEffectEntity movingCOKEffect = new CharmEffectEntity(TFEntities.charm_effect, targetPlayer.level, targetPlayer, usedCOK);
				movingCOKEffect.offset = (float) Math.PI;
				
				targetPlayer.level.addFreshEntity(movingCOKEffect);
				targetPlayer.level.playSound(null, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), TFSounds.CHARM_KEEP, targetPlayer.getSoundSource(), 1.5F, 1.0F);
			}
		}
	}

	/**
	 * Method which delegates to {@link TFItemStackUtils#consumeInventoryItem(NonNullList, Item)}, using its own {@link NonNullList} of Curios 
	 * {@linkplain ItemStack ItemStacks}.
	 * @param ownerPlayer The player from which to get the Curios {@link ItemStack}.
	 * @param targetItem The item in the {@link NonNullList} of Curios {@linkplain ItemStack ItemStacks} to check against.
	 * @return {@code true} if a match in the {@link NonNullList} of Curios {@linkplain ItemStack ItemStacks} is found (consuming the found 
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

	/**
	 * Method to validate a {@link LivingEntity} for charm use by checking if it's a real {@linkplain PlayerEntity player}.
	 * @param targetEntity The {@link LivingEntity} to validate for charm use.
	 * @return {@code true} if the specified {@link LivingEntity} is a real {@link PlayerEntity} and is present on the server, else returns 
	 * {@code false}.
	 */
	public static boolean validateEntityForCharmUse(LivingEntity targetEntity) {
		return !(targetEntity == null || (targetEntity != null && (targetEntity.level.isClientSide || !(targetEntity instanceof PlayerEntity) || targetEntity instanceof FakePlayer)));
	}

	/**
	 * Attaches a {@linkplain Provider CurioItemCapability Provider} which slightly modifies the behaviour of the specified {@link ItemStack} 
	 * as a Curio.
	 * @param targetStack The {@link ItemStack} to add the custom {@linkplain Provider CurioItemCapability Provider} to.
	 * @return A new {@linkplain Provider CurioItemCapability Provider} containing behavioural modifications added to the specified {@link ItemStack}.
	 */
	public static ICapabilityProvider handleCuriosCompat(ItemStack targetStack) {
		return CurioItemCapability.createProvider(new ICurio() {
			@Override
			public SoundInfo getEquipSound(SlotContext slotContext) {
				return new SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
			}

			@Override
			public boolean canEquipFromUse(SlotContext slotContext) {
				return true;
			}
		});
	}

	/**
	 * Ported method from newer Twilight Forest versions, gets a {@linkplain PlayerEntity player's} {@linkplain PlayerEntity#PERSISTED_NBT_TAG Persisted NBT Tag}, 
	 * or stores it if it isn't stored in the {@linkplain PlayerEntity target player's} {@linkplain PlayerEntity#getPersistentData() persistent data}.
	 * @param targetPlayer The target {@link PlayerEntity} to get the {@linkplain PlayerEntity#getPersistentData() persistent data} of.
	 * @return The {@linkplain PlayerEntity target player's} {@linkplain PlayerEntity#getPersistentData() persistent data}.
	 */
	public static CompoundNBT getPersistentPlayerData(PlayerEntity targetPlayer) {
		if (!targetPlayer.getPersistentData().contains(PlayerEntity.PERSISTED_NBT_TAG)) targetPlayer.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());

		return targetPlayer.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
	}
	
	/**
	 * A modified version of {@link PlayerInventory#replaceWith(PlayerInventory)} that only takes the specified {@linkplain List list} of {@linkplain ItemStack ItemStacks} 
	 * into account when attempting to restore items to the specified {@linkplain PlayerEntity PlayerEntity's} {@linkplain PlayerInventory inventory}.
	 * @param targetPlayer The player whose inventory should be restored/modified.
	 * @param itemsToRestore The {@linkplain ItemStack ItemStacks} to place in the target {@link PlayerInventory}. 
	 * Takes ordering into account, which means if the index of a specified {@link ItemStack} in that {@link List} matches that of an offhand item/the offhand's index, 
	 * then only the specified offhand slots will be replaced.
	 * @param newSelectedStack Optional {@link ItemStack} to make the specified {@link PlayerInventory} select and carry after the replace operation is 
	 * finished.
	 */
	public static void restoreItemsFrom(PlayerInventory targetPlayerInventory, List<ItemStack> itemsToRestore, @Nullable ItemStack newSelectedStack) {
		if (targetPlayerInventory == null || itemsToRestore.isEmpty()) return;

		for (int curSlot = 0; curSlot < targetPlayerInventory.getContainerSize(); curSlot++) {
			if (curSlot < itemsToRestore.size()) targetPlayerInventory.setItem(curSlot, itemsToRestore.get(curSlot).copy());
		}

		if (newSelectedStack != null && !newSelectedStack.isEmpty() && targetPlayerInventory.contains(newSelectedStack) && PlayerInventory.isHotbarSlot(targetPlayerInventory.findSlotMatchingItem(newSelectedStack))) {
			targetPlayerInventory.selected = targetPlayerInventory.findSlotMatchingItem(newSelectedStack);
			targetPlayerInventory.setCarried(newSelectedStack);
		}
	}

	/**
	 * A method which delegates to {@link #restoreItemsFrom(PlayerInventory, List, ItemStack)}, but instead only takes the specified {@linkplain List list} of {@linkplain ItemStack ItemStacks} 
	 * into account when attempting to restore items to the specified {@linkplain PlayerEntity PlayerEntity's} {@linkplain PlayerInventory inventory}.
	 * @param targetPlayer The player whose inventory should be restored/modified.
	 * @param itemsToRestore The {@linkplain ItemStack ItemStacks} to place in the {@linkplain PlayerEntity PlayerEntity's} {@linkplain PlayerInventory inventory}. 
	 * Takes ordering into account, which means if the index of a specified {@link ItemStack} in that {@link List} matches that of an offhand item/the offhand's index, 
	 * then only the specified offhand slots will be replaced.
	 * @param newSelectedStack Optional {@link ItemStack} to make the specified {@link PlayerEntity} hold after the replace operation is 
	 * finished.
	 */
	public static void restoreItemsFrom(PlayerEntity targetPlayer, List<ItemStack> itemsToRestore, @Nullable ItemStack newSelectedStack) {
		restoreItemsFrom(targetPlayer.inventory, itemsToRestore, newSelectedStack);
	}

	/**
	 * Creates a new {@link PlayerInventory} unbound to the specified {@link PlayerEntity} and populates it with the specified {@linkplain PlayerEntity PlayerEntity's} 
	 * current {@link PlayerInventory}.
	 * @param targetPlayer The target {@link PlayerEntity} from which inventory contents/settings should be copied
	 * @return A new {@link PlayerInventory} with the contents and settings of the specified {@linkplain PlayerEntity PlayerEntity's} {@linkplain PlayerInventory inventory}.
	 */
	public static PlayerInventory copyContentsFrom(PlayerEntity targetPlayer) {
		PlayerInventory targetPlayerInventory = targetPlayer.inventory;
		PlayerInventory newPlayerInventory = new PlayerInventory(null);

		newPlayerInventory.replaceWith(targetPlayerInventory);

		return newPlayerInventory;
	}
	
	/**
	 * Gets the entire (vanilla) {@linkplain NonNullList list} of {@linkplain ItemStack ItemStacks} from the specified {@link PlayerInventory} 
	 * in the order (hotbar -> inventory -> armor -> offhand), just like the {@linkplain PlayerInventory#compartments immutable list of compartments in PlayerInventory}.
	 * @param targetInventory The {@link PlayerInventory} from which to get all inventory compartments.
	 * @return A {@link NonNullList} containing all vanilla inventory compartments in the aforementioned order.
	 */
	public static NonNullList<ItemStack> getAllDefaultVanillaCompartments(PlayerInventory targetInventory) {
		return Stream.of(targetInventory.items, targetInventory.armor, targetInventory.offhand).flatMap(Collection::stream).collect(Collectors.toCollection(NonNullList::create));
	}
	
	/**
	 * Refactored version of both {@link PlayerInventory#load(ListNBT)} and the newer {@code loadNoClear} method in 1.17+ Twilight Forest versions. 
	 * Safely attempts to load an inventory from a {@link ListNBT} into the specified {@link PlayerInventory}.
	 * @param invData The {@link ListNBT} data from which an inventory should be loaded.
	 * @param targetInventory The target {@link PlayerInventory} into which the parsed inventory contents should be loaded.
	 */
	public static void loadInventoryFrom(ListNBT invData, PlayerInventory targetInventory) {
		if (invData.isEmpty() || targetInventory == null) return;
		
		ObjectArrayList<ItemStack> blockedParsedStacks = new ObjectArrayList<ItemStack>();
		
		for (int i = 0; i < invData.size(); ++i) {
			CompoundNBT slotData = invData.getCompound(i);
			int targetSlot = slotData.getByte("Slot") & 255;
			ItemStack targetParsedStack = ItemStack.of(slotData);
			
			if (!targetParsedStack.isEmpty()) {
				if (targetSlot >= 0 && targetSlot < targetInventory.items.size()) {
					if (targetInventory.items.get(targetSlot).isEmpty()) targetInventory.items.set(targetSlot, targetParsedStack);
					else if (!blockedParsedStacks.contains(targetParsedStack)) blockedParsedStacks.add(targetParsedStack);
				} else if (targetSlot >= 100 && targetSlot < targetInventory.armor.size() + 100) {
					if (targetInventory.armor.get(targetSlot).isEmpty()) targetInventory.armor.set(targetSlot - 100, targetParsedStack);
					else if (!blockedParsedStacks.contains(targetParsedStack)) blockedParsedStacks.add(targetParsedStack);
				} else if (targetSlot >= 150 && targetSlot < targetInventory.offhand.size() + 150) {
					if (targetInventory.offhand.get(targetSlot).isEmpty()) targetInventory.offhand.set(targetSlot - 150, targetParsedStack);
					else if (!blockedParsedStacks.contains(targetParsedStack)) blockedParsedStacks.add(targetParsedStack);
				}
			}
		}
		
		if (!blockedParsedStacks.isEmpty()) blockedParsedStacks.stream().filter(targetStack -> !targetStack.isEmpty()).forEach(targetInventory::add);
	}
}
