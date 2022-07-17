package com.blakebr0.morebuckets.init;

import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.crafting.ingredient.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModRecipeSerializers {
    public static final IIngredientSerializer<?> FLUID_INGREDIENT = new FluidIngredient.Serializer();

    @SubscribeEvent
    public void onRegisterRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        CraftingHelper.register(new ResourceLocation(MoreBuckets.MOD_ID, "fluid"), FLUID_INGREDIENT);
    }
}
