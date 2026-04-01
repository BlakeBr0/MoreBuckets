package com.blakebr0.morebuckets.crafting.ingredient;

import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.morebuckets.crafting.RecipeFixer;
import com.blakebr0.morebuckets.init.ModIngredientTypes;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

// TODO: fluid bucket ingredient
public class FluidBucketIngredient implements ICustomIngredient {
    public static final MapCodec<FluidBucketIngredient> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Ingredient.CODEC.fieldOf("parent").forGetter(ingredient -> ingredient.parent)
            ).apply(builder, FluidBucketIngredient::new)
    );

    private final Ingredient parent;
    private ItemStack[] stacks;

    public FluidBucketIngredient(Ingredient parent) {
        this.parent = parent;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        } else {
            var fluid = FluidUtil.getFirstStackContained(stack);
//            if (!fluid.isEmpty() && stack.getItem() instanceof MoreBucketItem) {
//                return this.getItems().anyMatch(s -> FluidUtil.getFirstStackContained(s).is(fluid.getFluid()));
//            }

            return this.parent.test(stack);
        }
    }

    @Override
    public Stream<Holder<Item>> items() {
        return Stream.empty();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.FLUID_BUCKET.get();
    }

    private void initMatchingStacks() {
//        var fluid = findFluid(this.parent);
//
//        if (fluid != null) {
//            var parentStacks = this.parent.getValues();
//            var bucketStacks = RecipeFixer.VALID_BUCKETS.stream()
//                    .map(e -> FluidHelper.getFilledBucket(fluid, e, e.getCapacity()))
//                    .toArray(ItemStack[]::new);
//            var matchingStacks = new ItemStack[parentStacks.size() + bucketStacks.length];
//
//            for (int i = 0; i < parentStacks.size(); i++) {
//                matchingStacks[i] = parentStacks.get(i);
//            }
//
//            for (int j = parentStacks.size(); j < matchingStacks.length; j++) {
//                matchingStacks[j] = bucketStacks[j - parentStacks.size()];
//            }
//
//            this.stacks = matchingStacks;
//        } else {
//            this.stacks = this.parent.getValues();
//        }
    }

    public static Ingredient of(Ingredient parent) {
        return new Ingredient(new FluidBucketIngredient(parent));
    }

    private static FluidStack findFluid(Ingredient ingredient) {
//        for (var value : ingredient.getValues()) {
//            return FluidUtil.getFirstStackContained(value);
//        }

        return FluidStack.EMPTY;
    }
}
