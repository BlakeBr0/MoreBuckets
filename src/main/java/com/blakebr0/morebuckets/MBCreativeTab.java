package com.blakebr0.morebuckets;

import com.blakebr0.morebuckets.init.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MBCreativeTab extends CreativeModeTab {
	public MBCreativeTab() {
		super(MoreBuckets.MOD_ID);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.DIAMOND_BUCKET.get());
	}
}
