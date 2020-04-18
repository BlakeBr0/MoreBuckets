package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.fluid.FluidHolderItemWrapper;
import com.blakebr0.cucumber.helper.BucketHelper;
import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.iface.IFluidHolder;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.lib.ModTooltips;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.List;

public class MoreBucketItem extends BaseItem implements IFluidHolder, IEnableable {
	private final int capacity;
	private final boolean requiredMods;

	public MoreBucketItem(int capacity) {
		this(capacity, true, true);
	}
	
	public MoreBucketItem(int capacity, boolean requiredMods) {
		this(capacity, true, requiredMods);
	}
	
	public MoreBucketItem(int capacity, boolean recipeReplacement, boolean requiredMods) {
		super(p -> p.group(MoreBuckets.ITEM_GROUP).maxStackSize(1));
		this.capacity = capacity;
		this.requiredMods = requiredMods;

		if (this.isEnabled()) {
			DispenserBlock.registerDispenseBehavior(this, DispenseFluidContainer.getInstance());

			if (recipeReplacement) {
//				RecipeFixer.VALID_BUCKETS.add(this);
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
		
		this.drain(copy, FluidAttributes.BUCKET_VOLUME, true);
		
		return copy;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.getCapacity(stack) > FluidAttributes.BUCKET_VOLUME;
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
		if (!fluid.isEmpty() && fluid.isFluidEqual(new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME))) {
			if (BucketHelper.getFluidAmount(stack) >= FluidAttributes.BUCKET_VOLUME)
				return 20000;
		}

		return -1;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		int capacity = this.getCapacity(stack) / FluidAttributes.BUCKET_VOLUME;
		int buckets = BucketHelper.getFluidAmount(stack) / FluidAttributes.BUCKET_VOLUME;
		FluidStack fluid = this.getFluid(stack);

		ITextComponent fluidName = fluid.isEmpty() ? ModTooltips.EMPTY.build() : fluid.getDisplayName();

		if (!fluid.isEmpty() && FluidHelper.getFluidRarity(fluid) != Rarity.COMMON) {
			fluidName = new StringTextComponent(FluidHelper.getFluidRarity(fluid).color.toString() + fluidName);
		}

		tooltip.add(ModTooltips.BUCKETS.args(buckets, capacity, fluidName).build());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		FluidStack fluid = this.getFluid(stack);

		ActionResult<ItemStack> pickup = this.tryPickupFluid(stack, world, player);

		if (pickup.getType() == ActionResultType.SUCCESS) {
			return pickup;
		} else {
			if (!fluid.isEmpty() && fluid.getAmount() >= FluidAttributes.BUCKET_VOLUME) {
				return this.tryPlaceFluid(stack, world, player, hand);
			} else {
				return new ActionResult<>(ActionResultType.FAIL, stack);
			}
		}
	}
	
//	@Override // TODO: IMPLEMENT
//	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
//		if (target instanceof CowEntity) {
//			CowEntity cow = (CowEntity) target;
//			if (!player.isCreative() && !cow.isChild()) {
//				Fluid milk = ForgeRegistries.FLUIDS.getFluid("milk");
//				return milk != null && this.fill(stack, new FluidStack(milk, 1000), true) > 0;
//			}
//		}
//
//		return false;
//	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT tag) {
		return new FluidHolderItemWrapper(stack, this);
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
		FluidStack bucketFluid = this.getFluid(stack);
		if (!bucketFluid.isEmpty() && !fluid.isFluidEqual(bucketFluid))
			return 0;

		int capacity = this.getCapacity(stack);

		if (!canFill) {
			if (bucketFluid.isEmpty())
				return BucketHelper.toBuckets(Math.min(capacity, fluid.getAmount()));

			return BucketHelper.toBuckets(Math.min(capacity - bucketFluid.getAmount(), fluid.getAmount()));
		}

		int filled = BucketHelper.toBuckets(Math.min(fluid.getAmount(), capacity));

		if (bucketFluid.isEmpty()) {
			CompoundNBT fluidTag = fluid.writeToNBT(new CompoundNBT());
			fluidTag.putInt("Amount", filled);
			stack.setTag(fluidTag);

			return filled;
		}

		filled = BucketHelper.toBuckets(capacity - bucketFluid.getAmount());
		int amount = BucketHelper.toBuckets(fluid.getAmount());

		if (amount < filled) {
			bucketFluid.setAmount(bucketFluid.getAmount() + amount);
			filled = amount;
		} else {
			bucketFluid.setAmount(capacity);
		}

		bucketFluid.writeToNBT(stack.getOrCreateTag());

		return filled;
	}

	@Override
	public FluidStack drain(ItemStack stack, int amount, boolean canDrain) {
		if (amount == 0) return FluidStack.EMPTY;

		FluidStack fluid = this.getFluid(stack);
		if (fluid.isEmpty()) return FluidStack.EMPTY;

		int drained = BucketHelper.toBuckets(Math.min(fluid.getAmount(), amount));

		if (canDrain) {
			if (amount >= fluid.getAmount()) {
				NBTHelper.removeTag(stack, "FluidName");
				NBTHelper.removeTag(stack, "Amount");
				return fluid;
			}

			fluid.setAmount(fluid.getAmount() - drained);
			fluid.writeToNBT(stack.getOrCreateTag());
		}

		fluid.setAmount(drained);
		return fluid;
	}
	
	public int getSpaceLeft(ItemStack stack) {
		return this.getCapacity(stack) - BucketHelper.getFluidAmount(stack);
	}
	
	private ActionResult<ItemStack> tryPlaceFluid(ItemStack stack, World world, PlayerEntity player, Hand hand) {
		if (BucketHelper.getFluidAmount(stack) < FluidAttributes.BUCKET_VOLUME)
			return new ActionResult<>(ActionResultType.PASS, stack);

		RayTraceResult trace = rayTrace(world, player, RayTraceContext.FluidMode.NONE);
		if (trace.getType() != RayTraceResult.Type.BLOCK)
			return new ActionResult<>(ActionResultType.PASS, stack);

		BlockRayTraceResult blockTrace = (BlockRayTraceResult) trace;
		BlockPos pos = blockTrace.getPos();
		if (world.isBlockModifiable(player, pos)) {
			Direction face = blockTrace.getFace();
			BlockPos targetPos = pos.offset(face);
			if (player.canPlayerEdit(targetPos, face.getOpposite(), stack)) {
				FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, hand, pos.offset(face), stack, new FluidStack(this.getFluid(stack), FluidAttributes.BUCKET_VOLUME));
				if (result.isSuccess() && !player.isCreative()) {
					player.addStat(Stats.ITEM_USED.get(this));
					return new ActionResult<>(ActionResultType.SUCCESS, result.getResult());
				}
			}
		}

		return new ActionResult<>(ActionResultType.FAIL, stack);
	}

	private ActionResult<ItemStack> tryPickupFluid(ItemStack stack, World world, PlayerEntity player) {
		if (this.getSpaceLeft(stack) < FluidAttributes.BUCKET_VOLUME)
			return new ActionResult<>(ActionResultType.PASS, stack);

		RayTraceResult trace = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (trace.getType() != RayTraceResult.Type.BLOCK)
			return new ActionResult<>(ActionResultType.PASS, stack);

		BlockRayTraceResult blockTrace = (BlockRayTraceResult) trace;
		BlockPos pos = blockTrace.getPos();
		if (world.isBlockModifiable(player, pos)) {
			Direction face = blockTrace.getFace();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if (block instanceof IBucketPickupHandler && player.canPlayerEdit(pos.offset(face), face, stack)) {
				Fluid fluid = ((IBucketPickupHandler) block).pickupFluid(world, pos, state);
				if (fluid != Fluids.EMPTY) {
					player.addStat(Stats.ITEM_USED.get(this));

					SoundEvent soundevent = this.getFluid(stack).getFluid().getAttributes().getEmptySound();
					if (soundevent == null) soundevent = fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL;
					player.playSound(soundevent, 1.0F, 1.0F);
					this.fill(stack, new FluidStack(fluid, 1000), true);
					if (!world.isRemote()) {
						CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) player, new ItemStack(fluid.getFilledBucket()));
					}

					return new ActionResult<>(ActionResultType.SUCCESS, stack);
				}
			}
		}

		return new ActionResult<>(ActionResultType.FAIL, stack);
	}

	@Override
	public boolean isEnabled() {
		return this.requiredMods;
	}
}