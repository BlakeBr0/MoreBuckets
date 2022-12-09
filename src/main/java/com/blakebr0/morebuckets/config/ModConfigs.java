package com.blakebr0.morebuckets.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;

public class ModConfigs {
    public static final ForgeConfigSpec COMMON;

    public static final ForgeConfigSpec.BooleanValue ENABLE_RECIPE_FIXER;

    // Common
    static {
        final var common = new ForgeConfigSpec.Builder();

        common.comment("General settings.").push("General");
        ENABLE_RECIPE_FIXER = common
                .comment("Should the recipes with buckets be automatically updated to work with More Buckets buckets?")
                .define("enableRecipeFixer", true);

        COMMON = common.build();
    }

    public static boolean isMysticalAgricultureInstalled() {
        return ModList.get().isLoaded("mysticalagriculture");
    }

    public static boolean isMysticalAgradditionsInstalled() {
        return ModList.get().isLoaded("mysticalagradditions");
    }
}
