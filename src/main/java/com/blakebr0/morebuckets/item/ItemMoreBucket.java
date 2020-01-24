package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.fluid.FluidHolderItemWrapper;
import com.blakebr0.cucumber.helper.BucketHelper;
import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.iface.IFluidHolder;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.lib.RecipeFixer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.List;

public class ItemMoreBucket extends BaseItem implements IFluidHolder, IModelHelper, IEnableable {
	private final String name;
	private final int capacity;
	private final boolean requiredMods;

	public ItemMoreBucket(String name, int capacity) {
		this(name, capacity, true, true);
	}
	
	public ItemMoreBucket(String name, int capacity, boolean requiredMods) {
		this(name, capacity, true, requiredMods);
	}
	
	public ItemMoreBucket(String name, int capacity, boolean recipeReplacement, boolean requiredMods) {
		super(p -> p.group(MoreBuckets.ITEM_GROUP).maxStackSize(1));
		this.name = name;
		this.capacity = capacity;
		this.requiredMods = requiredMods;

		if (this.isEnabled()) {
			DispenserBlock.registerDispenseBehavior(this, DispenseFluidContainer.getInstance());

			if (recipeReplacement) {
				RecipeFixer.VALID_BUCKETS.add(this);
			}
		}
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return BucketHelper.getFluidAmount(stack) > 0;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		ItemStack copy = new ItemStack(this);
		copy.setTag(stack.getTag());
		
		this.drain(copy, 1000, true);
		
		return copy;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.getCapacity(stack) > 1000;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		int capacity = this.getCapacity(stack);
		double stored = capacity - BucketHelper.getFluidAmount(stack);
		return stored / capacity;
	}

	@Override
	public int getBurnTime(ItemStack stack) {
		FluidStack fluid = this.getFluid(stack);
		if (fluid != null && fluid.isFluidEqual(new FluidStack(Fluids.LAVA, 1000))) {
			if (BucketHelper.getFluidAmount(stack) >= 1000) {
				return 20000;
			}
		}

		return -1;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		int capacity = this.getCapacity(stack) / 1000;
		int buckets = BucketHelper.getFluidAmount(stack) / 1000;
		FluidStack fluid = this.getFluid(stack);

		String fluidName = fluid == null ? Utils.localize("tooltip.morebuckets.empty") : fluid.getLocalizedName();

		if (fluid != null && FluidHelper.getFluidRarity(fluid) != Rarity.COMMON) {
			fluidName = FluidHelper.getFluidRarity(fluid).color.toString() + fluidName;
		}

		tooltip.add(Utils.localize("tooltip.morebuckets.buckets", buckets, capacity, fluidName));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		FluidStack fluid = this.getFluid(stack);

		ActionResult<ItemStack> pickup = this.tryPickupFluid(stack, world, player);

		if (pickup.getType() == ActionResultType.SUCCESS) {
			return pickup;
		} else {
			if (fluid != null && fluid.getAmount() >= 1000) {
				return this.tryPlaceFluid(stack, world, player);
			} else {
				return ActionResult.newResult(ActionResultType.FAIL, stack);
			}
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (target instanceof CowEntity) {
			CowEntity cow = (CowEntity) target;
			if (!player.isCreative() && !cow.isChild()) {
				Fluid milk = FluidRegistry.getFluid("milk");
				return milk != null && this.fill(stack, new FluidStack(milk, 1000), true) > 0;
			}
		}
		
		return false;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT tag) {
		return new FluidHolderItemWrapper(stack, this, true, true);
	}

	@Override
	public int getCapacity(ItemStack stack) {
		return this.capacity;
	}

	@Override
	public FluidStack getFluid(ItemStack stack) {
		return BucketHelper.getFluid(stack);
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
				return BucketHelper.toBuckets(Math.min(capacity, fluid.getAmount()));

			return BucketHelper.toBuckets(Math.min(capacity - bucketFluid.getAmount(), fluid.getAmount()));
		}

		int filled = BucketHelper.toBuckets(Math.min(fluid.getAmount(), capacity));

		if (bucketFluid == null) {
			CompoundNBT fluidTag = fluid.writeToNBT(new CompoundNBT());
			fluidTag.putInt("Amount", filled);
			stack.setTag(fluidTag);

			return filled;
		}

		filled = BucketHelper.toBuckets(capacity - bucketFluid.getAmount());
		int amount = BucketHelper.toBuckets(fluid.getAmount());

		if (amount < filled) {
			bucketFluid.amount += amount;
			filled = amount;
		} else {
			bucketFluid.amount = capacity;
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

		int drained = BucketHelper.toBuckets(Math.min(fluid.getAmount(), amount));

		if (canDrain) {
			if (amount >= fluid.getAmount()) {
				NBTHelper.removeTag(stack, "FluidName");
				NBTHelper.removeTag(stack, "Amount");
				return fluid;
			}

			fluid.amount -= drained;
			fluid.writeToNBT(stack.getTag());
		}

		fluid.amount = drained;
		return fluid;
	}

	@Override
	public void initModels() {
		ModelLoader.setCustomMeshDefinition(this, stack -> ResourceHelper.getModelResource(MoreBuckets.MOD_ID, this.name, "inventory"));
	}
	
	public int getSpaceLeft(ItemStack stack) {
		return this.getCapacity(stack) - BucketHelper.getFluidAmount(stack);
	}
	
	private ActionResult<ItemStack> tryPlaceFluid(ItemStack stack, World world, PlayerEntity player) {
		if (BucketHelper.getFluidAmount(stack) < 1000)
			return ActionResult.newResult(ActionResultType.PASS, stack);

		RayTraceResult trace = this.rayTrace(world, player, false);
		if (trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK)
			return ActionResult.newResult(ActionResultType.PASS, stack);

		BlockPos pos = trace.getBlockPos();
		if (world.isBlockModifiable(player, pos)) {
			BlockPos targetPos = pos.offset(trace.sideHit);
			if (player.canPlayerEdit(targetPos, trace.sideHit.getOpposite(), stack)) {
				FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, stack, new FluidStack(this.getFluid(stack), Fluid.BUCKET_VOLUME));
				if (result.isSuccess() && !player.isCreative()) {
					player.addStat(StatList.getObjectUseStats(this));
					return ActionResult.newResult(ActionResultType.SUCCESS, result.getResult());
				}
			}
		}

		return ActionResult.newResult(ActionResultType.FAIL, stack);
	}

	private ActionResult<ItemStack> tryPickupFluid(ItemStack stack, World world, PlayerEntity player) {
		if (this.getSpaceLeft(stack) < 1000)
			return ActionResult.newResult(ActionResultType.PASS, stack);

		RayTraceResult trace = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (trace.sideHit == null || trace.getType() != RayTraceResult.Type.BLOCK)
			return ActionResult.newResult(ActionResultType.PASS, stack);

		BlockPos pos = trace.getBlockPos();
		if (world.isBlockModifiable(player, pos)) {
			if (player.canPlayerEdit(pos, trace.sideHit, stack)) {
				FluidActionResult result = FluidUtil.tryPickUpFluid(stack, player, world, pos, trace.);
				if (result.isSuccess() && !player.isCreative()) {
					player.addStat(StatList.getObjectUseStats(this));
					return ActionResult.newResult(ActionResultType.SUCCESS, result.getResult());
				}
			}
		}

		return ActionResult.newResult(ActionResultType.FAIL, stack);
	}

	@Override
	public boolean isEnabled() {
		return this.requiredMods;
	}
}