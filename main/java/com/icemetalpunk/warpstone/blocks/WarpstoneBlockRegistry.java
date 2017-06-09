package com.icemetalpunk.warpstone.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class WarpstoneBlockRegistry {

	protected final HashMap<String, WarpstoneBlockBase> blockList = new HashMap<String, WarpstoneBlockBase>();
	protected final List<ItemBlock> itemBlocks = new ArrayList<ItemBlock>();

	public WarpstoneBlockRegistry() {

		blockList.put("warpstone_ore", new WarpstoneBlockWarpstoneOre());
		blockList.put("warpstone_block", new WarpstoneBlockWarpstoneBlock());
		blockList.put("warp_pad", new WarpstoneBlockWarpPad());

		registerBlocks();
		registerOres();
		registerRecipes();
	}

	public WarpstoneBlockBase get(String name) {
		return this.blockList.get(name);
	}

	protected void registerBlocks() {
		ItemBlock item;
		for (Map.Entry<String, WarpstoneBlockBase> entry : blockList.entrySet()) {
			WarpstoneBlockBase block = entry.getValue();
			item = new ItemBlock(block);
			item.setRegistryName(block.getRegistryName());
			item.setUnlocalizedName(block.getUnlocalizedName());
			itemBlocks.add(item);
			GameRegistry.register(block);
			GameRegistry.register(item);
		}
	}

	protected void registerOres() {
		String[] ores = new String[0];
		for (Map.Entry<String, WarpstoneBlockBase> entry : blockList.entrySet()) {
			WarpstoneBlockBase block = entry.getValue();
			ores = block.getOreDict();
			for (String ore : ores) {
				OreDictionary.registerOre(ore, block);
			}
		}
	}

	protected void registerRecipes() {
		List<IRecipe> recipes = null;
		for (Map.Entry<String, WarpstoneBlockBase> entry : blockList.entrySet()) {
			WarpstoneBlockBase block = entry.getValue();
			recipes = block.getRecipes();
			for (IRecipe recipe : recipes) {
				GameRegistry.addRecipe(recipe);
			}
		}
	}

	public void registerModels() {
		for (Map.Entry<String, WarpstoneBlockBase> entry : blockList.entrySet()) {
			WarpstoneBlockBase block = entry.getValue();
			block.registerModel();
		}
	}
}
