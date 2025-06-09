package com.funcation.trade.data;

import com.funcation.trade.data.trades.TradeOffer;
import com.funcation.trade.player.PlayerTradeProgress;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all trade offers and trade tier progression for the Trade Block.
 * Provides access to trades by tier and handles unlocking logic.
 * @since 1.0.0
 */
public class TradeManager {
    private static final List<TradeOffer> TIER_1_TRADES = new ArrayList<>();
    private static final List<TradeOffer> TIER_2_TRADES = new ArrayList<>();
    private static final List<TradeOffer> TIER_3_TRADES = new ArrayList<>();

    // Config cache for unique trades required per tier
    private static final List<Integer> uniqueTradesRequiredPerTier = java.util.Arrays.asList(2, 3, 4); // fallback default

    static {
        // Tier 3: Late game progression and advanced crafting alternatives
        // 1. Charcoal + Gravel to Copper Ingot - Renewable to ore pathway
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 12),
                        new ItemStack(net.minecraft.world.item.Items.GRAVEL, 4)
                ),
                new ItemStack(net.minecraft.world.item.Items.COPPER_INGOT, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.GRAVEL, 4),
                        new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 12)
                ),
                new ItemStack(net.minecraft.world.item.Items.COPPER_INGOT, 1), 3, Integer.MAX_VALUE));

        // 2. Stone Tools + Flint to Iron Nugget - Tool recycling path
        // Creating four trades for the different stone tools
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.STONE_PICKAXE, 6),
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3),
                        new ItemStack(net.minecraft.world.item.Items.STONE_PICKAXE, 6)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.STONE_AXE, 6),
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3),
                        new ItemStack(net.minecraft.world.item.Items.STONE_AXE, 6)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.STONE_SHOVEL, 6),
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3),
                        new ItemStack(net.minecraft.world.item.Items.STONE_SHOVEL, 6)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.STONE_HOE, 6),
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 3),
                        new ItemStack(net.minecraft.world.item.Items.STONE_HOE, 6)
                ),
                new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 3, Integer.MAX_VALUE));

        // 3. Rotten Flesh + Dirt to Leather - Farming alternative
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.ROTTEN_FLESH, 10),
                        new ItemStack(net.minecraft.world.item.Items.DIRT, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.LEATHER, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.DIRT, 1),
                        new ItemStack(net.minecraft.world.item.Items.ROTTEN_FLESH, 10)
                ),
                new ItemStack(net.minecraft.world.item.Items.LEATHER, 1), 3, Integer.MAX_VALUE));

        // 4. Sapling + Apple to Golden Nugget - Woodland value
        // Creating trades for different sapling types
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.OAK_SAPLING, 3),
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1),
                        new ItemStack(net.minecraft.world.item.Items.OAK_SAPLING, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.SPRUCE_SAPLING, 3),
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1),
                        new ItemStack(net.minecraft.world.item.Items.SPRUCE_SAPLING, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.BIRCH_SAPLING, 3),
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1),
                        new ItemStack(net.minecraft.world.item.Items.BIRCH_SAPLING, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.JUNGLE_SAPLING, 3),
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1),
                        new ItemStack(net.minecraft.world.item.Items.JUNGLE_SAPLING, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.ACACIA_SAPLING, 3),
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1),
                        new ItemStack(net.minecraft.world.item.Items.ACACIA_SAPLING, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.DARK_OAK_SAPLING, 3),
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1),
                        new ItemStack(net.minecraft.world.item.Items.DARK_OAK_SAPLING, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.MANGROVE_PROPAGULE, 3),
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.APPLE, 1),
                        new ItemStack(net.minecraft.world.item.Items.MANGROVE_PROPAGULE, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1), 3, Integer.MAX_VALUE));

        // 5. Torch + Flint + Gravel to Arrows - Combat utility
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.TORCH, 2),
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 2),
                        new ItemStack(net.minecraft.world.item.Items.GRAVEL, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.ARROW, 4), 3, Integer.MAX_VALUE));
        // (No order-independence for 3-input trades for now)

        // 6. Copper Ingot + Charcoal to Lightning Rod - Thematic + harmless
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.COPPER_INGOT, 1),
                        new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.LIGHTNING_ROD, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 1),
                        new ItemStack(net.minecraft.world.item.Items.COPPER_INGOT, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.LIGHTNING_ROD, 1), 3, Integer.MAX_VALUE));

        // 7. Sand + Glass Pane to Stained Glass Block - Cosmetic value
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.SAND, 3),
                        new ItemStack(net.minecraft.world.item.Items.GLASS_PANE, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.BLUE_STAINED_GLASS, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.GLASS_PANE, 1),
                        new ItemStack(net.minecraft.world.item.Items.SAND, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.BLUE_STAINED_GLASS, 1), 3, Integer.MAX_VALUE));

        // 8. Rotten Flesh + Gravel to Compass - Replaces map/Sugar Cane
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.ROTTEN_FLESH, 8),
                        new ItemStack(net.minecraft.world.item.Items.GRAVEL, 2)
                ),
                new ItemStack(net.minecraft.world.item.Items.COMPASS, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.GRAVEL, 2),
                        new ItemStack(net.minecraft.world.item.Items.ROTTEN_FLESH, 8)
                ),
                new ItemStack(net.minecraft.world.item.Items.COMPASS, 1), 3, Integer.MAX_VALUE));

        // 9. Flint + Stick to Crossbow - Replaces book; range reward
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 6),
                        new ItemStack(net.minecraft.world.item.Items.STICK, 3)
                ),
                new ItemStack(net.minecraft.world.item.Items.CROSSBOW, 1), 3, Integer.MAX_VALUE));
        TIER_3_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.STICK, 3),
                        new ItemStack(net.minecraft.world.item.Items.FLINT, 6)
                ),
                new ItemStack(net.minecraft.world.item.Items.CROSSBOW, 1), 3, Integer.MAX_VALUE));

        // Tier 2: Mid-game resources and transitions
        // 1. Sand + Charcoal to Glass - Early access to decorative/light blocks
        TIER_2_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.SAND, 1),
                        new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GLASS, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(
                List.of(
                        new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 1),
                        new ItemStack(net.minecraft.world.item.Items.SAND, 1)
                ),
                new ItemStack(net.minecraft.world.item.Items.GLASS, 1), 2, Integer.MAX_VALUE));

        // 2. Charcoal to Coal - Transition fuel, introduces ore equivalency
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 4)), new ItemStack(net.minecraft.world.item.Items.COAL, 1), 2, Integer.MAX_VALUE));

        // 3. Stone Tools to Iron Nugget - Soft incentive to recycle tools, aligns with T3
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STONE_PICKAXE, 4)), new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STONE_AXE, 4)), new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STONE_SHOVEL, 4)), new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STONE_HOE, 4)), new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 1), 2, Integer.MAX_VALUE));

        // 4. Flint to Arrow Bundle - Preps player for ranged play, reduces grind
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.FLINT, 6)), new ItemStack(net.minecraft.world.item.Items.ARROW, 4), 2, Integer.MAX_VALUE));

        // 5. Rotten Flesh to Leather - Offers alternative to cow farming
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.ROTTEN_FLESH, 10)), new ItemStack(net.minecraft.world.item.Items.LEATHER, 1), 2, Integer.MAX_VALUE));

        // 6. Wheat Seeds to Bone Meal - Adds farming conversion loop
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.WHEAT_SEEDS, 8)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 2, Integer.MAX_VALUE));

        // 7. Saplings to Apple - Rare apple recycling for food/gold
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.OAK_SAPLING, 6)), new ItemStack(net.minecraft.world.item.Items.APPLE, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.SPRUCE_SAPLING, 6)), new ItemStack(net.minecraft.world.item.Items.APPLE, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.BIRCH_SAPLING, 6)), new ItemStack(net.minecraft.world.item.Items.APPLE, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.JUNGLE_SAPLING, 6)), new ItemStack(net.minecraft.world.item.Items.APPLE, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.ACACIA_SAPLING, 6)), new ItemStack(net.minecraft.world.item.Items.APPLE, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.DARK_OAK_SAPLING, 6)), new ItemStack(net.minecraft.world.item.Items.APPLE, 1), 2, Integer.MAX_VALUE));
        TIER_2_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.MANGROVE_PROPAGULE, 6)), new ItemStack(net.minecraft.world.item.Items.APPLE, 1), 2, Integer.MAX_VALUE));

        // Tier 1: Early game resource exchanges
        // 1. Dirt to Gravel - Trade bulk dirt for early flint use
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.DIRT, 8)), new ItemStack(net.minecraft.world.item.Items.GRAVEL, 1), 1, Integer.MAX_VALUE));
        // 2. Gravel to Flint - Saves RNG grind for arrows/tools
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.GRAVEL, 6)), new ItemStack(net.minecraft.world.item.Items.FLINT, 1), 1, Integer.MAX_VALUE));
        // 3. Sticks to Charcoal - Early fuel option
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STICK, 4)), new ItemStack(net.minecraft.world.item.Items.CHARCOAL, 1), 1, Integer.MAX_VALUE));
        // 4. Cobblestone to Andesite - Aesthetic building assist
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.COBBLESTONE, 3)), new ItemStack(net.minecraft.world.item.Items.ANDESITE, 1), 1, Integer.MAX_VALUE));
        // 5. Saplings to Bone Meal - Recycle extras for tree growth or farming acceleration (any sapling type)
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.OAK_SAPLING, 5)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.SPRUCE_SAPLING, 5)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.BIRCH_SAPLING, 5)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.JUNGLE_SAPLING, 5)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.ACACIA_SAPLING, 5)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.DARK_OAK_SAPLING, 5)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.MANGROVE_PROPAGULE, 5)), new ItemStack(net.minecraft.world.item.Items.BONE_MEAL, 1), 1, Integer.MAX_VALUE));
        // 6. Stone Tools to Cobblestone - Disposal option for old tools (one of any type)
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STONE_PICKAXE, 1)),
            new ItemStack(net.minecraft.world.item.Items.COBBLESTONE, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STONE_AXE, 1)),
            new ItemStack(net.minecraft.world.item.Items.COBBLESTONE, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.STONE_SHOVEL, 1)),
            new ItemStack(net.minecraft.world.item.Items.COBBLESTONE, 1), 1, Integer.MAX_VALUE));
        // 7. Seeds to Bread - Removes surplus seeds
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.WHEAT_SEEDS, 10)), new ItemStack(net.minecraft.world.item.Items.BREAD, 1), 1, Integer.MAX_VALUE));
        // 8. Sand + Gravel to Gray Concrete Powder - Introduces decor pathway (requires both)
        // This requires exactly one trade with both items in a single List.of() call - both ingredients are required
        TIER_1_TRADES.add(new TradeOffer(
            List.of(
                new ItemStack(net.minecraft.world.item.Items.SAND, 2),
                new ItemStack(net.minecraft.world.item.Items.GRAVEL, 2)
            ),
            new ItemStack(net.minecraft.world.item.Items.GRAY_CONCRETE_POWDER, 1), 1, Integer.MAX_VALUE));
        TIER_1_TRADES.add(new TradeOffer(
                List.of(
                    new ItemStack(net.minecraft.world.item.Items.GRAVEL, 2),
                    new ItemStack(net.minecraft.world.item.Items.SAND, 2)
                ),
                new ItemStack(net.minecraft.world.item.Items.GRAY_CONCRETE_POWDER, 1), 1, Integer.MAX_VALUE));
        // 9. Apple to Sticks - Recycle apples if not needed
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.APPLE, 1)), new ItemStack(net.minecraft.world.item.Items.STICK, 2), 1, Integer.MAX_VALUE));
        // 10. Oak Leaves to Stick - Encourages shearing/gathering leaves
        TIER_1_TRADES.add(new TradeOffer(List.of(new ItemStack(net.minecraft.world.item.Items.OAK_LEAVES, 4)), new ItemStack(net.minecraft.world.item.Items.STICK, 1), 1, Integer.MAX_VALUE));

    }

    public static List<TradeOffer> getTradesForTier(int tier) {
        return switch (tier) {
            case 1 -> TIER_1_TRADES;
            case 2 -> TIER_2_TRADES;
            case 3 -> TIER_3_TRADES;
            default -> List.of();
        };
    }

    public static void addTrade(TradeOffer offer) {
        switch (offer.getTier()) {
            case 1 -> TIER_1_TRADES.add(offer);
            case 2 -> TIER_2_TRADES.add(offer);
            case 3 -> TIER_3_TRADES.add(offer);
        }
    }

    /**
     * Unlocks Tier 2 trades if enough Tier 1 trades have been completed.
     * Example: Unlock Tier 2 after 5 Tier 1 trades completed.
     * @param currentTier The player's current unlocked tier
     * @param tradesCompleted Number of trades completed at this tier
     * @return true if Tier 2 should be unlocked
     */
    public static boolean shouldUnlockTier2(int currentTier, int tradesCompleted) {
        return currentTier == 1 && tradesCompleted >= 5;
    }

    /**
     * Checks if the next trade tier can be unlocked based on current tier and trades completed.
     * For Tier 3, require 10 Tier 2 trades completed to unlock.
     * @param currentTier The player's current unlocked tier
     * @param tradesCompleted Number of trades completed at this tier
     * @return true if the next tier can be unlocked, false otherwise
     */
    public static boolean canUnlockNextTier(int currentTier, int tradesCompleted) {
        // Example logic: unlock Tier 2 after 5 Tier 1 trades, Tier 3 after 10 Tier 2 trades
        return switch (currentTier) {
            case 1 -> tradesCompleted >= 5;
            case 2 -> tradesCompleted >= 10; // Tier 3 unlocks after 10 Tier 2 trades
            default -> false;
        };
    }

    /**
     * Checks if the player can unlock the next tier based on unique trades completed in the current tier.
     * @param currentTier The player's current unlocked tier
     * @param progress The PlayerTradeProgress instance for this player
     * @param uniqueTradesRequiredPerTier List of required unique trades per tier (from config)
     * @return true if the next tier can be unlocked, false otherwise
     */
    public static boolean canUnlockNextTierUnique(int currentTier, PlayerTradeProgress progress, List<Integer> uniqueTradesRequiredPerTier) {
        if (currentTier < 1) return false;
        int idx = Math.max(0, Math.min(currentTier - 1, uniqueTradesRequiredPerTier.size() - 1));
        int required = uniqueTradesRequiredPerTier.get(idx);
        int completed = progress.getUniqueTradeCount(currentTier);
        return completed >= required;
    }

    /**
     * Checks if a trade creates a resource cycle or infinite loop.
     * Prevents cyclical trades (e.g., A->B and B->A with no loss) and infinite resource gain.
     * @param offer The trade offer to validate
     * @return true if the trade is valid (not exploitable), false if it should be blocked
     */
    public static boolean isTradeValid(TradeOffer offer) {
        // Simple check: prevent direct 1:1 reverse trades with no loss
        for (TradeOffer other : getTradesForTier(offer.getTier())) {
            if (other.getOutput().is(offer.getInputs().get(0).getItem()) &&
                other.getInputs().size() == 1 &&
                offer.getInputs().size() == 1 &&
                other.getInputs().get(0).is(offer.getOutput().getItem())) {
                // If both trades have the same input/output amounts, it's a cycle
                if (other.getInputs().get(0).getCount() == offer.getOutput().getCount() &&
                    other.getOutput().getCount() == offer.getInputs().get(0).getCount()) {
                    return false;
                }
            }
        }
        // Additional checks for more complex cycles can be added here
        return true;
    }

    public static List<TradeOffer> getTier1Trades() {
        return TIER_1_TRADES;
    }
    public static List<TradeOffer> getTier2Trades() {
        return TIER_2_TRADES;
    }
    public static List<TradeOffer> getTier3Trades() {
        return TIER_3_TRADES;
    }

    // Only allow tier progression when the configured number of unique trades for that tier is reached.
    // Use canUnlockNextTierUnique() in your trade completion logic (e.g., after a trade is completed, check if the next tier should be unlocked).
    // Example usage (pseudo-code):
    // if (TradeManager.canUnlockNextTierUnique(currentTier, playerTradeProgress, configUniqueTradesRequiredPerTier)) {
    //     currentTier++;
    //     // Optionally reset or update progress for the new tier
    // }

    /**
     * Returns a unique identifier for a trade offer for use in unique trade tracking.
     * Uses the offer's hashCode by default, but can be replaced with a more stable ID if needed.
     */
    public static String getTradeId(TradeOffer offer) {
        // For now, use hashCode as a string. For more stability, use a custom ID field.
        return Integer.toString(offer.hashCode());
    }

    /**
     * Adds a unique trade to the player's progress for the current tier, preventing duplicates.
     * Returns true if this trade was newly completed (not previously counted).
     */
    public static boolean addUniqueTradeIfNew(PlayerTradeProgress progress, TradeOffer offer) {
        int tier = offer.getTier();
        String tradeId = getTradeId(offer);
        return progress.addUniqueTrade(tier, tradeId);
    }

    /**
     * Returns the number of unique trades required to unlock the next tier for the given tier (1-based).
     * If the config is shorter than the tier, the last value is used for all higher tiers.
     */
    public static int getUniqueTradesRequiredForTier(int tier) {
        if (uniqueTradesRequiredPerTier.isEmpty()) return 1;
        int idx = Math.max(0, Math.min(tier - 1, uniqueTradesRequiredPerTier.size() - 1));
        return uniqueTradesRequiredPerTier.get(idx);
    }

    /**
     * Returns the config list for unique trades required per tier.
     * Used for passing to trade progression logic.
     */
    public static List<Integer> getUniqueTradesRequiredPerTierConfig() {
        return uniqueTradesRequiredPerTier;
    }
}
