package com.blakebr0.morebuckets.lib;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
// TODO: See what can go into cucumber
public class BucketUtils {

	public static FluidStack getFluid(ItemStack stack) {
		return FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
	}
	
	public static int getFluidAmount(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return fluid == null ? 0 : fluid.amount;
	}
	
	public static EnumRarity getFluidRarity(FluidStack fluid) {
		return fluid.getFluid().getRarity();
	}
}
