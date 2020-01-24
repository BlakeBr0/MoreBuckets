package com.blakebr0.morebuckets.lib;

import net.minecraftforge.fml.ModList;

public class ModChecker {
	// TODO: Move from static init into constructors
	public static final boolean MYSTICAL_AGRICULTURE = ModList.get().isLoaded("mysticalagriculture");
	public static final boolean MYSTICAL_AGRADDITIONS = ModList.get().isLoaded("mysticalagradditions");
	public static final boolean TINKERS_CONSTRUCT = ModList.get().isLoaded("tconstruct");
}
