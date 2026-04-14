package com.blakebr0.morebuckets.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.morebuckets.bucket.Bucket;
import com.blakebr0.morebuckets.init.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MoreBucketItem extends BaseItem {
    public static final List<MoreBucketItem> BUCKETS = new ArrayList<>();

    private final Bucket bucket;

    public MoreBucketItem(Identifier id, Bucket bucket) {
        super(id, p -> p
                .stacksTo(1)
                .component(ModDataComponentTypes.BUCKET_CONTENT, SimpleFluidContent.EMPTY)
        );
        this.bucket = bucket;

        DispenserBlock.registerBehavior(this, new DispenserBehavior());

        BUCKETS.add(this);
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        var copy = new ItemStack(this);

        copy.applyComponents(instance.typeHolder().components());

        var tank = ItemAccess.forStack(copy).getCapability(Capabilities.Fluid.ITEM);
        if (tank != null) {
            var resource = tank.getResource(0);

            try (var tx = Transaction.openRoot()) {
                tank.extract(resource, FluidType.BUCKET_VOLUME, tx);

                tx.commit();
            }
        }

        return ItemStackTemplate.fromNonEmptyStack(copy);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int capacity = this.bucket.getCapacity();
        int stored = capacity - FluidUtil.getFirstStackContained(stack).getAmount();

        return Math.round(13.0F - stored * 13.0F / (float) capacity);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int capacity = this.bucket.getCapacity();
        int stored = FluidUtil.getFirstStackContained(stack).getAmount();

        float f = Math.max(0.0F, (float) stored / (float) capacity);

        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.bucket.getCapacity() > FluidType.BUCKET_VOLUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return isMilkBucket(stack) ? ItemUseAnimation.DRINK : ItemUseAnimation.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return isMilkBucket(stack) ? 32 : 0;
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> type, FuelValues fuelValues) {
        var fluid = FluidUtil.getFirstStackContained(stack);
        if (fluid.is(Fluids.LAVA)) {
            if (fluid.getAmount() >= FluidType.BUCKET_VOLUME) {
                return 20000;
            }
        }

        return super.getBurnTime(stack, type, fuelValues);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        var capacity = this.bucket.getBuckets();
        var fluid = FluidUtil.getFirstStackContained(stack);
        var buckets = fluid.getAmount() / FluidType.BUCKET_VOLUME;

        if (fluid.isEmpty()) {
            builder.accept(Component.literal("%s/%s - %s".formatted(buckets, capacity, Tooltips.EMPTY.toString())).withStyle(ChatFormatting.GRAY));
        } else {
            builder.accept(Component.literal("%s/%s - %s".formatted(buckets, capacity, fluid.getHoverName().getString())).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (isMilkBucket(stack)) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        var tank = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
        if (tank == null) {
            return InteractionResult.FAIL;
        }

        var pickup = this.tryPickupFluid(stack, level, player);
        if (pickup == InteractionResult.SUCCESS) {
            return pickup;
        } else {
            var fluid = FluidUtil.getFirstStackContained(stack);
            if (!fluid.isEmpty() && fluid.getAmount() >= FluidType.BUCKET_VOLUME) {
                return this.tryPlaceFluid(stack, level, player, hand);
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Cow cow && !cow.isBaby()) {
            var tank = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);

            try (var tx = Transaction.openRoot()) {
                if (tank != null && tank.insert(FluidResource.of(NeoForgeMod.MILK.get()), FluidType.BUCKET_VOLUME, tx) == FluidType.BUCKET_VOLUME) {
                    player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                    tx.commit();
                    return InteractionResult.SUCCESS;
                }
            }
		}

		return InteractionResult.PASS;
	}

    @Override // copied from MilkBucketItem#finishUsingItem
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide()) {
            entity.removeAllEffects();
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            // change: instead of shrinking the stack, we drain the fluid
            var tank = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
            if (tank != null) {
                var resource = tank.getResource(0);

                try (var tx = Transaction.openRoot()) {
                    tank.extract(resource, FluidType.BUCKET_VOLUME, tx);
                    tx.commit();
                }
            }
        }

        return stack;
    }

    public int getCapacity() {
        return this.bucket.getCapacity();
    }

    public int getSpaceLeft(ItemStack stack) {
        return this.bucket.getCapacity() - FluidUtil.getFirstStackContained(stack).getAmount();
    }

    private InteractionResult tryPlaceFluid(ItemStack stack, Level level, Player player, InteractionHand hand) {
        if (FluidUtil.getFirstStackContained(stack).getAmount() < FluidType.BUCKET_VOLUME)
            return InteractionResult.PASS;

        var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (trace.getType() != HitResult.Type.BLOCK)
            return InteractionResult.PASS;

        var pos = trace.getBlockPos();
        if (level.mayInteract(player, pos)) {
            var targetPos = pos.relative(trace.getDirection());

            if (player.mayUseItemAt(targetPos, trace.getDirection().getOpposite(), stack)) {
                var tank = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
                var result = FluidUtil.tryPlaceFluid(tank, player, level, hand, targetPos);
                if (!result.isEmpty() && !player.getAbilities().instabuild) {
                    if (!level.isClientSide()) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, stack);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.FAIL;
    }

    private InteractionResult tryPickupFluid(ItemStack stack, Level level, Player player) {
        if (this.getSpaceLeft(stack) < FluidType.BUCKET_VOLUME)
            return InteractionResult.PASS;

        var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (trace.getType() != HitResult.Type.BLOCK)
            return InteractionResult.PASS;

        var pos = trace.getBlockPos();
        if (level.mayInteract(player, pos)) {
            var direction = trace.getDirection();
            if (player.mayUseItemAt(pos, direction, stack)) {
                var tank = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
                var result = FluidUtil.tryPickupFluid(tank, player, level, pos, direction);

                if (!result.isEmpty() && !player.getAbilities().instabuild) {
                    if (!level.isClientSide()) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, stack);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.FAIL;
    }

    private static boolean isMilkBucket(ItemStack stack) {
        return FluidUtil.getFirstStackContained(stack).getFluid() == NeoForgeMod.MILK.get();
    }

    private static class DispenserBehavior extends OptionalDispenseItemBehavior {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            var level = source.level();
            var facing = source.state().getValue(DispenserBlock.FACING);
            var pos = source.pos().relative(facing);

            var tank = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
            var pickup = FluidUtil.tryPickupFluid(tank, null, level, pos, facing.getOpposite());

            // didn't pick up anything
            if (pickup.isEmpty()) {
                var result = FluidUtil.tryPlaceFluid(tank, null, level, InteractionHand.MAIN_HAND, pos);
                if (!result.isEmpty()) {
                    return stack;
                } else {
                    return this.dispense(source, stack);
                }
            }

            return stack;
        }
    }
}