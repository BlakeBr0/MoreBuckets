package com.blakebr0.morebuckets;

import com.blakebr0.morebuckets.config.ModConfigs;
import com.blakebr0.morebuckets.crafting.RecipeFixer;
import com.blakebr0.morebuckets.init.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MoreBuckets.MOD_ID)
public class MoreBuckets {
	public static final String MOD_ID = "morebuckets";
	public static final String NAME = "More Buckets";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreativeModeTab CREATIVE_TAB = new MBCreativeTab();

	public MoreBuckets() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);

		ModItems.REGISTRY.register(bus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new RecipeFixer());
	}
}
