package com.blakebr0.morebuckets.crafting;

import com.blakebr0.morebuckets.item.MoreBucketItem;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.ArrayList;

public class RecipeFixer implements ResourceManagerReloadListener {
    public static final ArrayList<MoreBucketItem> VALID_BUCKETS = new ArrayList<>();

//    TODO recipe fixer?
    @Override
    public void onResourceManagerReload(ResourceManager manager) {
//        if (!ModConfigs.ENABLE_RECIPE_FIXER.get())
//            return;
//
//        VALID_BUCKETS.clear();
//
//        for (var bucket : MoreBucketItem.BUCKETS) {
//            if (bucket.isEnabled()) {
//                VALID_BUCKETS.add(bucket);
//            }
//        }
//
//        var recipes = RecipeHelper.getAllRecipes();
//
//        for (var recipe : recipes) {
//            var ingredients = recipe.value().getIngredients();
//
//            for (int i = 0; i < ingredients.size(); i++) {
//                var ingredient = ingredients.get(i);
//
//                if (ingredient.isCustom())
//                    continue;
//
//                for (var value : ingredient.getValues()) {
//                    // we want to avoid initializing tag ingredients
//                    if (value instanceof Ingredient.ItemValue) {
//                        for (var stack : value.getItems()) {
//                            var item = stack.getItem();
//                            if (item instanceof BucketItem || item instanceof MilkBucketItem || item instanceof IFluidHandler) {
//                                try {
//                                    ingredients.set(i, FluidBucketIngredient.of(ingredient));
//
//                                    if (recipe.value().getClass().equals(ShapelessRecipe.class)) {
//                                        setNotSimple((ShapelessRecipe) recipe.value());
//                                    }
//                                } catch (Exception e) {
//                                    MoreBuckets.LOGGER.warn("Failed to modify ingredients for recipe {}, skipping", recipe.id());
//                                }
//
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
//
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onAddReloadListeners(AddServerReloadListenersEvent event) {
//        event.addListener(this);
    }
//
//    private static void setNotSimple(ShapelessRecipe recipe) throws NoSuchFieldException, IllegalAccessException {
//        var isSimple = recipe.getClass().getDeclaredField("isSimple");
//
//        isSimple.setAccessible(true);
//        isSimple.set(recipe, false);
//    }
}
