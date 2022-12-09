package com.blakebr0.morebuckets.init;

import com.blakebr0.cucumber.util.FeatureFlagDisplayItemGenerator;
import com.blakebr0.morebuckets.MoreBuckets;
import com.blakebr0.morebuckets.config.ModConfigs;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModCreativeModeTabs {
    @SubscribeEvent
    public void onRegisterCreativeModeTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MoreBuckets.MOD_ID, "creative_mode_tab"), (builder) -> {
            var displayItems = FeatureFlagDisplayItemGenerator.create((flagSet, output, hasPermission) -> {
                output.accept(ModItems.COPPER_BUCKET);
                output.accept(ModItems.QUARTZ_BUCKET);
                output.accept(ModItems.OBSIDIAN_BUCKET);
                output.accept(ModItems.GOLD_BUCKET);
                output.accept(ModItems.EMERALD_BUCKET);
                output.accept(ModItems.DIAMOND_BUCKET);

                output.accept(ModItems.ALUMINUM_BUCKET);
                output.accept(ModItems.TIN_BUCKET);
                output.accept(ModItems.RUBBER_BUCKET);
                output.accept(ModItems.SILVER_BUCKET);
                output.accept(ModItems.BRONZE_BUCKET);
                output.accept(ModItems.STEEL_BUCKET);

                if (ModConfigs.isMysticalAgricultureInstalled()) {
                    output.accept(ModItems.INFERIUM_BUCKET);
                    output.accept(ModItems.PRUDENTIUM_BUCKET);
                    output.accept(ModItems.TERTIUM_BUCKET);
                    output.accept(ModItems.IMPERIUM_BUCKET);
                    output.accept(ModItems.SUPREMIUM_BUCKET);
                }

                if (ModConfigs.isMysticalAgradditionsInstalled()) {
                    output.accept(ModItems.INSANIUM_BUCKET);
                }
            });

            builder.title(Component.translatable("itemGroup.morebuckets"))
                    .icon(() -> new ItemStack(ModItems.DIAMOND_BUCKET.get()))
                    .displayItems(displayItems)
                    .build();
        });
    }
}
