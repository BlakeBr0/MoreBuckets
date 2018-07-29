package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.registry.ModRegistry;

public class ModItems {

	public static ItemMoreBucket itemAluminumBucket = new ItemMoreBucket("aluminum_bucket", 1000);
	public static ItemMoreBucket itemCopperBucket = new ItemMoreBucket("copper_bucket", 1000);
	public static ItemMoreBucket itemTinBucket = new ItemMoreBucket("tin_bucket", 1000);
	public static ItemMoreBucket itemRubberBucket = new ItemMoreBucket("rubber_bucket", 1000);
	public static ItemMoreBucket itemSilverBucket = new ItemMoreBucket("silver_bucket", 2000);
	public static ItemMoreBucket itemQuartzBucket = new ItemMoreBucket("quartz_bucket", 2000);
	public static ItemMoreBucket itemObsidianBucket = new ItemMoreBucket("obsidian_bucket", 3000);
	public static ItemMoreBucket itemBronzeBucket = new ItemMoreBucket("bronze_bucket", 3000);
	public static ItemMoreBucket itemGoldBucket = new ItemMoreBucket("golden_bucket", 4000);
	public static ItemMoreBucket itemSteelBucket = new ItemMoreBucket("steel_bucket", 4000);
	public static ItemMoreBucket itemEmeraldBucket = new ItemMoreBucket("emerald_bucket", 5000);
	public static ItemMoreBucket itemDiamondBucket = new ItemMoreBucket("diamond_bucket", 6000);
	
	public static void init(ModRegistry registry) {	
		registry.register(itemAluminumBucket, "aluminum_bucket");
		registry.register(itemCopperBucket, "copper_bucket");
		registry.register(itemTinBucket, "tin_bucket");
		registry.register(itemRubberBucket, "rubber_bucket");
		registry.register(itemSilverBucket, "silver_bucket");
		registry.register(itemQuartzBucket, "quartz_bucket");
		registry.register(itemObsidianBucket, "obsidian_bucket");
		registry.register(itemBronzeBucket, "bronze_bucket");
		registry.register(itemGoldBucket, "golden_bucket");
		registry.register(itemSteelBucket, "steel_bucket");
		registry.register(itemEmeraldBucket, "emerald_bucket");
		registry.register(itemDiamondBucket, "diamond_bucket");
	}
}
