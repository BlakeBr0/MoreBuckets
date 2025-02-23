package com.blakebr0.morebuckets.crafting.ingredient;

import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.morebuckets.crafting.RecipeFixer;
import com.blakebr0.morebuckets.init.ModIngredientTypes;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

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
            var fluid = FluidUtil.getFluidContained(stack);
            if (fluid.isPresent() && stack.getItem() instanceof MoreBucketItem) {
                return this.getItems().anyMatch(s -> FluidUtil.getFluidContained(s).map(f -> f.is(fluid.get().getFluid())).orElse(false));
            }

            return this.parent.test(stack);
        }
    }

    @Override
    public Stream<ItemStack> getItems() {
        if (this.stacks == null) {
            this.initMatchingStacks();
        }

        return Stream.of(this.stacks);
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
        var fluid = findFluid(this.parent);

        if (fluid != null) {
            ItemStack[] parentStacks = this.parent.getItems();
            ItemStack[] bucketStacks = RecipeFixer.VALID_BUCKETS.stream()
                    .map(e -> FluidHelper.getFilledBucket(fluid, e, e.getCapacity()))
                    .toArray(ItemStack[]::new);
            ItemStack[] matchingStacks = new ItemStack[parentStacks.length + bucketStacks.length];

            for (int i = 0; i < parentStacks.length; i++) {
                matchingStacks[i] = parentStacks[i];
            }

            for (int j = parentStacks.length; j < matchingStacks.length; j++) {
                matchingStacks[j] = bucketStacks[j - parentStacks.length];
            }

            this.stacks = matchingStacks;
        } else {
            this.stacks = new ItemStack[0];
        }
    }

    public static Ingredient of(Ingredient parent) {
        return new Ingredient(new FluidBucketIngredient(parent));
    }

    private static FluidStack findFluid(Ingredient ingredient) {
        for (var stack : ingredient.getItems()) {
            var fluid = FluidUtil.getFluidContained(stack);
            if (fluid.isPresent()) {
                return fluid.get();
            }
        }

        return null;
    }
}
