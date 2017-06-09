package com.icemetalpunk.warpstone.handlers;

import com.icemetalpunk.warpstone.Warpstone;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WarpstoneModelHandler {
	@SubscribeEvent
	public void modelRegistry(ModelRegistryEvent event) {
		Warpstone.blocks.registerModels();
		Warpstone.items.registerModels();
	}
}
