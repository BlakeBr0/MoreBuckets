//package com.blakebr0.morebuckets.lib;
//
//import net.minecraft.block.BlockDispenser;
//import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
//import net.minecraft.dispenser.IBlockSource;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntityDispenser;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fluids.DispenseFluidContainer;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.FluidActionResult;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.FluidUtil;
//import net.minecraftforge.fluids.capability.IFluidHandlerItem;
//
//public class DispenseBucketBehavior extends DispenseFluidContainer {
//
//	private static final DispenseBucketBehavior INSTANCE = new DispenseBucketBehavior();
//
//	public static DispenseBucketBehavior getInstance() {
//		return INSTANCE;
//	}
//
//	private DispenseBucketBehavior() { }
//
//	private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();
//
//	@Override
//    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
//        World world = source.getWorld();
//        EnumFacing dispenserFacing = source.getBlockState().getValue(BlockDispenser.FACING);
//        BlockPos blockpos = source.getBlockPos().offset(dispenserFacing);
//
//        FluidActionResult actionResult = FluidUtil.tryPickUpFluid(stack, null, world, blockpos, dispenserFacing.getOpposite());
//        ItemStack resultStack = actionResult.getResult();
//
//        if (!actionResult.isSuccess() || resultStack.isEmpty()) {
//            ItemStack singleStack = stack.copy();
//            singleStack.setCount(1);
//
//            IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(singleStack);
//            if (fluidHandler == null) return super.dispenseStack(source, stack);
//
//            FluidStack fluidStack = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);
//            FluidActionResult result = fluidStack != null ? FluidUtil.tryPlaceFluid(null, source.getWorld(), blockpos, stack, fluidStack) : FluidActionResult.FAILURE;
//
//    		if (result.isSuccess()) {
//    			ItemStack drainedStack = result.getResult();
//
//    			if (drainedStack.getCount() == 1) {
//    				return drainedStack;
//    			} else if (!drainedStack.isEmpty() && ((TileEntityDispenser) source.getBlockTileEntity()).addItemStack(drainedStack) < 0) {
//    				this.dispenseBehavior.dispense(source, drainedStack);
//    			}
//
//    			ItemStack stackCopy = drainedStack.copy();
//    			stackCopy.shrink(1);
//    			return stackCopy;
//    		} else {
//    			return this.dispenseBehavior.dispense(source, stack);
//    		}
//        } else {
//            if (stack.getCount() == 1) {
//                return resultStack;
//            } else if (((TileEntityDispenser) source.getBlockTileEntity()).addItemStack(resultStack) < 0) {
//                this.dispenseBehavior.dispense(source, resultStack);
//            }
//        }
//
//		return resultStack;
//    }
//}