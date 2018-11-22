package com.blakebr0.morebuckets.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidIngredient extends Ingredient {

	public FluidIngredient(ItemStack... stacks) {
		super(stacks);
	}

	@Override
	public boolean apply(ItemStack stack) {
		if (stack == null) {
			return false;
		} else {
			FluidStack fluid = FluidUtil.getFluidContained(stack);
			if (fluid != null) {
				for (ItemStack itemstack : this.getMatchingStacks()) {
					FluidStack fluidstack = FluidUtil.getFluidContained(itemstack);
					if (fluidstack != null && fluidstack.isFluidEqual(fluid)) {
						return true;
					}
				}
			}

			return false;
		}
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
}
