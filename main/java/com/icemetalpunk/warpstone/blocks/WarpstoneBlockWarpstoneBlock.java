package com.icemetalpunk.warpstone.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.icemetalpunk.warpstone.Warpstone;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class WarpstoneBlockWarpstoneBlock extends WarpstoneBlockBase {

	public WarpstoneBlockWarpstoneBlock() {
		super(Material.ROCK, "warpstone_block");
		this.setHardness(3.0F).setResistance(5.0F);
		this.setHarvestLevel("pickaxe", ToolMaterial.IRON.getHarvestLevel());
	}

	@Override
	public String[] getOreDict() {
		return new String[] { "blockWarpstone" };
	}

	@Override
	public List<IRecipe> getRecipes() {
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		recipes.add(new ShapedOreRecipe(this, "SSS", "SSS", "SSS", 'S', "pebbleWarpstone"));
		return recipes;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return this.quantityDropped(random);
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		return 0;
	}

	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this);
	}

}
