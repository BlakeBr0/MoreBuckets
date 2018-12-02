package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.morebuckets.lib.ModChecker;

public class ModItems {

	public static final ItemMoreBucket QUARTZ_BUCKET = new ItemMoreBucket("quartz_bucket", 2000);
	public static final ItemMoreBucket OBSIDIAN_BUCKET = new ItemMoreBucket("obsidian_bucket", 3000);
	public static final ItemMoreBucket GOLD_BUCKET = new ItemMoreBucket("golden_bucket", 4000);
	public static final ItemMoreBucket EMERALD_BUCKET = new ItemMoreBucket("emerald_bucket", 8000);
	public static final ItemMoreBucket DIAMOND_BUCKET = new ItemMoreBucket("diamond_bucket", 10000);
	
	public static final ItemMoreBucket ALUMINUM_BUCKET = new ItemMoreBucket("aluminum_bucket", 1000);
	public static final ItemMoreBucket COPPER_BUCKET = new ItemMoreBucket("copper_bucket", 1000);
	public static final ItemMoreBucket TIN_BUCKET = new ItemMoreBucket("tin_bucket", 1000);
	public static final ItemMoreBucket RUBBER_BUCKET = new ItemMoreBucket("rubber_bucket", 1000);
	public static final ItemMoreBucket SILVER_BUCKET = new ItemMoreBucket("silver_bucket", 3000);
	public static final ItemMoreBucket BRONZE_BUCKET = new ItemMoreBucket("bronze_bucket", 4000);
	public static final ItemMoreBucket STEEL_BUCKET = new ItemMoreBucket("steel_bucket", 6000);

	public static final ItemMoreBucket INFERIUM_BUCKET = new ItemMoreBucket("inferium_bucket", 2000, ModChecker.MYSTICAL_AGRICULTURE);
	public static final ItemMoreBucket PRUDENTIUM_BUCKET = new ItemMoreBucket("prudentium_bucket", 4000, ModChecker.MYSTICAL_AGRICULTURE);
	public static final ItemMoreBucket INTERMEDIUM_BUCKET = new ItemMoreBucket("intermedium_bucket", 8000, ModChecker.MYSTICAL_AGRICULTURE);
	public static final ItemMoreBucket SUPERIUM_BUCKET = new ItemMoreBucket("superium_bucket", 16000, ModChecker.MYSTICAL_AGRICULTURE);
	public static final ItemMoreBucket SUPREMIUM_BUCKET = new ItemMoreBucket("supremium_bucket", 32000, ModChecker.MYSTICAL_AGRICULTURE);
	public static final ItemMoreBucket INSANIUM_BUCKET = new ItemMoreBucket("insanium_bucket", 64000, ModChecker.MYSTICAL_AGRADDITIONS);
	
	public static final ItemMoreBucket ARDITE_BUCKET = new ItemMoreBucket("ardite_bucket", 9000, ModChecker.TINKERS_CONSTRUCT);
	public static final ItemMoreBucket COBALT_BUCKET = new ItemMoreBucket("cobalt_bucket", 9000, ModChecker.TINKERS_CONSTRUCT);
	public static final ItemMoreBucket MANYULLYN_BUCKET = new ItemMoreBucket("manyullyn_bucket", 16000, ModChecker.TINKERS_CONSTRUCT);
	
	public static void init(ModRegistry registry) {
		registry.register(QUARTZ_BUCKET, "quartz_bucket");
		registry.register(OBSIDIAN_BUCKET, "obsidian_bucket");
		registry.register(GOLD_BUCKET, "golden_bucket");
		registry.register(EMERALD_BUCKET, "emerald_bucket");
		registry.register(DIAMOND_BUCKET, "diamond_bucket");
		
		registry.register(ALUMINUM_BUCKET, "aluminum_bucket");
		registry.register(COPPER_BUCKET, "copper_bucket");
		registry.register(TIN_BUCKET, "tin_bucket");
		registry.register(RUBBER_BUCKET, "rubber_bucket");
		registry.register(SILVER_BUCKET, "silver_bucket");
		registry.register(BRONZE_BUCKET, "bronze_bucket");
		registry.register(STEEL_BUCKET, "steel_bucket");
		
		registry.register(INFERIUM_BUCKET, "inferium_bucket");
		registry.register(PRUDENTIUM_BUCKET, "prudentium_bucket");
		registry.register(INTERMEDIUM_BUCKET, "intermedium_bucket");
		registry.register(SUPERIUM_BUCKET, "superium_bucket");
		registry.register(SUPREMIUM_BUCKET, "supremium_bucket");
		registry.register(INSANIUM_BUCKET, "insanium_bucket");
		
		registry.register(ARDITE_BUCKET, "ardite_bucket");
		registry.register(COBALT_BUCKET, "cobalt_bucket");
		registry.register(MANYULLYN_BUCKET, "manyullyn_bucket");
	}
}
