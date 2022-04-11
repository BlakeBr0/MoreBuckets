package com.blakebr0.morebuckets.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.morebuckets.crafting.ingredient.FluidIngredient;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;

public class RecipeFixer implements ResourceManagerReloadListener {
    public static final ArrayList<MoreBucketItem> VALID_BUCKETS = new ArrayList<>();

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        var recipes = RecipeHelper.getRecipes(RecipeType.CRAFTING);

        for (var recipe : recipes.values()) {
            var ingredients = recipe.getIngredients();

            for (int i = 0; i < ingredients.size(); i++) {
                var ingredient = ingredients.get(i);
                if (!ingredient.getClass().equals(Ingredient.class) && !ingredient.getClass().isInstance(AbstractIngredient.class))
                    continue;

                for (var value : ingredient.values) {
                    // we want to avoid initializing tag ingredients
                    if (value instanceof Ingredient.ItemValue) {
                        for (var stack : value.getItems()) {
                            var item = stack.getItem();
                            if (item instanceof BucketItem || item instanceof IFluidHandler) {
                                ingredients.set(i, new FluidIngredient(ingredient));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
    }
}
