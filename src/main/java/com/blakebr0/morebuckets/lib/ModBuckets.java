package com.blakebr0.morebuckets.lib;

import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.bucket.Bucket;
import com.blakebr0.morebuckets.config.ModConfigs;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModBuckets {
    public static final Map<Identifier, Bucket> ALL = new LinkedHashMap<>();

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
        ALL.put(MoreBuckets.resource("copper"), COPPER);
        ALL.put(MoreBuckets.resource("quartz"), QUARTZ);
        ALL.put(MoreBuckets.resource("obsidian"), OBSIDIAN);
        ALL.put(MoreBuckets.resource("gold"), GOLD);
        ALL.put(MoreBuckets.resource("emerald"), EMERALD);
        ALL.put(MoreBuckets.resource("diamond"), DIAMOND);

        ALL.put(MoreBuckets.resource("aluminum"), ALUMINUM);
        ALL.put(MoreBuckets.resource("tin"), TIN);
        ALL.put(MoreBuckets.resource("rubber"), RUBBER);
        ALL.put(MoreBuckets.resource("silver"), SILVER);
        ALL.put(MoreBuckets.resource("bronze"), BRONZE);
        ALL.put(MoreBuckets.resource("steel"), STEEL);

        ALL.put(MoreBuckets.resource("inferium"), INFERIUM);
        ALL.put(MoreBuckets.resource("prudentium"), PRUDENTIUM);
        ALL.put(MoreBuckets.resource("tertium"), TERTIUM);
        ALL.put(MoreBuckets.resource("imperium"), IMPERIUM);
        ALL.put(MoreBuckets.resource("supremium"), SUPREMIUM);
        ALL.put(MoreBuckets.resource("insanium"), INSANIUM);
    }
}
