package com.funcation.blocks.entity;

import com.funcation.trade.blocks.entity.TradeBlockEntity;
import com.funcation.trade.data.TradeManager;
import com.funcation.trade.data.trades.TradeOffer;
import com.funcation.trade.player.PlayerTradeProgress;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradeBlockEntityTest {
    private TradeBlockEntity tradeBlockEntity;
    private Player mockPlayer;
    private PlayerTradeProgress progress;
    private List<Integer> config;

    @BeforeAll
    static void setupAll() {
        SharedConstants.setVersion(net.minecraft.DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
        // Skip Forge registration in unit test environment
    }

    @BeforeEach
    void setUp() {
        // Mock BlockState as it's not relevant for logic
        BlockState mockState = Mockito.mock(BlockState.class);
        // Use a test-only constructor to avoid registry lookup
        tradeBlockEntity = TradeBlockEntity.createForTest(BlockPos.ZERO, mockState);
        mockPlayer = Mockito.mock(Player.class);
        // Mock getName() to avoid NPE in debug output
        Mockito.when(mockPlayer.getName()).thenReturn(net.minecraft.network.chat.Component.literal("TestPlayer"));
        progress = new PlayerTradeProgress();
        config = Arrays.asList(2, 3, 4); // Example config: 2 unique for tier 1, etc.
    }

    @Test
    void testTier1TradeProcessesWithValidInput() {
        List<TradeOffer> trades = TradeManager.getTradesForTier(1);
        if (trades.isEmpty()) return;
        TradeOffer offer = trades.get(0);
        SimpleContainer inv = tradeBlockEntity.getInventory();
        for (int i = 0; i < offer.getInputs().size(); i++) {
            inv.setItem(i, offer.getInputs().get(i).copy());
        }
        inv.setItem(2, ItemStack.EMPTY);
        assertTrue(tradeBlockEntity.isValidTradeInput(offer));
        assertTrue(tradeBlockEntity.processTrade(offer, mockPlayer, config, progress));
        assertEquals(offer.getOutput().getItem(), inv.getItem(2).getItem());
    }

    @Test
    void testTier1TradeFailsWithInvalidInput() {
        List<TradeOffer> trades = TradeManager.getTradesForTier(1);
        if (trades.isEmpty()) return;
        TradeOffer offer = trades.get(0);
        SimpleContainer inv = tradeBlockEntity.getInventory();
        inv.setItem(0, new ItemStack(net.minecraft.world.item.Items.DIRT, 1));
        inv.setItem(1, ItemStack.EMPTY);
        inv.setItem(2, ItemStack.EMPTY);
        assertFalse(tradeBlockEntity.isValidTradeInput(offer));
        assertFalse(tradeBlockEntity.processTrade(offer, mockPlayer, config, progress));
    }

    @Test
    void testUniqueTradeProgressionIsRespected() {
        List<TradeOffer> trades = TradeManager.getTradesForTier(1);
        if (trades.size() < 2) return;
        TradeOffer offer1 = trades.get(0);
        TradeOffer offer2 = trades.get(1);
        SimpleContainer inv = tradeBlockEntity.getInventory();
        // Complete first unique trade
        for (int i = 0; i < offer1.getInputs().size(); i++) {
            inv.setItem(i, offer1.getInputs().get(i).copy());
        }
        inv.setItem(2, ItemStack.EMPTY);
        assertTrue(tradeBlockEntity.processTrade(offer1, mockPlayer, config, progress));
        // Complete second unique trade
        for (int i = 0; i < offer2.getInputs().size(); i++) {
            inv.setItem(i, offer2.getInputs().get(i).copy());
        }
        inv.setItem(2, ItemStack.EMPTY);
        assertTrue(tradeBlockEntity.processTrade(offer2, mockPlayer, config, progress));
        // Now player should have 2 unique trades for tier 1
        assertEquals(2, progress.getUniqueTrades(1).size());
    }
}
