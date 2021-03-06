package com.icemetalpunk.warpstone.blocks;

import java.util.ArrayList;
import java.util.List;

import com.icemetalpunk.warpstone.Warpstone;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.model.ModelLoader;

public class WarpstoneBlockBase extends Block {

	public WarpstoneBlockBase(Material mat, String name) {
		super(mat);
		this.setRegistryName(Warpstone.MODID, name).setUnlocalizedName(name).setCreativeTab(Warpstone.tab);
	}

	/*
	 * Return an array of string ore names to register this block as. Return an
	 * empty array if this shouldn't be registered as an ore.
	 */
	public String[] getOreDict() {
		return new String[0];
	}

	/*
	 * Return a list of IRecipes if this can be crafted, or an empty list if it
	 * can't. Recipes should output some variant of "this" block.
	 */
	public List<IRecipe> getRecipes() {
		return new ArrayList<IRecipe>();
	}

	public void registerModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}
}
