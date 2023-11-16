package com.mememan.twilightforesttweaks.mixins;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.mememan.twilightforesttweaks.util.CompatUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.fml.ModList;
import twilightforest.item.CuriosCharmItem;

@Mixin(IForgeItem.class)
public interface IForgeItemMixin {
	
	@Nullable
	@Overwrite(remap = false)
	default ICapabilityProvider initCapabilities(ItemStack targetStack, @Nullable CompoundNBT capData) {
		if (targetStack.getItem() instanceof CuriosCharmItem && ModList.get().isLoaded("curios")) return CompatUtil.handleCuriosCompat(targetStack);
		else return null;
	}
}