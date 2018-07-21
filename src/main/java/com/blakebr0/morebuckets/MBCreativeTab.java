package com.blakebr0.morebuckets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MBCreativeTab extends CreativeTabs {

	public MBCreativeTab() {
		super(MoreBuckets.MOD_ID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return ItemStack.EMPTY;
	}
}
