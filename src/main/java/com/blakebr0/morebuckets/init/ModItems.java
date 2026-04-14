package com.blakebr0.morebuckets.init;

import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import com.blakebr0.morebuckets.lib.ModBuckets;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, MoreBuckets.MOD_ID);

	public static final DeferredHolder<Item, Item> COPPER_BUCKET = REGISTRY.register("copper_bucket", id -> new MoreBucketItem(id, ModBuckets.COPPER));
	public static final DeferredHolder<Item, Item> QUARTZ_BUCKET = REGISTRY.register("quartz_bucket", id -> new MoreBucketItem(id, ModBuckets.QUARTZ));
	public static final DeferredHolder<Item, Item> OBSIDIAN_BUCKET = REGISTRY.register("obsidian_bucket", id -> new MoreBucketItem(id, ModBuckets.OBSIDIAN));
	public static final DeferredHolder<Item, Item> GOLD_BUCKET = REGISTRY.register("golden_bucket", id -> new MoreBucketItem(id, ModBuckets.GOLD));
	public static final DeferredHolder<Item, Item> EMERALD_BUCKET = REGISTRY.register("emerald_bucket", id -> new MoreBucketItem(id, ModBuckets.EMERALD));
	public static final DeferredHolder<Item, Item> DIAMOND_BUCKET = REGISTRY.register("diamond_bucket", id -> new MoreBucketItem(id, ModBuckets.DIAMOND));

	public static final DeferredHolder<Item, Item> ALUMINUM_BUCKET = REGISTRY.register("aluminum_bucket", id -> new MoreBucketItem(id, ModBuckets.ALUMINUM));
	public static final DeferredHolder<Item, Item> TIN_BUCKET = REGISTRY.register("tin_bucket", id -> new MoreBucketItem(id, ModBuckets.TIN));
	public static final DeferredHolder<Item, Item> RUBBER_BUCKET = REGISTRY.register("rubber_bucket", id -> new MoreBucketItem(id, ModBuckets.RUBBER));
	public static final DeferredHolder<Item, Item> SILVER_BUCKET = REGISTRY.register("silver_bucket", id -> new MoreBucketItem(id, ModBuckets.SILVER));
	public static final DeferredHolder<Item, Item> BRONZE_BUCKET = REGISTRY.register("bronze_bucket", id -> new MoreBucketItem(id, ModBuckets.BRONZE));
	public static final DeferredHolder<Item, Item> STEEL_BUCKET = REGISTRY.register("steel_bucket", id -> new MoreBucketItem(id, ModBuckets.STEEL));

	public static final DeferredHolder<Item, Item> INFERIUM_BUCKET = REGISTRY.register("inferium_bucket", id -> new MoreBucketItem(id, ModBuckets.INFERIUM));
	public static final DeferredHolder<Item, Item> PRUDENTIUM_BUCKET = REGISTRY.register("prudentium_bucket", id -> new MoreBucketItem(id, ModBuckets.PRUDENTIUM));
	public static final DeferredHolder<Item, Item> TERTIUM_BUCKET = REGISTRY.register("tertium_bucket", id -> new MoreBucketItem(id, ModBuckets.TERTIUM));
	public static final DeferredHolder<Item, Item> IMPERIUM_BUCKET = REGISTRY.register("imperium_bucket", id -> new MoreBucketItem(id, ModBuckets.IMPERIUM));
	public static final DeferredHolder<Item, Item> SUPREMIUM_BUCKET = REGISTRY.register("supremium_bucket", id -> new MoreBucketItem(id, ModBuckets.SUPREMIUM));
	public static final DeferredHolder<Item, Item> INSANIUM_BUCKET = REGISTRY.register("insanium_bucket", id -> new MoreBucketItem(id, ModBuckets.INSANIUM));
}
