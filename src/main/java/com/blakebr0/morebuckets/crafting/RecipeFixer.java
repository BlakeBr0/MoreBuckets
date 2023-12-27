package com.blakebr0.morebuckets.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.morebuckets.config.ModConfigs;
import com.blakebr0.morebuckets.crafting.ingredient.FluidIngredient;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import net.minecraftforge.common.crafting.IntersectionIngredient;
import net.minecraftforge.common.crafting.MultiItemValue;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;

public class RecipeFixer implements ResourceManagerReloadListener {
    public static final ArrayList<MoreBucketItem> VALID_BUCKETS = new ArrayList<>();

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        if (!ModConfigs.ENABLE_RECIPE_FIXER.get())
            return;

        VALID_BUCKETS.clear();

        for (var bucket : MoreBucketItem.BUCKETS) {
            if (bucket.isEnabled()) {
                VALID_BUCKETS.add(bucket);
            }
        }

        var recipes = RecipeHelper.getRecipeManager().getRecipes();

        for (var recipe : recipes) {
            var ingredients = recipe.getIngredients();

            for (int i = 0; i < ingredients.size(); i++) {
                var ingredient = ingredients.get(i);
                if (!ingredient.getClass().equals(Ingredient.class) && !isForgeIngredient(ingredient.getClass()))
                    continue;

                for (var value : ingredient.values) {
                    // we want to avoid initializing tag ingredients
                    if (value instanceof Ingredient.ItemValue || value instanceof MultiItemValue) {
                        for (var stack : value.getItems()) {
                            var item = stack.getItem();
                            if (item instanceof BucketItem || item instanceof MilkBucketItem || item instanceof IFluidHandler) {
                                ingredients.set(i, new FluidIngredient(ingredient));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
    }

    private static boolean isForgeIngredient(Class<?> clazz) {
        return clazz.equals(CompoundIngredient.class)
                || clazz.equals(DifferenceIngredient.class)
                || clazz.equals(IntersectionIngredient.class)
                || clazz.equals(StrictNBTIngredient.class)
                || clazz.equals(PartialNBTIngredient.class);
    }
}
