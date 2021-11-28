package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.iface.IFluidHolder;
import com.blakebr0.cucumber.item.BaseItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;

public class MoreBucketItem extends BaseItem implements IFluidHolder, IEnableable {
	private final int capacity;
	private final String[] requiredMods;

	public MoreBucketItem(int capacity) {
		this(capacity, true);
	}
	
	public MoreBucketItem(int capacity, String... requiredMods) {
		this(capacity, true, requiredMods);
	}
	
	public MoreBucketItem(int capacity, boolean recipeReplacement, String... requiredMods) {
		super(p -> p);
		this.capacity = capacity;
		this.requiredMods = requiredMods;

//		this.setMaxStackSize(1);
//		this.setCreativeTab(MoreBuckets.CREATIVE_TAB);
//
//		if (this.isEnabled()) {
//			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseBucketBehavior.getInstance());
//
//			if (recipeReplacement) {
//				RecipeFixer.VALID_BUCKETS.add(this);
//			}
//		}
	}

//	@Override
//	public boolean hasContainerItem(ItemStack stack) {
//		return BucketHelper.getFluidAmount(stack) > 0;
//	}
//
//	@Override
//	public ItemStack getContainerItem(ItemStack stack) {
//		ItemStack copy = new ItemStack(this);
//		copy.setTagCompound(stack.getTagCompound());
//
//		this.drain(copy, Fluid.BUCKET_VOLUME, true);
//
//		return copy;
//	}
//
//	@Override
//	public boolean showDurabilityBar(ItemStack stack) {
//		return this.getCapacity(stack) > 1000;
//	}
//
//	@Override
//	public double getDurabilityForDisplay(ItemStack stack) {
//		int capacity = this.getCapacity(stack);
//		double stored = capacity - BucketHelper.getFluidAmount(stack);
//		return stored / capacity;
//	}
//
//	@Override
//	public int getItemBurnTime(ItemStack stack) {
//		FluidStack fluid = this.getFluid(stack);
//		if (fluid != null && fluid.isFluidEqual(new FluidStack(FluidRegistry.LAVA, 1000))) {
//			if (BucketHelper.getFluidAmount(stack) >= 1000) {
//				return 20000;
//			}
//		}
//
//		return -1;
//	}
//
//	@Override
//	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
//		int capacity = this.getCapacity(stack) / 1000;
//		int buckets = BucketHelper.getFluidAmount(stack) / 1000;
//		FluidStack fluid = this.getFluid(stack);
//
//		String fluidName = fluid == null ? Utils.localize("tooltip.morebuckets.empty") : fluid.getLocalizedName();
//
//		if (fluid != null && FluidHelper.getFluidRarity(fluid) != EnumRarity.COMMON) {
//			fluidName = FluidHelper.getFluidRarity(fluid).rarityColor.toString() + fluidName;
//		}
//
//		tooltip.add(Utils.localize("tooltip.morebuckets.buckets", buckets, capacity, fluidName));
//	}
//
//	@Override
//	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
//		ItemStack stack = player.getHeldItem(hand);
//		FluidStack fluid = this.getFluid(stack);
//
//		ActionResult<ItemStack> pickup = this.tryPickupFluid(stack, world, player, hand);
//
//		if (pickup.getType() == EnumActionResult.SUCCESS) {
//			return pickup;
//		} else {
//			if (fluid != null && fluid.amount >= Fluid.BUCKET_VOLUME) {
//				return this.tryPlaceFluid(stack, world, player, hand);
//			} else {
//				return ActionResult.newResult(EnumActionResult.FAIL, stack);
//			}
//		}
//	}
//
//	@Override
//	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
//		if (target instanceof EntityCow) {
//			EntityCow cow = (EntityCow) target;
//			if (!player.capabilities.isCreativeMode && !cow.isChild()) {
//				Fluid milk = FluidRegistry.getFluid("milk");
//				return milk != null && this.fill(stack, new FluidStack(milk, 1000), true) > 0;
//			}
//		}
//
//		return false;
//	}
//
//	@Override
//	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound tag) {
//		return new FluidHolderItemWrapper(stack, this, true, true);
//	}
//
	@Override
	public int getCapacity(ItemStack stack) {
		return this.capacity;
	}

	@Override
	public FluidStack getFluid(ItemStack stack) {
		return FluidHelper.getFluidFromStack(stack);
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
				return FluidHelper.toBuckets(Math.min(capacity, fluid.getAmount()));

			return FluidHelper.toBuckets(Math.min(capacity - bucketFluid.getAmount(), fluid.getAmount()));
		}

		int filled = FluidHelper.toBuckets(Math.min(fluid.getAmount(), capacity));

		if (bucketFluid == null) {
			var fluidTag = fluid.writeToNBT(new CompoundTag());
			fluidTag.putInt("Amount", filled);
			stack.setTag(fluidTag);

			return filled;
		}

		filled = FluidHelper.toBuckets(capacity - bucketFluid.getAmount());
		int amount = FluidHelper.toBuckets(fluid.getAmount());

		if (amount < filled) {
			bucketFluid.grow(amount);;
			filled = amount;
		} else {
			bucketFluid.setAmount(capacity);
		}

		bucketFluid.writeToNBT(stack.getTag());

		return filled;
	}

	@Override
	public FluidStack drain(ItemStack stack, int amount, boolean canDrain) {
		NBTHelper.validateCompound(stack);

		if (amount == 0) return null;

		FluidStack fluid = this.getFluid(stack);
		if (fluid == null) return null;

		int drained = FluidHelper.toBuckets(Math.min(fluid.getAmount(), amount));

		if (canDrain) {
			if (amount >= fluid.getAmount()) {
				NBTHelper.removeTag(stack, "FluidName");
				NBTHelper.removeTag(stack, "Amount");
				return fluid;
			}

			fluid.shrink(drained);
			fluid.writeToNBT(stack.getTag());
		}

		fluid.setAmount(drained);
		return fluid;
	}
//
//	@Override
//	public void initModels() {
//		ModelLoader.setCustomMeshDefinition(this, stack -> ResourceHelper.getModelResource(MoreBuckets.MOD_ID, this.name, "inventory"));
//	}
//
//	public int getSpaceLeft(ItemStack stack) {
//		return this.getCapacity(stack) - BucketHelper.getFluidAmount(stack);
//	}
//
//	private ActionResult<ItemStack> tryPlaceFluid(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
//		if (BucketHelper.getFluidAmount(stack) < Fluid.BUCKET_VOLUME)
//			return ActionResult.newResult(EnumActionResult.PASS, stack);
//
//		RayTraceResult trace = this.rayTrace(world, player, false);
//		if (trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK)
//			return ActionResult.newResult(EnumActionResult.PASS, stack);
//
//		BlockPos pos = trace.getBlockPos();
//		if (world.isBlockModifiable(player, pos)) {
//			BlockPos targetPos = pos.offset(trace.sideHit);
//			if (player.canPlayerEdit(targetPos, trace.sideHit.getOpposite(), stack)) {
//				FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, stack, new FluidStack(this.getFluid(stack), Fluid.BUCKET_VOLUME));
//				if (result.isSuccess() && !player.capabilities.isCreativeMode) {
//					player.addStat(StatList.getObjectUseStats(this));
//					return ActionResult.newResult(EnumActionResult.SUCCESS, result.getResult());
//				}
//			}
//		}
//
//		return ActionResult.newResult(EnumActionResult.FAIL, stack);
//	}
//
//	private ActionResult<ItemStack> tryPickupFluid(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
//		if (this.getSpaceLeft(stack) < Fluid.BUCKET_VOLUME)
//			return ActionResult.newResult(EnumActionResult.PASS, stack);
//
//		RayTraceResult trace = this.rayTrace(world, player, true);
//		if (trace == null || trace.sideHit == null || trace.typeOfHit != RayTraceResult.Type.BLOCK)
//			return ActionResult.newResult(EnumActionResult.PASS, stack);
//
//		BlockPos pos = trace.getBlockPos();
//		if (world.isBlockModifiable(player, pos)) {
//			if (player.canPlayerEdit(pos, trace.sideHit, stack)) {
//				FluidActionResult result = FluidUtil.tryPickUpFluid(stack, player, world, pos, trace.sideHit);
//				if (result.isSuccess() && !player.capabilities.isCreativeMode) {
//					player.addStat(StatList.getObjectUseStats(this));
//					return ActionResult.newResult(EnumActionResult.SUCCESS, result.getResult());
//				}
//			}
//		}
//
//		return ActionResult.newResult(EnumActionResult.FAIL, stack);
//	}

	@Override
	public boolean isEnabled() {
		return Arrays.stream(this.requiredMods).anyMatch(ModList.get()::isLoaded);
	}
}