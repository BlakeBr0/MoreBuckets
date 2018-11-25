package com.blakebr0.morebuckets;

import com.blakebr0.morebuckets.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MBCreativeTab extends CreativeTabs {

	public MBCreativeTab() {
		super(MoreBuckets.MOD_ID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.DIAMOND_BUCKET);
	}
}
