package com.blakebr0.morebuckets.bucket;

import com.blakebr0.cucumber.helper.FluidHelper;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class BucketFluidHandlerItemStack extends ItemAccessFluidHandler {
    public BucketFluidHandlerItemStack(ItemAccess access, DataComponentType<SimpleFluidContent> component, int capacity) {
        super(access, component, capacity);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        var buckets = FluidHelper.toBuckets(amount);
        return super.insert(index, resource, buckets, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        var buckets = FluidHelper.toBuckets(amount);
        return super.extract(index, resource, buckets, transaction);
    }
}
