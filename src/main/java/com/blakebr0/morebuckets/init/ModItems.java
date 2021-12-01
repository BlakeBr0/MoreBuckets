package com.blakebr0.morebuckets.init;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.blakebr0.morebuckets.MoreBuckets.CREATIVE_TAB;

public class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MoreBuckets.MOD_ID);

	public static final RegistryObject<Item> QUARTZ_BUCKET = register("quartz_bucket", () -> new MoreBucketItem(2000));
	public static final RegistryObject<Item> OBSIDIAN_BUCKET = register("obsidian_bucket", () -> new MoreBucketItem(3000));
	public static final RegistryObject<Item> GOLD_BUCKET = register("golden_bucket", () -> new MoreBucketItem(4000));
	public static final RegistryObject<Item> EMERALD_BUCKET = register("emerald_bucket", () -> new MoreBucketItem(8000));
	public static final RegistryObject<Item> DIAMOND_BUCKET = register("diamond_bucket", () -> new MoreBucketItem(10000));

	public static final RegistryObject<Item> ALUMINUM_BUCKET = register("aluminum_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<Item> COPPER_BUCKET = register("copper_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<Item> TIN_BUCKET = register("tin_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<Item> RUBBER_BUCKET = register("rubber_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<Item> SILVER_BUCKET = register("silver_bucket", () -> new MoreBucketItem(3000));
	public static final RegistryObject<Item> BRONZE_BUCKET = register("bronze_bucket", () -> new MoreBucketItem(4000));
	public static final RegistryObject<Item> STEEL_BUCKET = register("steel_bucket", () -> new MoreBucketItem(6000));

	public static final RegistryObject<Item> INFERIUM_BUCKET = register("inferium_bucket", () -> new MoreBucketItem(2000, "mysticalagriculturee"));
	public static final RegistryObject<Item> PRUDENTIUM_BUCKET = register("prudentium_bucket", () -> new MoreBucketItem(4000, "mysticalagriculture"));
	public static final RegistryObject<Item> TERTIUM_BUCKET = register("tertium_bucket", () -> new MoreBucketItem(8000,"mysticalagriculture"));
	public static final RegistryObject<Item> IMPERIUM_BUCKET = register("imperium_bucket", () -> new MoreBucketItem(16000, "mysticalagriculture"));
	public static final RegistryObject<Item> SUPREMIUM_BUCKET = register("supremium_bucket", () -> new MoreBucketItem(32000, "mysticalagriculture"));
	public static final RegistryObject<Item> INSANIUM_BUCKET = register("insanium_bucket", () -> new MoreBucketItem(64000, "mysticalagradditions"));

	private static RegistryObject<Item> register(String name) {
		return register(name, () -> new BaseItem(p -> p.tab(CREATIVE_TAB)));
	}

	private static RegistryObject<Item> register(String name, Supplier<Item> item) {
		return REGISTRY.register(name, item);
	}
}
