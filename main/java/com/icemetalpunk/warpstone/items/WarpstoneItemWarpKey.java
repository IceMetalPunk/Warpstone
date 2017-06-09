package com.icemetalpunk.warpstone.items;

import java.util.ArrayList;
import java.util.List;

import com.icemetalpunk.warpstone.Warpstone;
import com.icemetalpunk.warpstone.blocks.WarpstoneBlockWarpPad;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class WarpstoneItemWarpKey extends WarpstoneItemBase {

	public WarpstoneItemWarpKey() {
		super("warp_key");
		this.maxStackSize = 1;
	}

	@Override
	public String[] getOreDict() {
		return new String[0];
	}

	@Override
	public List<IRecipe> getRecipes() {
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		recipes.add(new ShapedOreRecipe(this, " P ", "PGP", " G ", 'P', "pebbleWarpstone", 'G', "nuggetGold"));
		return recipes;
	}

	// Warp key functionality
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (worldIn.isRemote) {
			return EnumActionResult.PASS;
		}

		ItemStack held = player.getHeldItem(hand);
		IBlockState state = worldIn.getBlockState(pos);
		Block padBlock = Warpstone.blocks.get("warp_pad");

		if (state.getBlock() != padBlock) {
			return EnumActionResult.PASS;
		}

		System.out.println("Warp key!");

		if (!held.hasTagCompound()) {
			held.setTagCompound(new NBTTagCompound());
		}

		// FIXME: Once set, coordinates can't be changed?
		if (player.isSneaking()) {
			System.out.println("Sneaking!");
			held.getTagCompound().setIntArray("Coordinates", new int[] { pos.getX(), pos.getY(), pos.getZ() });
		}
		else if (state.getValue(WarpstoneBlockWarpPad.STATE) == WarpstoneBlockWarpPad.StateEnum.UNLINKED
				&& held.hasTagCompound() && held.getTagCompound().hasKey("Coordinates")) {
			System.out.println("Link this!");
			int[] destArray = held.getTagCompound().getIntArray("Coordinates");
			BlockPos dest = new BlockPos(destArray[0], destArray[1], destArray[2]);
			held.splitStack(held.getMaxStackSize());

			Warpstone.warpMap.forcePut(pos, dest);

			worldIn.setBlockState(pos,
					state.withProperty(WarpstoneBlockWarpPad.STATE, WarpstoneBlockWarpPad.StateEnum.LINK_SEND));
			IBlockState existingState = worldIn.getBlockState(dest);
			Block existingBlock = existingState.getBlock();
			if (existingBlock == Warpstone.blocks.get("warp_pad")) {
				worldIn.setBlockState(dest, existingState.withProperty(WarpstoneBlockWarpPad.STATE,
						WarpstoneBlockWarpPad.StateEnum.LINK_RECIEVE));
			}

		}
		return EnumActionResult.PASS;
	}

}
