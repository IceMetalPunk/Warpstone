/*
 * TODO LIST:
 * 1. Warp Pads
 * 		1a. Crafting: Warpstone Pebbles around a diamond.
 * 		1b. Right-click with Warp Key to save coords to key.
 * 		1c. Shift-RC with Warp Key to put key inside and set coords.
 * 		1d. Shift-RC with empty hand to remove key.
 * 		1e. One-directional, only 2 linked at a time; global hashmap?
 * 		1f. When pad broken, unlink! Only sender will drop key when broken.
 * 		1g. Four states: unlinked, link-send, link-receive, and link-broken.
 * 		1h. Step on link-send to /tp to link-receive
 * 			1hI. Costs XP
 * 			1hII. Exponential cost over distance
 * 			1hIII. If you don't have enough XP, you'll travel the proportional distance and stop 
 * 
 * 2. Warp Keys
 * 		2a. Crafting: [_W_, WGW, _G_] | W = Warpstone Pebble, G = Gold Nugget
 * 		2b. Right-click on Warp Pad to save coords to key.
 * 		2c. Shift-RC on Warp Pad to put key inside it and set its coords.
 * 		2d. Shift-RC on Warp Pad with empty hand to retrieve the key.
 * 		2e. Displayed on the Warp Pad when inserted; red when link is broken.
 * 
 * 3. Spectral Sword
 * 		3a. Rare drop from vexes.
 * 		3b. Always has 1-5 durability; must be repaired through sword combination or mending.
 * 		3c. Can only hurt undead mobs and vexes (which aren't undead, they're just dead).
 * 		3d. Even if they deal no damage, they still apply glowing effect and take durability when hitting any mob/player.
 * 
 * 4. Warpal Sword
 * 		4a. Crafting: Warpstone Pebbles around a Spectral Sword
 * 		4b. Fully repairs sword when crafted; can hit anything, like a regular sword
 * 		4c. Right-click warps you forward in facing direction, even through walls
 * 		4d. Can be combined with a Warp Key in an anvil to become "bound".
 * 		4e. When bound warp sword hits a mob/player, they're teleported to the bound warp pad location.
 * 		4f. /tp'ing costs durability linear to the distance /tp'd
 * 		4g. If there's not enough durability left, the mob they hit is /tp'd proportional distance instead of all the way
 * 		4h. CANNOT HAVE MENDING ON IT!
 * 
 * 5. Warp Chest
 * 		5a. Crafting: Warpstone Pebbles around a hopper
 * 		5b. Teleports item entities from within a radius into it, even through walls
 * 
 * 6. Warpick / Warp Spade
 * 		6a. Crafting: Warpstone Pebbles around a diamond pickaxe / diamond shovel, resp.
 * 		6b. Acts like a diamond pickaxe/shovel, but teleports harvested drops to you from a radius.
 * 		6c. Can be combined with a Warp Key in an anvil to become "bound".
 * 		6d. Bound Warpicks / Warp Spades /tp drops to bound coordinates instead.
 * 		6e. Since warp pads are half-slabs, you can put a hopper under destination to collect items as soon as they load.
 * 		6f. Or you can put the pad near a Warp Chest to pull them in from nearby instead of from underneath. Your choice.
 * 
 */
package com.icemetalpunk.warpstone;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.icemetalpunk.warpstone.blocks.WarpstoneBlockRegistry;
import com.icemetalpunk.warpstone.handlers.WarpstoneModelHandler;
import com.icemetalpunk.warpstone.items.WarpstoneItemRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Warpstone.MODID, version = Warpstone.VERSION)
public class Warpstone {
	public static final String MODID = "warpstone";
	public static final String VERSION = "0.1";

	@Instance("Mod")
	public static Warpstone instance = new Warpstone();

	@SidedProxy(clientSide = "com.icemetalpunk.warpstone.WarpstoneClientProxy", serverSide = "com.icemetalpunk.warpstone.WarpstoneServerProxy")
	public static WarpstoneCommonProxy proxy;

	// Add Creative tab
	public static CreativeTabs tab = new CreativeTabs("warpstone") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Item.getItemFromBlock(Warpstone.blocks.get("warpstone_ore")));
		}

	};

	public static BiMap<BlockPos, BlockPos> warpMap = HashBiMap.create();

	// Add registries
	public static WarpstoneBlockRegistry blocks = new WarpstoneBlockRegistry();
	public static WarpstoneItemRegistry items = new WarpstoneItemRegistry();

	// Attach event handlers
	public Warpstone() {
		MinecraftForge.EVENT_BUS.register(new WarpstoneModelHandler());
	}

	// Handle inits
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
