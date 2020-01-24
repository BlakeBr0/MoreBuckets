package com.blakebr0.morebuckets;

import com.blakebr0.morebuckets.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MBItemGroup extends ItemGroup {
	public MBItemGroup() {
		super(MoreBuckets.MOD_ID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModItems.DIAMOND_BUCKET.get());
	}
}
