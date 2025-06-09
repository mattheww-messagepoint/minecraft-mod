package com.funcation.trade.events;

import com.funcation.trade.registry.ModItems;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mod.EventBusSubscriber(modid = "funcation")
public class StartingInventoryHandler {
    // Control switch: set to 1, 2, or 3 to enable Tier 1, 2, or 3 starting items
    // Set to 0 to disable, or 4 to enable all tiers
    private static final int STARTING_TIER = 4; // Change this value to control which package is enabled

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            Player player = event.getEntity();
            // Tier 1 items - Early game resource exchanges
            if (STARTING_TIER == 1 || STARTING_TIER == 4) {
                player.getInventory().add(new ItemStack(Items.DIRT, 64));
                player.getInventory().add(new ItemStack(Items.GRAVEL, 32));
                player.getInventory().add(new ItemStack(Items.STICK, 32));
                player.getInventory().add(new ItemStack(Items.COBBLESTONE, 32));
                player.getInventory().add(new ItemStack(Items.OAK_SAPLING, 20));
                player.getInventory().add(new ItemStack(Items.SPRUCE_SAPLING, 10));
                player.getInventory().add(new ItemStack(Items.BIRCH_SAPLING, 10));
                player.getInventory().add(new ItemStack(Items.JUNGLE_SAPLING, 10));
                player.getInventory().add(new ItemStack(Items.ACACIA_SAPLING, 10));
                player.getInventory().add(new ItemStack(Items.DARK_OAK_SAPLING, 10));
                player.getInventory().add(new ItemStack(Items.MANGROVE_PROPAGULE, 10));
                player.getInventory().add(new ItemStack(Items.STONE_PICKAXE, 3));
                player.getInventory().add(new ItemStack(Items.STONE_AXE, 3));
                player.getInventory().add(new ItemStack(Items.STONE_SHOVEL, 3));
                player.getInventory().add(new ItemStack(Items.WHEAT_SEEDS, 40));
                player.getInventory().add(new ItemStack(Items.SAND, 32));
                player.getInventory().add(new ItemStack(Items.APPLE, 10));
                player.getInventory().add(new ItemStack(Items.OAK_LEAVES, 32));
                player.getInventory().add(new ItemStack(ModItems.TRADE_GUIDE_BOOK.get()));
            }
            // Tier 2 items - Mid-game resources and transitions
            if (STARTING_TIER == 2 || STARTING_TIER == 4) {
                player.getInventory().add(new ItemStack(Items.CHARCOAL, 32));
                player.getInventory().add(new ItemStack(Items.STONE_PICKAXE, 8));
                player.getInventory().add(new ItemStack(Items.STONE_AXE, 8));
                player.getInventory().add(new ItemStack(Items.STONE_SHOVEL, 8));
                player.getInventory().add(new ItemStack(Items.STONE_HOE, 8));
                player.getInventory().add(new ItemStack(Items.FLINT, 32));
                player.getInventory().add(new ItemStack(Items.ROTTEN_FLESH, 40));
                player.getInventory().add(new ItemStack(Items.WHEAT_SEEDS, 64));
                player.getInventory().add(new ItemStack(Items.OAK_SAPLING, 24));
                player.getInventory().add(new ItemStack(Items.SPRUCE_SAPLING, 24));
                player.getInventory().add(new ItemStack(Items.BIRCH_SAPLING, 24));
                player.getInventory().add(new ItemStack(Items.SAND, 16));
            }
            // Tier 3 items - Late game progression and advanced crafting alternatives
            if (STARTING_TIER == 3 || STARTING_TIER == 4) {
                player.getInventory().add(new ItemStack(Items.CHARCOAL, 64));
                player.getInventory().add(new ItemStack(Items.GRAVEL, 32));
                player.getInventory().add(new ItemStack(Items.STONE_PICKAXE, 24));
                player.getInventory().add(new ItemStack(Items.STONE_AXE, 24));
                player.getInventory().add(new ItemStack(Items.STONE_SHOVEL, 24));
                player.getInventory().add(new ItemStack(Items.STONE_HOE, 24));
                player.getInventory().add(new ItemStack(Items.FLINT, 48));
                player.getInventory().add(new ItemStack(Items.ROTTEN_FLESH, 64));
                player.getInventory().add(new ItemStack(Items.DIRT, 16));
                player.getInventory().add(new ItemStack(Items.OAK_SAPLING, 32));
                player.getInventory().add(new ItemStack(Items.SPRUCE_SAPLING, 32));
                player.getInventory().add(new ItemStack(Items.APPLE, 16));
                player.getInventory().add(new ItemStack(Items.TORCH, 32));
                player.getInventory().add(new ItemStack(Items.COPPER_INGOT, 8));
                player.getInventory().add(new ItemStack(Items.SAND, 32));
                player.getInventory().add(new ItemStack(Items.GLASS_PANE, 16));
                player.getInventory().add(new ItemStack(Items.STICK, 48));
            }
            // Basic tools and equipment for testing convenience
            if (STARTING_TIER > 0) {
                player.getInventory().add(new ItemStack(Items.DIAMOND_PICKAXE, 1));
                player.getInventory().add(new ItemStack(Items.DIAMOND_AXE, 1));
                player.getInventory().add(new ItemStack(Items.DIAMOND_SHOVEL, 1));
                player.getInventory().add(new ItemStack(Items.SHEARS, 1));  // For collecting leaves
                player.getInventory().add(new ItemStack(Items.BREAD, 32));  // Food for testing
            }
        }
    }
}
