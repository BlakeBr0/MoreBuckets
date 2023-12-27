package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.fluid.FluidHolderItemWrapper;
import com.blakebr0.cucumber.helper.FluidHelper;
import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.iface.IFluidHolder;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.morebuckets.bucket.Bucket;
import com.blakebr0.morebuckets.lib.ModTooltips;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class MoreBucketItem extends BaseItem implements IFluidHolder {
    public static final List<MoreBucketItem> BUCKETS = new ArrayList<>();

    private final Bucket bucket;

    public MoreBucketItem(Bucket bucket) {
        super(p -> p.stacksTo(1));
        this.bucket = bucket;

        DispenserBlock.registerBehavior(this, new DispenserBehavior());

        BUCKETS.add(this);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return FluidHelper.getFluidAmount(stack) > 0;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        var copy = new ItemStack(this);
        copy.setTag(stack.getTag());

        this.drain(copy, FluidType.BUCKET_VOLUME, true);

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
        int stored = FluidHelper.getFluidAmount(stack);

        float f = Math.max(0.0F, (float) stored / (float) capacity);

        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.getCapacity(stack) > FluidType.BUCKET_VOLUME;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return isMilkBucket(stack) ? UseAnim.DRINK : UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return isMilkBucket(stack) ? 32 : 0;
    }

    @Override
    public int getBurnTime(ItemStack stack, RecipeType<?> type) {
        var fluid = this.getFluid(stack);
        if (fluid != null && fluid.isFluidEqual(new FluidStack(Fluids.LAVA, 1000))) {
            if (FluidHelper.getFluidAmount(stack) >= FluidType.BUCKET_VOLUME) {
                return 20000;
            }
        }

        return -1;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        var capacity = Formatting.number(this.bucket.getBuckets());
        int buckets = FluidHelper.getFluidAmount(stack) / FluidType.BUCKET_VOLUME;
        var fluid = FluidHelper.getFluidFromStack(stack);

        if (fluid.isEmpty()) {
            tooltip.add(ModTooltips.BUCKETS.args(buckets, capacity, ModTooltips.EMPTY.build()).build());
        } else {
            tooltip.add(ModTooltips.BUCKETS.args(buckets, capacity, fluid.getDisplayName()).build());
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (isMilkBucket(stack)) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        var fluid = this.getFluid(stack);
        var pickup = this.tryPickupFluid(stack, level, player);

        if (pickup.getResult() == InteractionResult.SUCCESS) {
            return pickup;
        } else {
            if (fluid != null && fluid.getAmount() >= FluidType.BUCKET_VOLUME) {
                return this.tryPlaceFluid(stack, level, player, hand);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }
    }

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Cow cow && !cow.isBaby()) {
            if (this.fill(stack, new FluidStack(ForgeMod.MILK.get(), FluidType.BUCKET_VOLUME), true) > 0) {
                player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
		}

		return InteractionResult.PASS;
	}

    @Override // copied from MilkBucketItem#finishUsingItem
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // change: pass in vanilla milk bucket to trick MobEffectInstance#isCurativeItem
        if (!level.isClientSide) entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
        if (entity instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            // change: instead of shrinking the stack we drain the fluid
            this.drain(stack, FluidType.BUCKET_VOLUME, true);
        }

        return stack;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag tag) {
        return new FluidHolderItemWrapper(stack, this);
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return this.bucket.getCapacity();
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
            bucketFluid.grow(amount);
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

    public int getSpaceLeft(ItemStack stack) {
        return this.getCapacity(stack) - FluidHelper.getFluidAmount(stack);
    }

    public boolean isEnabled() {
        return this.bucket.isEnabled();
    }

    private InteractionResultHolder<ItemStack> tryPlaceFluid(ItemStack stack, Level level, Player player, InteractionHand hand) {
        if (FluidHelper.getFluidAmount(stack) < FluidType.BUCKET_VOLUME)
            return InteractionResultHolder.pass(stack);

        var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (trace.getType() != HitResult.Type.BLOCK)
            return InteractionResultHolder.pass(stack);

        var pos = trace.getBlockPos();
        if (level.mayInteract(player, pos)) {
            var targetPos = pos.relative(trace.getDirection());

            if (player.mayUseItemAt(targetPos, trace.getDirection().getOpposite(), stack)) {
                var result = FluidUtil.tryPlaceFluid(player, level, hand, targetPos, stack, new FluidStack(this.getFluid(stack), FluidType.BUCKET_VOLUME));
                if (result.isSuccess() && !player.getAbilities().instabuild) {
                    if (!level.isClientSide()) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, stack);
                    }

                    return InteractionResultHolder.success(result.getResult());
                }
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    private InteractionResultHolder<ItemStack> tryPickupFluid(ItemStack stack, Level level, Player player) {
        if (this.getSpaceLeft(stack) < FluidType.BUCKET_VOLUME)
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
                    if (!level.isClientSide()) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, stack);
                    }

                    return InteractionResultHolder.success(result.getResult());
                }
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    private static boolean isMilkBucket(ItemStack stack) {
        return FluidHelper.getFluidFromStack(stack).getFluid() == ForgeMod.MILK.get();
    }

    private static class DispenserBehavior extends OptionalDispenseItemBehavior {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            var level = source.getLevel();
            var facing = source.getBlockState().getValue(DispenserBlock.FACING);
            var pos = source.getPos().relative(facing);

            var action = FluidUtil.tryPickUpFluid(stack, null, level, pos, facing.getOpposite());
            var resultStack = action.getResult();

            if (!action.isSuccess() || resultStack.isEmpty()) {
                var singleStack = StackHelper.withSize(stack, 1, false);

                var fluidHandler = FluidUtil.getFluidHandler(singleStack).resolve();
                if (fluidHandler.isEmpty()) return super.execute(source, stack);

                var fluidStack = fluidHandler.get().drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                var result = !fluidStack.isEmpty() ? FluidUtil.tryPlaceFluid(null, level, InteractionHand.MAIN_HAND, pos, stack, fluidStack) : FluidActionResult.FAILURE;

                if (result.isSuccess()) {
                    var drainedStack = result.getResult();

                    if (drainedStack.getCount() == 1) {
                        return drainedStack;
                    } else if (!drainedStack.isEmpty() && ((DispenserBlockEntity) source.getEntity()).addItem(drainedStack) < 0) {
                        this.dispense(source, drainedStack);
                    }

                    return StackHelper.shrink(drainedStack, 1, false);
                } else {
                    return this.dispense(source, stack);
                }
            } else {
                if (stack.getCount() == 1) {
                    return resultStack;
                } else if (((DispenserBlockEntity) source.getEntity()).addItem(resultStack) < 0) {
                    this.dispense(source, resultStack);
                }
            }

            return resultStack;
        }
    }
}