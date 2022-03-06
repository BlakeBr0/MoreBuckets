package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.fluid.FluidHolderItemWrapper;
import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.iface.IFluidHolder;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.morebuckets.lib.ModTooltips;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MoreBucketItem extends BaseItem implements IFluidHolder, IEnableable {
	private final int capacity;
	private final String[] requiredMods;

	public MoreBucketItem(int capacity, Function<Properties, Properties> properties) {
		this(capacity, true, properties);
	}
	
	public MoreBucketItem(int capacity, Function<Properties, Properties> properties, String... requiredMods) {
		this(capacity, true, properties, requiredMods);
	}
	
	public MoreBucketItem(int capacity, boolean recipeReplacement, Function<Properties, Properties> properties, String... requiredMods) {
		super(properties.compose(p -> p.stacksTo(1)));
		this.capacity = capacity;
		this.requiredMods = requiredMods;
//
//		if (this.isEnabled()) {
//			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseBucketBehavior.getInstance());
//
//			if (recipeReplacement) {
//				RecipeFixer.VALID_BUCKETS.add(this);
//			}
//		}
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return FluidHelper.getFluidAmount(stack) > 0;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		var copy = new ItemStack(this);
		copy.setTag(stack.getTag());

		this.drain(copy, FluidAttributes.BUCKET_VOLUME, true);

		return copy;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		int capacity = this.getCapacity(stack);
		int stored = capacity - FluidHelper.getFluidAmount(stack);

		return Math.round(13.0F - stored * 13.0F / (float) capacity);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		int capacity = this.getCapacity(stack);
		int stored = capacity - FluidHelper.getFluidAmount(stack);

		float f = Math.max(0.0F, (float) stored / (float) capacity);

		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return this.getCapacity(stack) > FluidAttributes.BUCKET_VOLUME;
	}

	@Override
	public int getBurnTime(ItemStack stack, RecipeType<?> type) {
		var fluid = this.getFluid(stack);
		if (fluid != null && fluid.isFluidEqual(new FluidStack(Fluids.LAVA, 1000))) {
			if (FluidHelper.getFluidAmount(stack) >= 1000) {
				return 20000;
			}
		}

		return -1;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		int capacity = this.getCapacity(stack) / 1000;
		int buckets = FluidHelper.getFluidAmount(stack) / 1000;
		var fluid = FluidHelper.getFluidFromStack(stack);

		tooltip.add(ModTooltips.BUCKETS.args(buckets, capacity, fluid.getDisplayName()).build());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var stack = player.getItemInHand(hand);
		var fluid = this.getFluid(stack);

		var pickup = this.tryPickupFluid(stack, level, player, hand);

		if (pickup.getResult() == InteractionResult.SUCCESS) {
			return pickup;
		} else {
			if (fluid != null && fluid.getAmount() >= FluidAttributes.BUCKET_VOLUME) {
				return this.tryPlaceFluid(stack, level, player, hand);
			} else {
				return InteractionResultHolder.fail(stack);
			}
		}
	}

	// TODO: milk
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

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag tag) {
		return new FluidHolderItemWrapper(stack, this);
	}

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

		var bucketFluid = this.getFluid(stack);
		if (!bucketFluid.isEmpty() && !fluid.isFluidEqual(bucketFluid))
			return 0;

		int capacity = this.getCapacity(stack);

		if (!canFill) {
			if (bucketFluid.isEmpty())
				return FluidHelper.toBuckets(Math.min(capacity, fluid.getAmount()));

			return FluidHelper.toBuckets(Math.min(capacity - bucketFluid.getAmount(), fluid.getAmount()));
		}

		int filled = FluidHelper.toBuckets(Math.min(fluid.getAmount(), capacity));

		if (bucketFluid.isEmpty()) {
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

		if (amount == 0) return FluidStack.EMPTY;

		var fluid = this.getFluid(stack);
		if (fluid.isEmpty()) return FluidStack.EMPTY;

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

	@Override
	public boolean isEnabled() {
		return this.requiredMods.length == 0 || Arrays.stream(this.requiredMods).anyMatch(ModList.get()::isLoaded);
	}

	public int getSpaceLeft(ItemStack stack) {
		return this.getCapacity(stack) - FluidHelper.getFluidAmount(stack);
	}

	private InteractionResultHolder<ItemStack> tryPlaceFluid(ItemStack stack, Level level, Player player, InteractionHand hand) {
		if (FluidHelper.getFluidAmount(stack) < FluidAttributes.BUCKET_VOLUME)
			return InteractionResultHolder.pass(stack);

		var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
		if (trace.getType() != HitResult.Type.BLOCK)
			return InteractionResultHolder.pass(stack);

		var pos = trace.getBlockPos();
		if (level.mayInteract(player, pos)) {
			var targetPos = pos.relative(trace.getDirection());

			if (player.mayUseItemAt(targetPos, trace.getDirection().getOpposite(), stack)) {
				var result = FluidUtil.tryPlaceFluid(player, level, hand, targetPos, stack, new FluidStack(this.getFluid(stack), FluidAttributes.BUCKET_VOLUME));
				if (result.isSuccess() && !player.getAbilities().instabuild) {
//					player.addStat(StatList.getObjectUseStats(this));
					return InteractionResultHolder.success(result.getResult());
				}
			}
		}

		return InteractionResultHolder.fail(stack);
	}

	private InteractionResultHolder<ItemStack> tryPickupFluid(ItemStack stack, Level level, Player player, InteractionHand hand) {
		if (this.getSpaceLeft(stack) < FluidAttributes.BUCKET_VOLUME)
			return InteractionResultHolder.pass(stack);

		var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		if (trace.getType() != HitResult.Type.BLOCK)
			return InteractionResultHolder.pass(stack);

		var pos = trace.getBlockPos();
		if (level.mayInteract(player, pos)) {
			var direction = trace.getDirection();
			if (player.mayUseItemAt(pos, direction, stack)) {
				var result = FluidUtil.tryPickUpFluid(stack, player, level, pos, direction);

				if (result.isSuccess() && !player.getAbilities().instabuild) {
//					player.addStat(StatList.getObjectUseStats(this));
					return InteractionResultHolder.success(result.getResult());
				}
			}
		}

		return InteractionResultHolder.fail(stack);
	}
}