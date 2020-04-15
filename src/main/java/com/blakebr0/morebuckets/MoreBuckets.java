package com.blakebr0.morebuckets;

import com.blakebr0.morebuckets.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
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

	public static final ItemGroup ITEM_GROUP = new MBItemGroup();
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	public MoreBuckets() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);
		bus.register(new ModItems());
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {

	}
}
