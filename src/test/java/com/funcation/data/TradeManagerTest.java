package com.funcation.data;

import com.funcation.trade.data.TradeManager;
import net.minecraft.SharedConstants; // Added import
import net.minecraft.server.Bootstrap; // Added import
import org.junit.jupiter.api.Test;

import com.funcation.trade.player.PlayerTradeProgress;

import static org.junit.jupiter.api.Assertions.*;

class TradeManagerTest {

    static { // Added static initializer block
        SharedConstants.setVersion(net.minecraft.DetectedVersion.BUILT_IN); // Added this line
        Bootstrap.bootStrap();
    }

    @Test
    void testCanUnlockNextTier_Tier1ToTier2_SufficientTrades() {
        assertTrue(TradeManager.canUnlockNextTier(1, 5), "Should unlock Tier 2 with 5 Tier 1 trades.");
        assertTrue(TradeManager.canUnlockNextTier(1, 10), "Should unlock Tier 2 with more than 5 Tier 1 trades.");
    }

    @Test
    void testCanUnlockNextTier_Tier1ToTier2_InsufficientTrades() {
        assertFalse(TradeManager.canUnlockNextTier(1, 4), "Should not unlock Tier 2 with less than 5 Tier 1 trades.");
        assertFalse(TradeManager.canUnlockNextTier(1, 0), "Should not unlock Tier 2 with 0 Tier 1 trades.");
    }

    @Test
    void testCanUnlockNextTier_Tier2ToTier3_SufficientTrades() {
        assertTrue(TradeManager.canUnlockNextTier(2, 10), "Should unlock Tier 3 with 10 Tier 2 trades.");
        assertTrue(TradeManager.canUnlockNextTier(2, 15), "Should unlock Tier 3 with more than 10 Tier 2 trades.");
    }

    @Test
    void testCanUnlockNextTier_Tier2ToTier3_InsufficientTrades() {
        assertFalse(TradeManager.canUnlockNextTier(2, 9), "Should not unlock Tier 3 with less than 10 Tier 2 trades.");
        assertFalse(TradeManager.canUnlockNextTier(2, 0), "Should not unlock Tier 3 with 0 Tier 2 trades.");
    }

    @Test
    void testCanUnlockNextTier_InvalidCurrentTier() {
        assertFalse(TradeManager.canUnlockNextTier(0, 10), "Should not unlock from Tier 0.");
        assertFalse(TradeManager.canUnlockNextTier(3, 20), "Should not unlock from Tier 3 (max tier).");
        assertFalse(TradeManager.canUnlockNextTier(-1, 10), "Should not unlock from a negative tier.");
    }

    @Test
    void testShouldUnlockTier2_CorrectLogic() {
        assertTrue(TradeManager.shouldUnlockTier2(1, 5), "Tier 2 should unlock with 5 Tier 1 trades.");
        assertTrue(TradeManager.shouldUnlockTier2(1, 6), "Tier 2 should unlock with 6 Tier 1 trades.");
        assertFalse(TradeManager.shouldUnlockTier2(1, 4), "Tier 2 should not unlock with 4 Tier 1 trades.");
        assertFalse(TradeManager.shouldUnlockTier2(2, 5), "shouldUnlockTier2 should only apply to currentTier 1.");
    }

    @Test
    void testIsTradeValid_NoCycle() {
        com.funcation.trade.data.trades.TradeOffer offer = new com.funcation.trade.data.trades.TradeOffer(
            java.util.List.of(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE, 1)),
            new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.COBBLESTONE, 1),
            1, Integer.MAX_VALUE
        );

        assertTrue(TradeManager.isTradeValid(offer), "A simple, non-cyclical trade should be valid.");
    }

    @Test
    void testIsTradeValid_DirectCycle() {
        com.funcation.trade.data.trades.TradeOffer offer1 = new com.funcation.trade.data.trades.TradeOffer(
            java.util.List.of(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND, 1)),
            new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.EMERALD, 1),
            1, Integer.MAX_VALUE
        );
        com.funcation.trade.data.trades.TradeOffer offer2 = new com.funcation.trade.data.trades.TradeOffer(
            java.util.List.of(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.EMERALD, 1)),
            new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND, 1),
            1, Integer.MAX_VALUE
        );

        // Temporarily add offer2 to simulate the existing trade for validation
        TradeManager.addTrade(offer2);

        assertFalse(TradeManager.isTradeValid(offer1), "A trade forming a direct 1:1 cycle should be invalid.");

        // Clean up: remove the temporarily added trade to not affect other tests
        TradeManager.getTradesForTier(1).removeIf(t ->
            t.getInputs().size() == 1 &&
            t.getInputs().get(0).is(net.minecraft.world.item.Items.EMERALD) &&
            t.getInputs().get(0).getCount() == 1 &&
            t.getOutput().is(net.minecraft.world.item.Items.DIAMOND) &&
            t.getOutput().getCount() == 1);
    }

    @Test
    void testIsTradeValid_CycleWithDifferentAmounts() {
        com.funcation.trade.data.trades.TradeOffer offer1 = new com.funcation.trade.data.trades.TradeOffer(
            java.util.List.of(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND, 1)),
            new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.EMERALD, 2),
            1, Integer.MAX_VALUE
        ); // D(1) -> E(2)
        com.funcation.trade.data.trades.TradeOffer offer2 = new com.funcation.trade.data.trades.TradeOffer(
            java.util.List.of(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.EMERALD, 2)),
            new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND, 1),
            1, Integer.MAX_VALUE
        ); // E(2) -> D(1)

        // This is a perfect reversal (non-lossy cycle according to isTradeValid logic).
        // isTradeValid should return false for such cycles.
        TradeManager.addTrade(offer2); // Add the reverse trade

        assertFalse(TradeManager.isTradeValid(offer1), "A D(1)->E(2) trade, with an existing E(2)->D(1) trade, forms a non-lossy cycle and should be invalid.");

        // Clean up: remove the temporarily added trade
        TradeManager.getTradesForTier(1).removeIf(t ->
            t.getInputs().size() == 1 &&
            t.getInputs().get(0).is(net.minecraft.world.item.Items.EMERALD) &&
            t.getInputs().get(0).getCount() == 2 &&
            t.getOutput().is(net.minecraft.world.item.Items.DIAMOND) &&
            t.getOutput().getCount() == 1);
    }

    @Test
    void testIsTradeValid_ExistingDirtGravelTrade() {
        com.funcation.trade.data.trades.TradeOffer dirtToGravel = TradeManager.getTradesForTier(1).stream()
            .filter(t -> t.getInputs().size() == 1 && t.getInputs().get(0).is(net.minecraft.world.item.Items.DIRT) && t.getOutput().is(net.minecraft.world.item.Items.GRAVEL))
            .findFirst().orElse(null);

        // The existing D(8)->G(7) and G(8)->D(7) is a lossy cycle.
        // isTradeValid checks for non-lossy cycles (counts must match for reversal).
        // So, this lossy cycle should be considered VALID by the current isTradeValid logic.
        assertNotNull(dirtToGravel, "Dirt to Gravel trade must exist for this test.");
        assertTrue(TradeManager.isTradeValid(dirtToGravel), "Existing Dirt(8)->Gravel(7) vs Gravel(8)->Dirt(7) should be valid as it's lossy.");
    }

    // New unique trade progression tests (updated to use PlayerTradeProgress)
    @Test
    void testCanUnlockNextTierUnique_SufficientUniqueTrades() {
        PlayerTradeProgress progress = new com.funcation.trade.player.PlayerTradeProgress();
        progress.addUniqueTrade(1, "tradeA");
        progress.addUniqueTrade(1, "tradeB");
        progress.addUniqueTrade(1, "tradeC");
        java.util.List<Integer> config = java.util.Arrays.asList(3, 2, 4); // tier 1: 3, tier 2: 2, tier 3: 4
        assertTrue(TradeManager.canUnlockNextTierUnique(1, progress, config), "Should unlock Tier 2 with 3 unique trades (tier 1)");
    }

    @Test
    void testCanUnlockNextTierUnique_InsufficientUniqueTrades() {
        PlayerTradeProgress progress = new com.funcation.trade.player.PlayerTradeProgress();
        progress.addUniqueTrade(1, "tradeA");
        progress.addUniqueTrade(1, "tradeB");
        java.util.List<Integer> config = java.util.Arrays.asList(3, 2, 4);
        assertFalse(TradeManager.canUnlockNextTierUnique(1, progress, config), "Should not unlock Tier 2 with only 2 unique trades (tier 1)");
    }

    @Test
    void testCanUnlockNextTierUnique_DuplicateTradesNotCounted() {
        PlayerTradeProgress progress = new com.funcation.trade.player.PlayerTradeProgress();
        progress.addUniqueTrade(1, "tradeA");
        progress.addUniqueTrade(1, "tradeA"); // duplicate
        progress.addUniqueTrade(1, "tradeB");
        java.util.List<Integer> config = java.util.Arrays.asList(2, 2, 4);
        assertTrue(TradeManager.canUnlockNextTierUnique(1, progress, config), "Should unlock Tier 2 with 2 unique trades, even if duplicates attempted");
    }

    @Test
    void testCanUnlockNextTierUnique_PerTierConfigRespected() {
        PlayerTradeProgress progress = new com.funcation.trade.player.PlayerTradeProgress();
        progress.addUniqueTrade(1, "tradeA");
        progress.addUniqueTrade(1, "tradeB");
        progress.addUniqueTrade(1, "tradeC");
        java.util.List<Integer> config = java.util.Arrays.asList(2, 3, 4); // tier 1: 2, tier 2: 3, tier 3: 4
        // Tier 1 should unlock with 2 unique trades
        assertTrue(TradeManager.canUnlockNextTierUnique(1, progress, config), "Should unlock Tier 2 with 3 unique trades, config for tier 1 is 2");
        // Tier 2 should unlock with 3 unique trades if config is 3
        progress.addUniqueTrade(2, "tradeA");
        progress.addUniqueTrade(2, "tradeB");
        progress.addUniqueTrade(2, "tradeC");
        assertTrue(TradeManager.canUnlockNextTierUnique(2, progress, config), "Should unlock Tier 3 with 3 unique trades, config for tier 2 is 3");
    }

    @Test
    void testCanUnlockNextTierUnique_ConfigShorterThanTiers() {
        PlayerTradeProgress progress = new com.funcation.trade.player.PlayerTradeProgress();
        progress.addUniqueTrade(1, "tradeA");
        progress.addUniqueTrade(1, "tradeB");
        progress.addUniqueTrade(1, "tradeC");
        progress.addUniqueTrade(2, "tradeA");
        progress.addUniqueTrade(2, "tradeB");
        progress.addUniqueTrade(2, "tradeC");
        progress.addUniqueTrade(2, "tradeD");
        java.util.List<Integer> config = java.util.Arrays.asList(2); // Only tier 1 config, should use last value for all
        assertTrue(TradeManager.canUnlockNextTierUnique(1, progress, config), "Should unlock Tier 2 with 3 unique trades, config for tier 1 is 2");
        assertTrue(TradeManager.canUnlockNextTierUnique(2, progress, config), "Should unlock Tier 3 with 4 unique trades, config for tier 2 is 2 (fallback)");
    }
}
