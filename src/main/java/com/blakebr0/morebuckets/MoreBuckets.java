package com.blakebr0.morebuckets;

import com.blakebr0.morebuckets.config.ModConfigs;
import com.blakebr0.morebuckets.handler.RegisterCapabilityHandler;
import com.blakebr0.morebuckets.init.ModConditionSerializers;
import com.blakebr0.morebuckets.init.ModCreativeModeTabs;
import com.blakebr0.morebuckets.init.ModDataComponentTypes;
import com.blakebr0.morebuckets.init.ModItems;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MoreBuckets.MOD_ID)
public class MoreBuckets {
	public static final String MOD_ID = "morebuckets";
	public static final String NAME = "More Buckets";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public MoreBuckets(IEventBus bus, ModContainer mod) {
		bus.register(new RegisterCapabilityHandler());

		ModItems.REGISTRY.register(bus);
		ModCreativeModeTabs.REGISTRY.register(bus);
		ModDataComponentTypes.REGISTRY.register(bus);
		ModConditionSerializers.REGISTRY.register(bus);

		NeoForgeMod.enableMilkFluid();

		mod.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON);
	}

	public static Identifier resource(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
