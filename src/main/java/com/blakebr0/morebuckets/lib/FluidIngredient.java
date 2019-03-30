package com.blakebr0.morebuckets.lib;

import com.blakebr0.cucumber.helper.BucketHelper;
import com.blakebr0.morebuckets.item.ItemMoreBucket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidIngredient extends Ingredient {

	private final Ingredient parent;
	
	public FluidIngredient(Ingredient parent) {
		super(makeMatchingStacksArray(parent));
		this.parent = parent;
	}

	@Override
	public boolean apply(ItemStack stack) {
		if (stack == null) {
			return false;
		} else {
			FluidStack fluid = FluidUtil.getFluidContained(stack);
			if (fluid != null && stack.getItem() instanceof ItemMoreBucket) {
				for (ItemStack itemstack : this.getMatchingStacks()) {
					FluidStack fluidstack = FluidUtil.getFluidContained(itemstack);
                    if ((fluidstack != null && fluidstack.isFluidEqual(fluid)) || itemstack.getItem() == Items.MILK_BUCKET && fluid.getFluid().getName().equals("milk")) {
						return true;
					}
				}
			}

			return this.parent.apply(stack);
		}
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	private static ItemStack[] makeMatchingStacksArray(Ingredient ingredient) {
		FluidStack fluid = findFluid(ingredient);
		
		if (fluid != null) {
			ItemStack[] parentStacks = ingredient.getMatchingStacks();
			ItemStack[] bucketStacks = RecipeFixer.VALID_BUCKETS.stream()
					.map(e -> BucketHelper.getFilledBucket(fluid, e, e.getCapacity(ItemStack.EMPTY)))
					.toArray(ItemStack[]::new);
			ItemStack[] matchingStacks = new ItemStack[parentStacks.length + bucketStacks.length];
					
			for (int i = 0; i < parentStacks.length; i++) {
				matchingStacks[i] = parentStacks[i];
			}
			
			for (int j = parentStacks.length; j < matchingStacks.length; j++) {
				matchingStacks[j] = bucketStacks[j - parentStacks.length];
			}
			
			return matchingStacks;
		}

		return ingredient.getMatchingStacks();
	}
	
	private static FluidStack findFluid(Ingredient ingredient) {
		for (ItemStack stack : ingredient.getMatchingStacks()) {
			if (stack.getItem() == Items.WATER_BUCKET) {
				return new FluidStack(FluidRegistry.WATER, 1000);
			} else if (stack.getItem() == Items.LAVA_BUCKET) {
				return new FluidStack(FluidRegistry.LAVA, 1000);
			} else if (stack.getItem() == Items.MILK_BUCKET) {
				if (FluidRegistry.isFluidRegistered("milk")) {
					return new FluidStack(FluidRegistry.getFluid("milk"), 1000);
				} else {
					return null;
				}
			}
			
			FluidStack fluid = FluidUtil.getFluidContained(stack);
			if (fluid != null) {
				return fluid;
			}
		}
		
		return null;
	}
}
