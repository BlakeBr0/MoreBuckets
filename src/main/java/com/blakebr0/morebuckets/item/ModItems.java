package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.lib.ModChecker;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.blakebr0.morebuckets.MoreBuckets.ITEM_GROUP;

public class ModItems {
	public static final List<Supplier<? extends Item>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<MoreBucketItem> QUARTZ_BUCKET = register("quartz_bucket", () -> new MoreBucketItem(2000));
	public static final RegistryObject<MoreBucketItem> OBSIDIAN_BUCKET = register("obsidian_bucket", () -> new MoreBucketItem(3000));
	public static final RegistryObject<MoreBucketItem> GOLD_BUCKET = register("golden_bucket", () -> new MoreBucketItem(4000));
	public static final RegistryObject<MoreBucketItem> EMERALD_BUCKET = register("emerald_bucket", () -> new MoreBucketItem(8000));
	public static final RegistryObject<MoreBucketItem> DIAMOND_BUCKET = register("diamond_bucket", () -> new MoreBucketItem(10000));
	
	public static final RegistryObject<MoreBucketItem> ALUMINUM_BUCKET = register("aluminum_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<MoreBucketItem> COPPER_BUCKET = register("copper_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<MoreBucketItem> TIN_BUCKET = register("tin_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<MoreBucketItem> RUBBER_BUCKET = register("rubber_bucket", () -> new MoreBucketItem(1000));
	public static final RegistryObject<MoreBucketItem> SILVER_BUCKET = register("silver_bucket", () -> new MoreBucketItem(3000));
	public static final RegistryObject<MoreBucketItem> BRONZE_BUCKET = register("bronze_bucket", () -> new MoreBucketItem(4000));
	public static final RegistryObject<MoreBucketItem> STEEL_BUCKET = register("steel_bucket", () -> new MoreBucketItem(6000));

	public static final RegistryObject<MoreBucketItem> INFERIUM_BUCKET = register("inferium_bucket", () -> new MoreBucketItem(2000, ModChecker.MYSTICAL_AGRICULTURE));
	public static final RegistryObject<MoreBucketItem> PRUDENTIUM_BUCKET = register("prudentium_bucket", () -> new MoreBucketItem(4000, ModChecker.MYSTICAL_AGRICULTURE));
	public static final RegistryObject<MoreBucketItem> TERTIUM_BUCKET = register("tertium_bucket", () -> new MoreBucketItem(8000,ModChecker.MYSTICAL_AGRICULTURE));
	public static final RegistryObject<MoreBucketItem> IMPERIUM_BUCKET = register("imperium_bucket", () -> new MoreBucketItem(16000, ModChecker.MYSTICAL_AGRICULTURE));
	public static final RegistryObject<MoreBucketItem> SUPREMIUM_BUCKET = register("supremium_bucket", () -> new MoreBucketItem(32000, ModChecker.MYSTICAL_AGRICULTURE));
	public static final RegistryObject<MoreBucketItem> INSANIUM_BUCKET = register("insanium_bucket", () -> new MoreBucketItem(64000, ModChecker.MYSTICAL_AGRADDITIONS));

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ENTRIES.stream().map(Supplier::get).forEach(registry::register);
	}

	private static <T extends Item> RegistryObject<T> register(String name) {
		return register(name, () -> new BaseItem(p -> p.group(ITEM_GROUP)));
	}

	private static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends Item> item) {
		ResourceLocation loc = new ResourceLocation(MoreBuckets.MOD_ID, name);
		ENTRIES.add(() -> item.get().setRegistryName(loc));
		return RegistryObject.of(loc, ForgeRegistries.ITEMS);
	}
}
