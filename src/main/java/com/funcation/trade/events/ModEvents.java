package com.funcation.trade.events;

import com.funcation.trade.registry.ModBlocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles mod-related events, such as giving the Trade Block to players on first join.
 * @since 1.0.0
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        // Only give if player doesn't already have it (prevents duplicates)
        boolean hasTradeBlock = player.getInventory().items.stream()
                .anyMatch(stack -> stack.getItem() == ModBlocks.TRADE_BLOCK.get().asItem());
        if (!hasTradeBlock) {
            player.getInventory().add(new ItemStack(ModBlocks.TRADE_BLOCK.get()));
        }
    }
}

