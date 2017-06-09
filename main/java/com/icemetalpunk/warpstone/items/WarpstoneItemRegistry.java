package com.icemetalpunk.warpstone.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class WarpstoneItemRegistry {

	protected final HashMap<String, WarpstoneItemBase> itemList = new HashMap<String, WarpstoneItemBase>();

	public WarpstoneItemRegistry() {

		itemList.put("warpstone_pebble", new WarpstoneItemWarpstonePebble());
		itemList.put("warp_key", new WarpstoneItemWarpKey());

		registerItems();
		registerOres();
		registerRecipes();
	}

	public WarpstoneItemBase get(String name) {
		return this.itemList.get(name);
	}

	protected void registerItems() {
		for (Map.Entry<String, WarpstoneItemBase> entry : itemList.entrySet()) {
			WarpstoneItemBase item = entry.getValue();
			GameRegistry.register(item);
		}
	}

	protected void registerOres() {
		String[] ores = new String[0];
		for (Map.Entry<String, WarpstoneItemBase> entry : itemList.entrySet()) {
			WarpstoneItemBase item = entry.getValue();
			ores = item.getOreDict();
			for (String ore : ores) {
				OreDictionary.registerOre(ore, item);
			}
		}
	}

	protected void registerRecipes() {
		List<IRecipe> recipes = null;
		for (Map.Entry<String, WarpstoneItemBase> entry : itemList.entrySet()) {
			WarpstoneItemBase item = entry.getValue();
			recipes = item.getRecipes();
			for (IRecipe recipe : recipes) {
				GameRegistry.addRecipe(recipe);
			}
		}
	}

	public void registerModels() {
		for (Map.Entry<String, WarpstoneItemBase> entry : itemList.entrySet()) {
			WarpstoneItemBase item = entry.getValue();
			item.registerModel();
		}
	}
}
