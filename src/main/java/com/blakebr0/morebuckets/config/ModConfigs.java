package com.blakebr0.morebuckets.config;

import net.minecraftforge.common.ForgeConfigSpec;

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
}
