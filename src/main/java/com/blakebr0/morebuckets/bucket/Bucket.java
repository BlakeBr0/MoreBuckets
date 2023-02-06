package com.blakebr0.morebuckets.bucket;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Supplier;

public class Bucket {
    private final String name;
    private final int buckets;
    private final Supplier<Boolean> hasRequirements;
    private ForgeConfigSpec.BooleanValue enabledValue;
    private ForgeConfigSpec.IntValue bucketsValue;

    public Bucket(String name, int buckets) {
        this(name, buckets, () -> true);
    }

    public Bucket(String name, int buckets, Supplier<Boolean> hasRequirements) {
        this.name = name;
        this.buckets = buckets;
        this.hasRequirements = hasRequirements;
    }

    public int getBuckets() {
        return this.bucketsValue.get();
    }

    public int getCapacity() {
        return this.bucketsValue.get() * FluidType.BUCKET_VOLUME;
    }

    public boolean isEnabled() {
        return this.hasRequirements.get() && this.enabledValue.get();
    }

    public void initConfigValues(ForgeConfigSpec.Builder config) {
        config.push(this.name);

        this.enabledValue = config.define("enabled", true);
        this.bucketsValue = config.defineInRange("capacity", this.buckets, 1, Integer.MAX_VALUE);

        config.pop();
    }
}
