package com.blakebr0.morebuckets;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.morebuckets.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MoreBuckets.MOD_ID, name = MoreBuckets.NAME, version = MoreBuckets.VERSION, dependencies = MoreBuckets.DEPENDENCIES)
public class MoreBuckets {

	public static final String MOD_ID = "morebuckets";
	public static final String NAME = "More Buckets";
	public static final String VERSION = "${version}";
	public static final String DEPENDENCIES = "required-after:cucumber@[1.1.2,)";

	public static final ModRegistry REGISTRY = ModRegistry.create(MOD_ID);
	public static final CreativeTabs CREATIVE_TAB = new MBCreativeTab();
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@SidedProxy(clientSide = "com.blakebr0.morebuckets.proxy.ClientProxy", serverSide = "com.blakebr0.morebuckets.proxy.ServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
