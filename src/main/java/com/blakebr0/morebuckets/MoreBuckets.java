package com.blakebr0.morebuckets;

import com.blakebr0.morebuckets.init.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MoreBuckets.MOD_ID)
public class MoreBuckets {
	public static final String MOD_ID = "morebuckets";
	public static final String NAME = "More Buckets";
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	public static final CreativeModeTab CREATIVE_TAB = new MBCreativeTab();

	public MoreBuckets() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);

		ModItems.REGISTRY.register(bus);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) { }
}
