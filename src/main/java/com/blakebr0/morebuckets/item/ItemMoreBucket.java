package com.blakebr0.morebuckets.item;

import java.util.List;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.lib.BucketUtils;
import com.blakebr0.morebuckets.lib.FluidHolderItemWrapper;
import com.blakebr0.morebuckets.lib.IFluidHolder;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
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

	public ItemMoreBucket(String name, int capacity) {
		super(MoreBuckets.MOD_ID + "." + name);
		this.name = name;
		this.capacity = capacity;

		this.setMaxStackSize(1);
		this.setCreativeTab(MoreBuckets.CREATIVE_TAB);
		// TODO: custom dispenser behavior
		// BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return BucketUtils.getFluidAmount(stack) > 0;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		ItemStack copy = stack.copy();
		
		this.drain(copy, Fluid.BUCKET_VOLUME, true);
		
		return copy;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.getCapacity(stack) > 1000;
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

		if (fluid != null && BucketUtils.getFluidRarity(fluid) != EnumRarity.COMMON) {
			fluidName = BucketUtils.getFluidRarity(fluid).rarityColor.toString() + fluidName;
		}

		tooltip.add(Utils.localize("tooltip.morebuckets.buckets", buckets, capacity, fluidName));
	}

	@Override // TODO: bucket event
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		FluidStack fluid = this.getFluid(stack);

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

		RayTraceResult trace = this.rayTrace(world, player, false);
		if (trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		BlockPos pos = trace.getBlockPos();
		if (world.isBlockModifiable(player, pos)) {
			BlockPos targetPos = pos.offset(trace.sideHit);
			if (player.canPlayerEdit(targetPos, trace.sideHit.getOpposite(), stack)) {
				FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, stack, new FluidStack(this.getFluid(stack), Fluid.BUCKET_VOLUME));
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

		RayTraceResult trace = this.rayTrace(world, player, true);
		if (trace == null || trace.sideHit == null || trace.typeOfHit != RayTraceResult.Type.BLOCK)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		BlockPos pos = trace.getBlockPos();
		if (world.isBlockModifiable(player, pos)) {
			if (player.canPlayerEdit(pos, trace.sideHit, stack)) {
				FluidActionResult result = FluidUtil.tryPickUpFluid(stack, player, world, pos, trace.sideHit);
				if (result.isSuccess() && !player.capabilities.isCreativeMode) {
					player.addStat(StatList.getObjectUseStats(this));
					return ActionResult.newResult(EnumActionResult.SUCCESS, result.getResult());
				}
			}
		}

		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if (target instanceof EntityCow) {
			EntityCow cow = (EntityCow) target;
			if (!player.capabilities.isCreativeMode && !cow.isChild()) {
				Fluid milk = FluidRegistry.getFluid("milk");
				return milk != null ? this.fill(stack, new FluidStack(FluidRegistry.getFluid("milk"), 1000), true) > 0 : false;
			}
		}
		
		return false;
	}

	public int getSpaceLeft(ItemStack stack) {
		return this.getCapacity(stack) - BucketUtils.getFluidAmount(stack);
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

		int capacity = this.getCapacity(stack);

		if (!canFill) {
			if (bucketFluid == null)
				return BucketUtils.toBuckets(Math.min(capacity, fluid.amount));

			return BucketUtils.toBuckets(Math.min(capacity - bucketFluid.amount, fluid.amount));
		}

		int filled = BucketUtils.toBuckets(Math.min(fluid.amount, capacity));

		if (bucketFluid == null) {
			NBTTagCompound fluidTag = fluid.writeToNBT(new NBTTagCompound());
			fluidTag.setInteger("Amount", filled);
			stack.setTagCompound(fluidTag);

			return filled;
		}

		filled = BucketUtils.toBuckets(capacity - bucketFluid.amount);
		int amount = BucketUtils.toBuckets(fluid.amount);

		if (amount < filled) {
			bucketFluid.amount += amount;
			filled = amount;
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

		int drained = BucketUtils.toBuckets(Math.min(fluid.amount, amount));

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