package com.funcation.trade.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores and manages each player's unique trade progress (per tier).
 * This can be attached as a capability or managed via persistent NBT.
 */
public class PlayerTradeProgress {
    private final Map<Integer, Set<String>> uniqueTradesPerTier = new HashMap<>();

    // Simple static map for demo/testing; replace with capability for production
    private static final java.util.Map<java.util.UUID, PlayerTradeProgress> PROGRESS_MAP = new java.util.HashMap<>();

    public boolean addUniqueTrade(int tier, String tradeId) {
        Set<String> set = uniqueTradesPerTier.computeIfAbsent(tier, k -> new HashSet<>());
        return set.add(tradeId);
    }

    public int getUniqueTradeCount(int tier) {
        Set<String> set = uniqueTradesPerTier.get(tier);
        return set == null ? 0 : set.size();
    }

    public Set<String> getUniqueTrades(int tier) {
        return uniqueTradesPerTier.getOrDefault(tier, Set.of());
    }

    public void load(CompoundTag tag) {
        uniqueTradesPerTier.clear();
        if (tag.contains("UniqueTradesPerTier")) {
            CompoundTag uniqueTag = tag.getCompound("UniqueTradesPerTier");
            for (String key : uniqueTag.getAllKeys()) {
                int tier = Integer.parseInt(key);
                Set<String> trades = new HashSet<>();
                ListTag list = uniqueTag.getList(key, 8); // 8 = StringTag
                for (int i = 0; i < list.size(); i++) {
                    trades.add(list.getString(i));
                }
                uniqueTradesPerTier.put(tier, trades);
            }
        }
    }

    public void save(CompoundTag tag) {
        CompoundTag uniqueTag = new CompoundTag();
        for (Map.Entry<Integer, Set<String>> entry : uniqueTradesPerTier.entrySet()) {
            ListTag list = new ListTag();
            for (String tradeId : entry.getValue()) {
                list.add(StringTag.valueOf(tradeId));
            }
            uniqueTag.put(String.valueOf(entry.getKey()), list);
        }
        tag.put("UniqueTradesPerTier", uniqueTag);
    }

    /**
     * Ensures this data is saved/loaded correctly for singleplayer and multiplayer.
     * Attach this as a capability to the Player entity, and call load/save from the player's persistent NBT events.
     * Example usage:
     *
     * // Saving:
     * PlayerTradeProgress progress = ...;
     * CompoundTag tag = new CompoundTag();
     * progress.save(tag);
     * player.getPersistentData().put("TradeProgress", tag);
     *
     * // Loading:
     * PlayerTradeProgress progress = ...;
     * CompoundTag tag = player.getPersistentData().getCompound("TradeProgress");
     * progress.load(tag);
     */

    /**
     * Gets the PlayerTradeProgress for the given player (creates if missing).
     * In production, replace with a proper capability system.
     */
    public static PlayerTradeProgress get(Player player) {
        return PROGRESS_MAP.computeIfAbsent(player.getUUID(), k -> new PlayerTradeProgress());
    }

    public void clearUniqueTradesForTier(int tier) {
        uniqueTradesPerTier.remove(tier);
    }
}
