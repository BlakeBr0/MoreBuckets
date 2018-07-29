package com.blakebr0.morebuckets.item;

import java.util.List;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.lib.BucketUtils;
import com.blakebr0.morebuckets.lib.FluidHolderItemWrapper;
import com.blakebr0.morebuckets.lib.IFluidHolder;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class ItemMoreBucket extends ItemBase implements IFluidHolder, IModelHelper {
	
	private final String name;
    private final int capacity;
    private final ItemStack empty;
    private final boolean nbtSensitive;

    public ItemMoreBucket(String name, int capacity) {
    	super(MoreBuckets.MOD_ID + "." + name);
    	this.name = name;
        this.capacity = capacity;
        this.empty = new ItemStack(this);
        this.nbtSensitive = true;

        this.setMaxStackSize(1);
        this.setCreativeTab(MoreBuckets.CREATIVE_TAB);
        // TODO: custom dispenser behavior
        //BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return !this.getEmpty().isEmpty();
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (!this.getEmpty().isEmpty()) return this.getEmpty().copy();
        return super.getContainerItem(stack);
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
    	return this.getCapacity(stack) / 1000 > 1;
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
    	int capacity = this.getCapacity(stack);
    	double stored = capacity - BucketUtils.getFluidAmount(stack);
    	return (double) stored / capacity;
    }
    
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
    	int capacity = this.getCapacity(stack) / 1000;
    	int buckets = BucketUtils.getFluidAmount(stack) / 1000;
    	FluidStack fluid = this.getFluid(stack);
    	String fluidName = fluid == null ? Utils.localize("tooltip.morebuckets.empty") : fluid.getLocalizedName();
    	tooltip.add(Utils.localize("tooltip.morebuckets.buckets", buckets, capacity, fluidName));
    }

    @Override// TODO: bucket event
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        FluidStack fluid = getFluid(stack);
        
        ActionResult<ItemStack> pickup = this.tryPickupFluid(stack, world, player, hand);
        
        if (pickup.getType() == EnumActionResult.SUCCESS) {
        	return pickup;
        } else {
        	if (fluid != null && fluid.amount >= Fluid.BUCKET_VOLUME) {
            	return this.tryPlaceFluid(stack, world, player, hand);
        	} else {
        		return ActionResult.newResult(EnumActionResult.FAIL, stack);
        	}
        }
    }
    
    private ActionResult<ItemStack> tryPlaceFluid(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (BucketUtils.getFluidAmount(stack) < Fluid.BUCKET_VOLUME)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
    	
        RayTraceResult mop = this.rayTrace(world, player, false);
    	if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
    	
		BlockPos pos = mop.getBlockPos();
		if (world.isBlockModifiable(player, pos)) {
			BlockPos targetPos = pos.offset(mop.sideHit);
			if (player.canPlayerEdit(targetPos, mop.sideHit.getOpposite(), stack)) {
				FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, stack, new FluidStack(getFluid(stack), Fluid.BUCKET_VOLUME));
				if (result.isSuccess() && !player.capabilities.isCreativeMode) {
					player.addStat(StatList.getObjectUseStats(this));
					return ActionResult.newResult(EnumActionResult.SUCCESS, result.getResult());
				}
			}
		}
		
    	return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }
    
    private ActionResult<ItemStack> tryPickupFluid(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {    	
    	if (this.getSpaceLeft(stack) < Fluid.BUCKET_VOLUME)
    		return ActionResult.newResult(EnumActionResult.PASS, stack);
    	
    	RayTraceResult traceResult = this.rayTrace(world, player, true);
		if (traceResult == null || traceResult.sideHit == null || traceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
		
		BlockPos pos = traceResult.getBlockPos();
		if (world.isBlockModifiable(player, pos)) {
			if (player.canPlayerEdit(pos, traceResult.sideHit, stack)) {
				FluidActionResult result = FluidUtil.tryPickUpFluid(stack, player, world, pos, traceResult.sideHit);
				if (result.isSuccess() && !player.capabilities.isCreativeMode) {
					player.addStat(StatList.getObjectUseStats(this));
					return ActionResult.newResult(EnumActionResult.SUCCESS, result.getResult());
				}
			}
		}
		
    	return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }
    
    public int getSpaceLeft(ItemStack stack) {
    	return this.getCapacity(stack) - BucketUtils.getFluidAmount(stack);
    }
    
    public ItemStack getEmpty() {
        return this.empty;
    }

    public boolean isNbtSensitive() {
        return this.nbtSensitive;
    }

    @Override
    public String getCreatorModId(ItemStack itemStack) {
    	return super.getCreatorModId(itemStack);
        //FluidStack fluidStack = getFluid(itemStack);
       //String modId = FluidRegistry.getModId(fluidStack);
       // return modId != null ? modId : super.getCreatorModId(itemStack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound tag) {
        return new FluidHolderItemWrapper(stack, this, true, true);
    }

	@Override
	public int getCapacity(ItemStack stack) {
		return this.capacity;
	}
	
	@Override
	public FluidStack getFluid(ItemStack stack) {
		return BucketUtils.getFluid(stack);
	}

	@Override
	public int fill(ItemStack stack, FluidStack fluid, boolean canFill) {
		NBTHelper.validateCompound(stack);
		
		FluidStack bucketFluid = this.getFluid(stack);
		if (bucketFluid != null && !fluid.isFluidEqual(bucketFluid)) 
			return 0;

		if (!canFill) {
			if (bucketFluid == null)
				return Math.min(capacity, fluid.amount);
			
			return Math.min(capacity - bucketFluid.amount, fluid.amount);
		}
		
		int capacity = this.getCapacity(stack);	
		int filled = Math.min(fluid.amount, capacity);
		
		if (bucketFluid == null) {
			NBTTagCompound fluidTag = fluid.writeToNBT(new NBTTagCompound());
			fluidTag.setInteger("Amount", filled);
			stack.setTagCompound(fluidTag);
			
			return filled;
		}
			
		filled = capacity - fluid.amount;

		if (fluid.amount < filled) {
			bucketFluid.amount += fluid.amount;
			filled = fluid.amount;
		} else {
			bucketFluid.amount = capacity;
		}

		bucketFluid.writeToNBT(stack.getTagCompound());
		
		return filled;
	}

	@Override
	public FluidStack drain(ItemStack stack, int amount, boolean canDrain) {
		NBTHelper.validateCompound(stack);
		
		if (amount == 0) return null;
		
		FluidStack fluid = this.getFluid(stack);
		if (fluid == null) return null;
		
		int drained = Math.min(fluid.amount, amount);

		if (canDrain) {
			if (amount >= fluid.amount) {
				NBTHelper.removeTag(stack, "FluidName");
				NBTHelper.removeTag(stack, "Amount");
				return fluid;
			}
			
			fluid.amount -= drained;
			fluid.writeToNBT(stack.getTagCompound());
		}
		
		fluid.amount = drained;
		return fluid;
	}

	@Override
	public void initModels() {
		ModelLoader.setCustomMeshDefinition(this, stack -> ResourceHelper.getModelResource(MoreBuckets.MOD_ID, this.name, "inventory"));
	}
}