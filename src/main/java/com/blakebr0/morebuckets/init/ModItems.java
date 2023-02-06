package com.blakebr0.morebuckets.init;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import com.blakebr0.morebuckets.lib.ModBuckets;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.blakebr0.morebuckets.MoreBuckets.CREATIVE_TAB;

public class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MoreBuckets.MOD_ID);

	public static final RegistryObject<Item> COPPER_BUCKET = register("copper_bucket", () -> new MoreBucketItem(ModBuckets.COPPER, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> QUARTZ_BUCKET = register("quartz_bucket", () -> new MoreBucketItem(ModBuckets.QUARTZ, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> OBSIDIAN_BUCKET = register("obsidian_bucket", () -> new MoreBucketItem(ModBuckets.OBSIDIAN, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> GOLD_BUCKET = register("golden_bucket", () -> new MoreBucketItem(ModBuckets.GOLD, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> EMERALD_BUCKET = register("emerald_bucket", () -> new MoreBucketItem(ModBuckets.EMERALD, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> DIAMOND_BUCKET = register("diamond_bucket", () -> new MoreBucketItem(ModBuckets.DIAMOND, p -> p.tab(CREATIVE_TAB)));

	public static final RegistryObject<Item> ALUMINUM_BUCKET = register("aluminum_bucket", () -> new MoreBucketItem(ModBuckets.ALUMINUM, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> TIN_BUCKET = register("tin_bucket", () -> new MoreBucketItem(ModBuckets.TIN, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> RUBBER_BUCKET = register("rubber_bucket", () -> new MoreBucketItem(ModBuckets.RUBBER, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> SILVER_BUCKET = register("silver_bucket", () -> new MoreBucketItem(ModBuckets.SILVER, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> BRONZE_BUCKET = register("bronze_bucket", () -> new MoreBucketItem(ModBuckets.BRONZE, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> STEEL_BUCKET = register("steel_bucket", () -> new MoreBucketItem(ModBuckets.STEEL, p -> p.tab(CREATIVE_TAB)));

	public static final RegistryObject<Item> INFERIUM_BUCKET = register("inferium_bucket", () -> new MoreBucketItem(ModBuckets.INFERIUM, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> PRUDENTIUM_BUCKET = register("prudentium_bucket", () -> new MoreBucketItem(ModBuckets.PRUDENTIUM, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> TERTIUM_BUCKET = register("tertium_bucket", () -> new MoreBucketItem(ModBuckets.TERTIUM, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> IMPERIUM_BUCKET = register("imperium_bucket", () -> new MoreBucketItem(ModBuckets.IMPERIUM, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> SUPREMIUM_BUCKET = register("supremium_bucket", () -> new MoreBucketItem(ModBuckets.SUPREMIUM, p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> INSANIUM_BUCKET = register("insanium_bucket", () -> new MoreBucketItem(ModBuckets.INSANIUM, p -> p.tab(CREATIVE_TAB)));

	private static RegistryObject<Item> register(String name) {
		return register(name, () -> new BaseItem(p -> p.tab(CREATIVE_TAB)));
	}

	private static RegistryObject<Item> register(String name, Supplier<Item> item) {
		return REGISTRY.register(name, item);
	}
}
