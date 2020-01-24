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

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(QUARTZ_BUCKET.setRegistryName("quartz_bucket"));
		registry.register(OBSIDIAN_BUCKET.setRegistryName("obsidian_bucket"));
		registry.register(GOLD_BUCKET.setRegistryName("golden_bucket"));
		registry.register(EMERALD_BUCKET.setRegistryName("emerald_bucket"));
		registry.register(DIAMOND_BUCKET.setRegistryName("diamond_bucket"));
		
		registry.register(ALUMINUM_BUCKET.setRegistryName("aluminum_bucket"));
		registry.register(COPPER_BUCKET.setRegistryName("copper_bucket"));
		registry.register(TIN_BUCKET.setRegistryName("tin_bucket"));
		registry.register(RUBBER_BUCKET.setRegistryName("rubber_bucket"));
		registry.register(SILVER_BUCKET.setRegistryName("silver_bucket"));
		registry.register(BRONZE_BUCKET.setRegistryName("bronze_bucket"));
		registry.register(STEEL_BUCKET.setRegistryName("steel_bucket"));
		
		registry.register(INFERIUM_BUCKET.setRegistryName("inferium_bucket"));
		registry.register(PRUDENTIUM_BUCKET.setRegistryName("prudentium_bucket"));
		registry.register(INTERMEDIUM_BUCKET.setRegistryName("intermedium_bucket"));
		registry.register(SUPERIUM_BUCKET.setRegistryName("superium_bucket"));
		registry.register(SUPREMIUM_BUCKET.setRegistryName("supremium_bucket"));
		registry.register(INSANIUM_BUCKET.setRegistryName("insanium_bucket"));
		
		registry.register(ARDITE_BUCKET.setRegistryName("ardite_bucket"));
		registry.register(COBALT_BUCKET.setRegistryName("cobalt_bucket"));
		registry.register(MANYULLYN_BUCKET.setRegistryName("manyullyn_bucket"));
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
