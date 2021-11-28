package com.blakebr0.morebuckets.lib;

import com.blakebr0.cucumber.helper.FluidHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.stream.Stream;

public class FluidIngredient extends Ingredient {
	private final Ingredient parent;
	
	public FluidIngredient(Ingredient parent) {
		super(Stream.of());
		this.parent = parent;
	}

	@Override
	public boolean test(ItemStack stack) {
//		if (stack == null) {
//			return false;
//		} else {
//			FluidStack fluid = FluidUtil.getFluidContained(stack);
//			if (fluid != null && stack.getItem() instanceof ItemMoreBucket) {
//				for (ItemStack itemstack : this.getMatchingStacks()) {
//					var fluidstack = FluidUtil.getFluidContained(itemstack);
//                    if ((fluidstack != null && fluidstack.isFluidEqual(fluid)) || itemstack.getItem() == Items.MILK_BUCKET && fluid.getFluid().getName().equals("milk")) {
//						return true;
//					}
//				}
//			}
//
//			return this.parent.apply(stack);
//		}

		return false;
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	private static ItemStack[] makeMatchingStacksArray(Ingredient ingredient) {
		FluidStack fluid = findFluid(ingredient);
		
//		if (fluid != null) {
//			ItemStack[] parentStacks = ingredient.getItems();
//			ItemStack[] bucketStacks = RecipeFixer.VALID_BUCKETS.stream()
//					.map(e -> FluidHelper.getFilledBucket(fluid, e, e.getCapacity(ItemStack.EMPTY)))
//					.toArray(ItemStack[]::new);
//			ItemStack[] matchingStacks = new ItemStack[parentStacks.length + bucketStacks.length];
//
//			for (int i = 0; i < parentStacks.length; i++) {
//				matchingStacks[i] = parentStacks[i];
//			}
//
//			for (int j = parentStacks.length; j < matchingStacks.length; j++) {
//				matchingStacks[j] = bucketStacks[j - parentStacks.length];
//			}
//
//			return matchingStacks;
//		}

		return ingredient.getItems();
	}
	
	private static FluidStack findFluid(Ingredient ingredient) {
//		for (ItemStack stack : ingredient.getMatchingStacks()) {
//			if (stack.getItem() == Items.WATER_BUCKET) {
//				return new FluidStack(FluidRegistry.WATER, 1000);
//			} else if (stack.getItem() == Items.LAVA_BUCKET) {
//				return new FluidStack(FluidRegistry.LAVA, 1000);
//			} else if (stack.getItem() == Items.MILK_BUCKET) {
//				if (FluidRegistry.isFluidRegistered("milk")) {
//					return new FluidStack(FluidRegistry.getFluid("milk"), 1000);
//				} else {
//					return null;
//				}
//			}
//
//			FluidStack fluid = FluidUtil.getFluidContained(stack);
//			if (fluid != null) {
//				return fluid;
//			}
//		}
		
		return null;
	}
}
