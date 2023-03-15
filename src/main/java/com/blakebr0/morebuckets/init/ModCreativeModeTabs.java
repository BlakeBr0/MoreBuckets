package com.blakebr0.morebuckets.init;

import com.blakebr0.cucumber.util.FeatureFlagDisplayItemGenerator;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.bucket.Bucket;
import com.blakebr0.morebuckets.lib.ModBuckets;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeModeTabs {
    @SubscribeEvent
    public void onRegisterCreativeModeTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MoreBuckets.MOD_ID, "creative_mode_tab"), (builder) -> {
            var displayItems = FeatureFlagDisplayItemGenerator.create((parameters, output) -> {
                accept(output, ModItems.COPPER_BUCKET, ModBuckets.COPPER);
                accept(output, ModItems.QUARTZ_BUCKET, ModBuckets.QUARTZ);
                accept(output, ModItems.OBSIDIAN_BUCKET, ModBuckets.OBSIDIAN);
                accept(output, ModItems.GOLD_BUCKET, ModBuckets.GOLD);
                accept(output, ModItems.EMERALD_BUCKET, ModBuckets.EMERALD);
                accept(output, ModItems.DIAMOND_BUCKET, ModBuckets.DIAMOND);

                accept(output, ModItems.ALUMINUM_BUCKET, ModBuckets.ALUMINUM);
                accept(output, ModItems.TIN_BUCKET, ModBuckets.TIN);
                accept(output, ModItems.RUBBER_BUCKET, ModBuckets.RUBBER);
                accept(output, ModItems.SILVER_BUCKET, ModBuckets.SILVER);
                accept(output, ModItems.BRONZE_BUCKET, ModBuckets.BRONZE);
                accept(output, ModItems.STEEL_BUCKET, ModBuckets.STEEL);

                accept(output, ModItems.INFERIUM_BUCKET, ModBuckets.INFERIUM);
                accept(output, ModItems.PRUDENTIUM_BUCKET, ModBuckets.PRUDENTIUM);
                accept(output, ModItems.TERTIUM_BUCKET, ModBuckets.TERTIUM);
                accept(output, ModItems.IMPERIUM_BUCKET, ModBuckets.IMPERIUM);
                accept(output, ModItems.SUPREMIUM_BUCKET, ModBuckets.SUPREMIUM);
                accept(output, ModItems.INSANIUM_BUCKET, ModBuckets.INSANIUM);
            });

            builder.title(Component.translatable("itemGroup.morebuckets"))
                    .icon(() -> new ItemStack(ModItems.DIAMOND_BUCKET.get()))
                    .displayItems(displayItems);
        });
    }

    private static void accept(FeatureFlagDisplayItemGenerator.Output output, RegistryObject<Item> item, Bucket bucket) {
        if (bucket.isEnabled()) {
            output.accept(item.get());
        }
    }
}
