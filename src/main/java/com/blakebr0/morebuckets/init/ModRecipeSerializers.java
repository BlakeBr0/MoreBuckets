package com.blakebr0.morebuckets.init;

import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.crafting.condition.BucketEnabledCondition;
import com.blakebr0.morebuckets.crafting.ingredient.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public final class ModRecipeSerializers {
    public static final IIngredientSerializer<?> FLUID_INGREDIENT = new FluidIngredient.Serializer();

    @SubscribeEvent
    public void onRegisterRecipeSerializers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, registry -> {
            CraftingHelper.register(BucketEnabledCondition.Serializer.INSTANCE);

            CraftingHelper.register(new ResourceLocation(MoreBuckets.MOD_ID, "fluid"), FLUID_INGREDIENT);
        });
    }
}
