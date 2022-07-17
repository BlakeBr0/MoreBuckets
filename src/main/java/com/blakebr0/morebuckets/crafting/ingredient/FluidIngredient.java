package com.blakebr0.morebuckets.crafting.ingredient;

import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.morebuckets.crafting.RecipeFixer;
import com.blakebr0.morebuckets.init.ModRecipeSerializers;
import com.blakebr0.morebuckets.item.MoreBucketItem;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public class FluidIngredient extends Ingredient {
    private final Ingredient parent;

    public FluidIngredient(Ingredient parent) {
        super(makeMatchingStacksArray(parent));
        this.parent = parent;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack == null) {
            return false;
        } else {
            var fluid = FluidUtil.getFluidContained(stack);
            if (fluid.isPresent() && stack.getItem() instanceof MoreBucketItem) {
                for (var itemstack : this.getItems()) {
                    var fluidstack = FluidUtil.getFluidContained(itemstack);
                    if ((fluidstack.isPresent() && fluidstack.get().isFluidEqual(fluid.get()))) {
                        return true;
                    }
                }
            }

            return this.parent.test(stack);
        }
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return ModRecipeSerializers.FLUID_INGREDIENT;
    }

    private static Stream<? extends Value> makeMatchingStacksArray(Ingredient ingredient) {
        var fluid = findFluid(ingredient);

        if (fluid != null) {
            ItemStack[] parentStacks = ingredient.getItems();
            ItemStack[] bucketStacks = RecipeFixer.VALID_BUCKETS.stream()
                    .map(e -> FluidHelper.getFilledBucket(fluid, e, e.getCapacity(ItemStack.EMPTY)))
                    .toArray(ItemStack[]::new);
            ItemStack[] matchingStacks = new ItemStack[parentStacks.length + bucketStacks.length];

            for (int i = 0; i < parentStacks.length; i++) {
                matchingStacks[i] = parentStacks[i];
            }

            for (int j = parentStacks.length; j < matchingStacks.length; j++) {
                matchingStacks[j] = bucketStacks[j - parentStacks.length];
            }

            return Arrays.stream(matchingStacks).map(ItemValue::new);
        }

        return Stream.of(ingredient.values);
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

    public static class Serializer implements IIngredientSerializer<FluidIngredient> {
        public FluidIngredient parse(FriendlyByteBuf buffer) {
            var ingredient = Ingredient.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(buffer.readVarInt()));
            return new FluidIngredient(ingredient);
        }

        public FluidIngredient parse(JsonObject json) {
            var ingredient = Ingredient.fromValues(Stream.of(Ingredient.valueFromJson(json)));
            return new FluidIngredient(ingredient);
        }

        public void write(FriendlyByteBuf buffer, FluidIngredient ingredient) {
            var items = ingredient.getItems();
            buffer.writeVarInt(items.length);

            for (var stack : items)
                buffer.writeItem(stack);
        }
    }
}
