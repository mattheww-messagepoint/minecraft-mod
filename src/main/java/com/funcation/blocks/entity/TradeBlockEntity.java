package com.funcation.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.funcation.registry.ModBlockEntities; // Added import for ModBlockEntities
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers; // Added import for dropContents
import net.minecraft.world.level.Level; // Added import for Level
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.funcation.data.TradeManager;
import com.funcation.player.PlayerTradeProgress;

/**
 * Block entity for the Trade Block, handles inventory and trade logic.
 * @since 1.0.0
 */
public class TradeBlockEntity extends BlockEntity implements MenuProvider {
    private final SimpleContainer inventory = new SimpleContainer(3); // Example: 2 input, 1 output
    private int currentTier = 1;
    private int tradesCompleted = 0;

    // Tracks unique trades completed per tier: tier -> set of trade IDs/hashes
    private final Map<Integer, Set<String>> uniqueTradesPerTier = new HashMap<>();

    private int uniqueTradesCompletedSynced = 0;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> uniqueTradesCompletedSynced;
                case 1 -> currentTier;
                default -> 0;
            };
        }
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> uniqueTradesCompletedSynced = value;
                case 1 -> currentTier = value;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
    };

    public TradeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRADE_BLOCK_ENTITY.get(), pos, state);
    }

    /**
     * Protected constructor for test use, allows bypassing registry.
     */
    protected TradeBlockEntity(@Nullable BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    public int getCurrentTier() {
        return currentTier;
    }

    public void setCurrentTier(int tier) {
        this.currentTier = tier;
        markDirtyAndSync();
    }

    public int getTradesCompleted() {
        return tradesCompleted;
    }

    public void setTradesCompleted(int tradesCompleted) {
        this.tradesCompleted = tradesCompleted;
        markDirtyAndSync();
    }

    /**
     * Returns the number of trades completed for this block entity.
     * Used for progression and tier unlocking.
     */
    public int getTotalTradesCompleted() {
        return this.tradesCompleted;
    }

    /**
     * Resets the trade completion counter (for testing or admin use).
     */
    public void resetTradesCompleted() {
        this.tradesCompleted = 0;
        markDirtyAndSync();
    }

    /**
     * Optimized: Only call setChanged() and send updates when state actually changes.
     * Use this method after any trade or inventory mutation.
     */
    public void markDirtyAndSync() {
        if (level != null && !level.isClientSide) {
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        // Load the inventory items from the compound tag
        if (pTag.contains("Items")) {
            inventory.fromTag(pTag.getList("Items", 10));
        }
        if (pTag.contains("CurrentTier")) {
            currentTier = pTag.getInt("CurrentTier");
        }
        if (pTag.contains("TradesCompleted")) {
            tradesCompleted = pTag.getInt("TradesCompleted");
        }
        if (pTag.contains("UniqueTradesPerTier")) {
            CompoundTag uniqueTag = pTag.getCompound("UniqueTradesPerTier");
            uniqueTradesPerTier.clear();
            for (String key : uniqueTag.getAllKeys()) {
                int tier = Integer.parseInt(key);
                Set<String> trades = new HashSet<>();
                for (String tradeId : uniqueTag.getList(key, 8).stream().map(t -> t.getAsString()).toList()) {
                    trades.add(tradeId);
                }
                uniqueTradesPerTier.put(tier, trades);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        // Save the inventory items to the compound tag
        pTag.put("Items", inventory.createTag());
        pTag.putInt("CurrentTier", currentTier);
        pTag.putInt("TradesCompleted", tradesCompleted);
        CompoundTag uniqueTag = new CompoundTag();
        for (Map.Entry<Integer, Set<String>> entry : uniqueTradesPerTier.entrySet()) {
            net.minecraft.nbt.ListTag list = new net.minecraft.nbt.ListTag();
            for (String tradeId : entry.getValue()) {
                list.add(net.minecraft.nbt.StringTag.valueOf(tradeId));
            }
            uniqueTag.put(String.valueOf(entry.getKey()), list);
        }
        pTag.put("UniqueTradesPerTier", uniqueTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        // Called when the chunk is sent to the client
        CompoundTag tag = super.getUpdateTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        // Called client-side when chunk data is received
        this.load(tag);
    }

    @Override
    public @org.jetbrains.annotations.Nullable net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        // Called to send a custom update packet to the client
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
        // Called client-side when a custom update packet is received
        this.load(pkt.getTag());
    }

    @Override
    public Component getDisplayName() {
        // TODO: Replace with a translatable component for the block's name
        return Component.literal("Trade Block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull net.minecraft.world.entity.player.Inventory pPlayerInventory, @NotNull Player pPlayer) {
        // Provide access to this block entity's inventory via ContainerLevelAccess
        return new com.funcation.screen.TradeBlockMenu(pContainerId, pPlayerInventory, net.minecraft.world.inventory.ContainerLevelAccess.create(this.level, this.getBlockPos()));
    }

    public ContainerData getContainerData() {
        return data;
    }

    /**
     * Drops the inventory contents in the world
     * @param level The level/world
     * @param pos The position to drop items at
     */
    public void dropContents(Level level, BlockPos pos) {
        Containers.dropContents(level, pos, this.inventory);
    }

    /**
     * Attempts to process a trade offer using the current inventory.
     * @param offer The TradeOffer to process
     * @param player The player performing the trade (for unique trade tracking)
     * @param uniqueTradesRequiredPerTier List of required unique trades per tier (from config)
     * @param playerProgress The PlayerTradeProgress instance for this player
     * @return true if the trade was successful, false otherwise
     */
    public boolean processTrade(com.funcation.data.trades.TradeOffer offer, Player player, java.util.List<Integer> uniqueTradesRequiredPerTier, PlayerTradeProgress playerProgress) {
        System.out.println("[TRADE DEBUG] ENTER processTrade: offer=" + offer + ", player=" + player.getName().getString());
        // Check if trade tier is unlocked
        if (offer.getTier() > this.currentTier) return false;
        if (!TradeManager.isTradeValid(offer)) return false;
        java.util.List<net.minecraft.world.item.ItemStack> inputs = offer.getInputs();
        // Check input items
        for (int i = 0; i < inputs.size(); i++) {
            net.minecraft.world.item.ItemStack required = inputs.get(i);
            net.minecraft.world.item.ItemStack inSlot = inventory.getItem(i);
            if (inSlot.isEmpty() || !inSlot.is(required.getItem()) || inSlot.getCount() < required.getCount()) {
                return false;
            }
        }
        // Check output slot can accept the result
        net.minecraft.world.item.ItemStack output = offer.getOutput();
        net.minecraft.world.item.ItemStack outSlot = inventory.getItem(2);
        if (!outSlot.isEmpty() && (!outSlot.is(output.getItem()) || outSlot.getCount() + output.getCount() > outSlot.getMaxStackSize())) {
            return false;
        }
        // Always consume input items for any valid trade
        for (int i = 0; i < inputs.size(); i++) {
            net.minecraft.world.item.ItemStack required = inputs.get(i);
            net.minecraft.world.item.ItemStack inSlot = inventory.getItem(i);
            inSlot.shrink(required.getCount());
        }
        // Give output item
        if (outSlot.isEmpty()) {
            inventory.setItem(2, output.copy());
        } else {
            outSlot.grow(output.getCount());
        }
        // Unique trade progression logic
        boolean isNewUnique = com.funcation.data.TradeManager.addUniqueTradeIfNew(playerProgress, offer);
        System.out.println("[TRADE DEBUG] processTrade: offer=" + offer + ", isNewUnique=" + isNewUnique);
        if (isNewUnique) {
            int completed = playerProgress.getUniqueTradeCount(offer.getTier());
            System.out.println("[TRADE DEBUG] Unique trades completed for tier " + offer.getTier() + ": " + completed);
            uniqueTradesCompletedSynced = completed;
            // Check if enough unique trades have been completed to unlock next tier
            if (com.funcation.data.TradeManager.canUnlockNextTierUnique(this.currentTier, playerProgress, uniqueTradesRequiredPerTier)) {
                this.currentTier++;
                // Reset unique trades for the new tier
                playerProgress.clearUniqueTradesForTier(this.currentTier);
                uniqueTradesCompletedSynced = 0;
                System.out.println("[TRADE DEBUG] Tier unlocked! New tier: " + this.currentTier + ", progress reset to 0");
                // Optionally: reset uniqueTradesPerTier for new tier, or keep for history
            }
        }
        markDirtyAndSync();
        return true;
    }

    /**
     * Ensures unique trade progression and tier unlocks work for all trade tiers.
     * The processTrade method and unique trade logic are tier-agnostic and will function for any tier present in the config and trade data.
     * To add more tiers, update the config and trade lists; the logic will automatically apply.
     */

    /**
     * Checks if the current inventory is valid for the given trade offer.
     * Does not mutate inventory or state.
     * @param offer The TradeOffer to validate
     * @return true if the inventory is valid for the trade, false otherwise
     */
    public boolean isValidTradeInput(com.funcation.data.trades.TradeOffer offer) {
        if (offer.getTier() > this.currentTier) return false;
        if (!com.funcation.data.TradeManager.isTradeValid(offer)) return false; // Added exploit check
        // Only apply maxUses check for current tier trades
        if (offer.getTier() == this.currentTier && offer.getMaxUses() != Integer.MAX_VALUE && this.tradesCompleted >= offer.getMaxUses()) return false;
        java.util.List<net.minecraft.world.item.ItemStack> inputs = offer.getInputs();
        for (int i = 0; i < inputs.size(); i++) {
            net.minecraft.world.item.ItemStack required = inputs.get(i);
            net.minecraft.world.item.ItemStack inSlot = inventory.getItem(i);
            if (inSlot.isEmpty() || !inSlot.is(required.getItem()) || inSlot.getCount() < required.getCount()) {
                return false;
            }
        }
        net.minecraft.world.item.ItemStack output = offer.getOutput();
        net.minecraft.world.item.ItemStack outSlot = inventory.getItem(2);
        if (!outSlot.isEmpty() && (!outSlot.is(output.getItem()) || outSlot.getCount() + output.getCount() > outSlot.getMaxStackSize())) {
            return false;
        }
        return true;
    }

    /**
     * Ticks the block entity
     * Called once per tick to update the block entity's state
     *
     * @param level The level the block entity is in
     * @param pos The position of the block entity
     * @param state The current block state
     * @param blockEntity The block entity instance
     */
    public static void tick(Level level, BlockPos pos, BlockState state, TradeBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        SimpleContainer inventory = blockEntity.getInventory();
        int tier = blockEntity.getCurrentTier();
        boolean found = false;
        // Check all trades up to and including the current tier
        for (com.funcation.data.trades.TradeOffer offer : getAllAvailableTrades(tier)) {
            if (blockEntity.isValidTradeInput(offer)) {
                ItemStack output = offer.getOutput();
                ItemStack outSlot = inventory.getItem(2);
                if (outSlot.isEmpty() || !ItemStack.isSameItemSameTags(outSlot, output) || outSlot.getCount() != output.getCount()) {
                    inventory.setItem(2, output.copy());
                    blockEntity.markDirtyAndSync();
                }
                found = true;
                break;
            }
        }
        if (!found) {
            ItemStack outSlot = inventory.getItem(2);
            if (!outSlot.isEmpty()) {
                inventory.setItem(2, ItemStack.EMPTY);
                blockEntity.markDirtyAndSync();
            }
        }
    }

    // Helper: Get all trades up to and including the current tier
    private static java.util.List<com.funcation.data.trades.TradeOffer> getAllAvailableTrades(int currentTier) {
        java.util.List<com.funcation.data.trades.TradeOffer> all = new java.util.ArrayList<>();
        for (int t = 1; t <= currentTier; t++) {
            all.addAll(com.funcation.data.TradeManager.getTradesForTier(t));
        }
        return all;
    }

    /**
     * Record a unique trade for the current tier. Returns true if it was newly added.
     */
    public boolean addUniqueTradeForCurrentTier(String tradeId) {
        int tier = this.currentTier;
        Set<String> set = uniqueTradesPerTier.computeIfAbsent(tier, k -> new HashSet<>());
        boolean added = set.add(tradeId);
        if (added) markDirtyAndSync();
        return added;
    }

    /**
     * Returns the number of unique trades completed for the current tier.
     */
    public int getUniqueTradesCompletedForCurrentTier() {
        if (this.level != null && this.level.isClientSide) {
            return uniqueTradesCompletedSynced;
        }
        return uniqueTradesPerTier.getOrDefault(currentTier, java.util.Collections.emptySet()).size();
    }

    /**
     * Returns the number of unique trades required for the current tier, using the config.
     */
    public int getUniqueTradesRequiredForCurrentTier() {
        return com.funcation.data.TradeManager.getUniqueTradesRequiredForTier(currentTier);
    }

    /**
     * Test-only factory method to create a TradeBlockEntity without registry lookup.
     */
    public static TradeBlockEntity createForTest(BlockPos pos, BlockState state) {
        // Use null for BlockEntityType in tests
        return new TradeBlockEntity(null, pos, state);
    }
}
