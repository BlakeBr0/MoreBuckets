package com.blakebr0.morebuckets.lib;

import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.bucket.Bucket;
import com.blakebr0.morebuckets.config.ModConfigs;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModBuckets {
    public static final Map<ResourceLocation, Bucket> ALL = new LinkedHashMap<>();

    public static final Bucket COPPER = new Bucket("Copper", 1);
    public static final Bucket QUARTZ = new Bucket("Quartz", 2);
    public static final Bucket OBSIDIAN = new Bucket("Obsidian", 3);
    public static final Bucket GOLD = new Bucket("Gold", 4);
    public static final Bucket EMERALD = new Bucket("Emerald", 8);
    public static final Bucket DIAMOND = new Bucket("Diamond", 10);

    public static final Bucket ALUMINUM = new Bucket("Aluminum", 1);
    public static final Bucket TIN = new Bucket("Tin", 1);
    public static final Bucket RUBBER = new Bucket("Rubber", 1);
    public static final Bucket SILVER = new Bucket("Silver", 3);
    public static final Bucket BRONZE = new Bucket("Bronze", 4);
    public static final Bucket STEEL = new Bucket("Steel", 6);

    public static final Bucket INFERIUM = new Bucket("Inferium", 2, ModConfigs::isMysticalAgricultureInstalled);
    public static final Bucket PRUDENTIUM = new Bucket("Prudentium", 4, ModConfigs::isMysticalAgricultureInstalled);
    public static final Bucket TERTIUM = new Bucket("Tertium", 8, ModConfigs::isMysticalAgricultureInstalled);
    public static final Bucket IMPERIUM = new Bucket("Imperium", 16, ModConfigs::isMysticalAgricultureInstalled);
    public static final Bucket SUPREMIUM = new Bucket("Supremium", 32, ModConfigs::isMysticalAgricultureInstalled);
    public static final Bucket INSANIUM = new Bucket("Insanium", 64, ModConfigs::isMysticalAgradditionsInstalled);

    static {
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "copper"), COPPER);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "quartz"), QUARTZ);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "obsidian"), OBSIDIAN);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "gold"), GOLD);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "emerald"), EMERALD);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "diamond"), DIAMOND);

        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "aluminum"), ALUMINUM);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "tin"), TIN);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "rubber"), RUBBER);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "silver"), SILVER);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "bronze"), BRONZE);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "steel"), STEEL);

        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "inferium"), INFERIUM);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "prudentium"), PRUDENTIUM);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "tertium"), TERTIUM);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "imperium"), IMPERIUM);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "supremium"), SUPREMIUM);
        ALL.put(new ResourceLocation(MoreBuckets.MOD_ID, "insanium"), INSANIUM);
    }
}
