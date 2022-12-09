package com.blakebr0.morebuckets.client.handler;

import com.blakebr0.morebuckets.item.MoreBucketItem;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ColorHandler {
    @SubscribeEvent
    public void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> {
            var fluid = ((MoreBucketItem) stack.getItem()).getFluid(stack);
            return tint == 1 ? IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid) : -1;
        }, MoreBucketItem.BUCKETS.toArray(new MoreBucketItem[0]));
    }
}
