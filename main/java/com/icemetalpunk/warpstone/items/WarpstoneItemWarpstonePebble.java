package com.icemetalpunk.warpstone.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class WarpstoneItemWarpstonePebble extends WarpstoneItemBase {

	public WarpstoneItemWarpstonePebble() {
		super("warpstone_pebble");
	}

	@Override
	public String[] getOreDict() {
		return new String[] { "pebbleWarpstone" };
	}

	@Override
	public List<IRecipe> getRecipes() {
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		recipes.add(new ShapelessOreRecipe(new ItemStack(this, 9), "blockWarpstone"));
		return recipes;
	}

}
