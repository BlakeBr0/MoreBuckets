package com.blakebr0.morebuckets.config;

import com.blakebr0.morebuckets.lib.ModBuckets;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfigs {
    public static final ModConfigSpec COMMON;

    // Common
    static {
        final var common = new ModConfigSpec.Builder();

        common.comment("Individual options for each bucket.").push("Buckets");

        for (var bucket : ModBuckets.ALL.values())
            bucket.initConfigValues(common);

        common.pop();

        COMMON = common.build();
    }

    public static boolean isMysticalAgricultureInstalled() {
        return ModList.get().isLoaded("mysticalagriculture");
    }

    public static boolean isMysticalAgradditionsInstalled() {
        return ModList.get().isLoaded("mysticalagradditions");
    }
}
