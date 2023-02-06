package com.blakebr0.morebuckets.crafting.condition;

import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.lib.ModBuckets;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class BucketEnabledCondition implements ICondition {
    private static final ResourceLocation ID = new ResourceLocation(MoreBuckets.MOD_ID, "bucket_enabled");
    private final ResourceLocation bucket;

    public BucketEnabledCondition(ResourceLocation bucket) {
        this.bucket = bucket;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        var bucket = ModBuckets.ALL.get(this.bucket);
        return bucket != null && bucket.isEnabled();
    }

    public static class Serializer implements IConditionSerializer<BucketEnabledCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, BucketEnabledCondition value) {
            json.addProperty("bucket", value.bucket.toString());
        }

        @Override
        public BucketEnabledCondition read(JsonObject json) {
            var crop = GsonHelper.getAsString(json, "bucket");
            return new BucketEnabledCondition(new ResourceLocation(crop));
        }

        @Override
        public ResourceLocation getID() {
            return BucketEnabledCondition.ID;
        }
    }
}
