package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.morebuckets.MoreBuckets;

import net.minecraftforge.common.MinecraftForge;

public class ModItems {

	public static ItemMoreBucket itemCopperBucket = new ItemMoreBucket("copper_bucket", 1000);
	public static ItemMoreBucket itemSilverBucket = new ItemMoreBucket("silver_bucket", 2000);
	
	public static void init() {
		final ModRegistry registry = MoreBuckets.REGISTRY;
		
		registry.register(itemCopperBucket, "copper_bucket");
		registry.register(itemSilverBucket, "silver_bucket");
	}
}
