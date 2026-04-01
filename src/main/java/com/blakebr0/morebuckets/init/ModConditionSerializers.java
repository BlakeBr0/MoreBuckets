package com.blakebr0.morebuckets.init;

import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.crafting.condition.BucketEnabledCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModConditionSerializers {
    public static final DeferredRegister<MapCodec<? extends ICondition>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, MoreBuckets.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<BucketEnabledCondition>> BUCKET_ENABLED = REGISTRY.register("bucket_enabled", () -> BucketEnabledCondition.MAP_CODEC);
}
