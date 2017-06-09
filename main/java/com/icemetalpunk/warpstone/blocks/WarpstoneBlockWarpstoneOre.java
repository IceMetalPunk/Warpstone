package com.icemetalpunk.warpstone.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.icemetalpunk.warpstone.Warpstone;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class WarpstoneBlockWarpstoneOre extends WarpstoneBlockBase {

	public WarpstoneBlockWarpstoneOre() {
		super(Material.ROCK, "warpstone_ore");
		this.setHardness(3.0F).setResistance(5.0F);
		this.setHarvestLevel("pickaxe", ToolMaterial.DIAMOND.getHarvestLevel());
	}

	@Override
	public String[] getOreDict() {
		return new String[] { "oreWarpstone" };
	}

	@Override
	public List<IRecipe> getRecipes() {
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		recipes.add(new ShapedOreRecipe(this, "SBS", "GPG", "SBS", 'S', "stone", 'B', Items.BLAZE_POWDER, 'G',
				"dustGlowstone", 'P', Items.ENDER_PEARL));
		return recipes;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Warpstone.items.get("warpstone_pebble");
	}

	@Override
	public int quantityDropped(Random random) {
		return 2;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return this.quantityDropped(random) + fortune;
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		if (this.getItemDropped(state, RANDOM, fortune) != Item.getItemFromBlock(this)) {
			return 1 + RANDOM.nextInt(5);
		}
		return 0;
	}

	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this);
	}

}
