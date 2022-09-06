package com.blakebr0.morebuckets.config;

import com.blakebr0.cucumber.util.FeatureFlag;
import com.blakebr0.cucumber.util.FeatureFlags;
import com.blakebr0.morebuckets.MoreBuckets;
import net.minecraft.resources.ResourceLocation;

@FeatureFlags
public final class ModFeatureFlags {
    public static final FeatureFlag RECIPE_FIXER = FeatureFlag.create(new ResourceLocation(MoreBuckets.MOD_ID, "recipe_fixer"), ModConfigs.ENABLE_RECIPE_FIXER);
}
