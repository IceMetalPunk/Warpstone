package com.icemetalpunk.warpstone;

import com.icemetalpunk.warpstone.handlers.WarpstoneGameplayHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class WarpstoneCommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		this.registerHandlers();
	}

	public void init(FMLInitializationEvent e) {

	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public void registerHandlers() {
		MinecraftForge.EVENT_BUS.register(new WarpstoneGameplayHandler());
	}
}
