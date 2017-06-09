package com.icemetalpunk.warpstone.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.icemetalpunk.warpstone.Warpstone;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class WarpstoneBlockWarpPad extends WarpstoneBlockBase {

	public static final PropertyEnum<WarpstoneBlockWarpPad.StateEnum> STATE = PropertyEnum.<WarpstoneBlockWarpPad
			.StateEnum> create("state", WarpstoneBlockWarpPad.StateEnum.class);
	protected static final AxisAlignedBB WARP_PAD_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.51D, 1.0D);

	public static enum StateEnum implements IStringSerializable {
		UNLINKED("unlinked"), LINK_SEND("link_send"), LINK_RECIEVE("link_recieve"), BROKEN("broken");

		private String name;

		StateEnum(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}
	};

	public WarpstoneBlockWarpPad() {
		super(Material.ROCK, "warp_pad");
		this.setHardness(3.0F).setResistance(5.0F);
		this.setHarvestLevel("pickaxe", ToolMaterial.IRON.getHarvestLevel());
		this.setDefaultState(this.blockState.getBaseState().withProperty(STATE, StateEnum.UNLINKED));
	}

	@Override
	public String[] getOreDict() {
		return new String[0];
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return WARP_PAD_AABB;
	}

	// Key handling
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		// FIXME: Triggering twice per click, so immediately popping out key.
		if (worldIn.isRemote) {
			return false;
		}

		ItemStack held = playerIn.getHeldItem(hand);

		System.out.println("Clicked warp pad with item " + held);
		System.out.println("State is " + state.getValue(STATE));

		if ((held == null || held == ItemStack.EMPTY || held.getItem() == Items.AIR)
				&& (state.getValue(STATE) == StateEnum.LINK_SEND || state.getValue(STATE) == StateEnum.BROKEN)) {
			worldIn.setBlockState(pos, state.withProperty(STATE, StateEnum.UNLINKED));
			ItemStack keyStack = new ItemStack(Warpstone.items.get("warp_key"), 1);

			System.out.println("New Stack");

			BlockPos destination = Warpstone.warpMap.get(pos);
			if (destination != null) {
				NBTTagCompound coordTag = new NBTTagCompound();
				coordTag.setIntArray("Coordinates",
						new int[] { destination.getX(), destination.getY(), destination.getZ() });
				keyStack.setTagCompound(coordTag);
			}

			EntityItem keyItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					keyStack);
			worldIn.spawnEntity(keyItem);
			return true;
		}
		return false;
	}

	@Override
	public List<IRecipe> getRecipes() {
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		recipes.add(new ShapedOreRecipe(this, "PPP", "PDP", "PPP", 'P', "pebbleWarpstone", 'D', "gemDiamond"));
		return recipes;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		IBlockState state = worldIn.getBlockState(pos);
		if (entityIn instanceof EntityPlayer && state.getValue(STATE) == StateEnum.LINK_SEND) {
			BlockPos dest = Warpstone.warpMap.get(pos);
			if (dest != null) {

				// TODO: Cost exponential amounts of XP, and interpolate
				// position if XP is not enough to reach the destination.

				entityIn.setPositionAndUpdate(dest.getX() + 0.5, dest.getY() + 0.5, dest.getZ() + 0.5);
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		if (state.getValue(STATE) == StateEnum.LINK_RECIEVE) {
			BlockPos dest = Warpstone.warpMap.inverse().get(pos);
			if (dest != null) {
				IBlockState currentState = worldIn.getBlockState(dest);
				if (currentState.getBlock() == this) {
					worldIn.setBlockState(dest, currentState.withProperty(STATE, StateEnum.BROKEN));
				}
			}
		}
		else if (state.getValue(STATE) == StateEnum.LINK_SEND) {
			BlockPos dest = Warpstone.warpMap.get(pos);
			if (dest != null) {
				IBlockState currentState = worldIn.getBlockState(dest);
				if (currentState.getBlock() == this) {
					worldIn.setBlockState(dest, currentState.withProperty(STATE, StateEnum.UNLINKED));
				}

				ItemStack keyStack = new ItemStack(Warpstone.items.get("warp_key"));
				NBTTagCompound coordTag = new NBTTagCompound();
				coordTag.setIntArray("Coordinates", new int[] { dest.getX(), dest.getY(), dest.getZ() });
				keyStack.setTagCompound(coordTag);

				EntityItem keyItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						keyStack);
				worldIn.spawnEntity(keyItem);

				Warpstone.warpMap.remove(pos);

			}
		}

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

	/* Block state implementation */
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(STATE, StateEnum.values()[meta]);
	}

	public int getMetaFromState(IBlockState state) {
		return state.getValue(STATE).ordinal();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { STATE });
	}

}
