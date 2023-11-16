package com.mememan.twilightforesttweaks.common.registry;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public class TFTTags {
	
	public static class Items {
		public static final INamedTag<Item> RETAIN_ON_DEATH = createItemTag("retain_on_death");
	}

	public static INamedTag<Item> createItemTag(String tagName) {
		return ItemTags.bind(tagName);
	}
}
