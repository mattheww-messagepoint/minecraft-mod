package com.funcation.screen;

import com.funcation.blocks.entity.TradeBlockEntity;
import com.funcation.registry.ModBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ObjectHolder;
import com.funcation.registry.ModMenuTypes;

/**
 * TradeBlockMenu - Container class for the Trade Block UI.
 * Handles input/output slots and player inventory binding.
 * @since 1.0.0
 */
public class TradeBlockMenu extends AbstractContainerMenu {
    private final TradeBlockEntity blockEntity; // Changed from Container
    private final ContainerLevelAccess access;
    private final Player player;
    private final net.minecraft.world.inventory.ContainerData data;

    // Constructor for when the server creates the menu
    public TradeBlockMenu(int id, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.TRADE_BLOCK_MENU.get(), id);
        this.access = access;
        this.player = playerInventory.player;
        // DEBUG LOGGING START
        System.out.println("[TRADE DEBUG] TradeBlockMenu constructor called. Access: " + access);
        // Only resolve blockEntity on the server
        TradeBlockEntity be = access.evaluate((level, pos) -> level.getBlockEntity(pos) instanceof TradeBlockEntity tbe ? tbe : null).orElse(null);
        if (be != null) {
            System.out.println("[TRADE DEBUG] BlockEntity lookup at " + be.getBlockPos() + ": FOUND");
        } else {
            System.out.println("[TRADE DEBUG] BlockEntity lookup: NOT FOUND");
        }
        this.blockEntity = be;
        System.out.println("[TRADE DEBUG] TradeBlockEntity reference: " + this.blockEntity);
        // DEBUG LOGGING END

        Container container = blockEntity != null ? blockEntity.getInventory() : new net.minecraft.world.SimpleContainer(3);

        // Input slots
        this.addSlot(new Slot(container, 0, 44, 35));
        this.addSlot(new Slot(container, 1, 62, 35));
        // Output slot
        this.addSlot(new Slot(container, 2, 120, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                if (blockEntity != null && !player.level().isClientSide) {
                    int tier = blockEntity.getCurrentTier();
                    // Only search trades for the current tier
                    for (com.funcation.data.trades.TradeOffer offer : com.funcation.data.TradeManager.getTradesForTier(tier)) {
                        if (blockEntity.isValidTradeInput(offer)) {
                            System.out.println("[TRADE DEBUG] Matched offer: " + offer + " for tier: " + tier);
                            blockEntity.processTrade(offer, player, com.funcation.data.TradeManager.getUniqueTradesRequiredPerTierConfig(), com.funcation.player.PlayerTradeProgress.get(player));
                            blockEntity.markDirtyAndSync();
                            return;
                        }
                    }
                }
            }
        });

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.data = blockEntity != null ? blockEntity.getContainerData() : new net.minecraft.world.inventory.SimpleContainerData(2);
        this.addDataSlots(this.data);
    }

    // Constructor for when the client creates the menu (reads BlockPos from buffer)
    public TradeBlockMenu(int id, Inventory playerInventory, net.minecraft.network.FriendlyByteBuf buf) {
        this(id, playerInventory, ContainerLevelAccess.create(playerInventory.player.level(), buf.readBlockPos()));
        // DEBUG LOGGING START
        System.out.println("[TRADE DEBUG] TradeBlockMenu CLIENT constructor called.");
        // DEBUG LOGGING END
        // blockEntity will be null on the client
    }

    @Override
    public boolean stillValid(Player player) {
        // Use ContainerLevelAccess to check if the block is still valid and reachable
        return this.access.evaluate((level, blockPos) ->
            level.getBlockState(blockPos).is(ModBlocks.TRADE_BLOCK.get()) && // Check if it's our block
            player.distanceToSqr(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D) <= 64.0D, // Check distance
            true // Default if optional is empty
        );
    }

    public Container getContainer() {
        return this.blockEntity != null ? this.blockEntity.getInventory() : new net.minecraft.world.SimpleContainer(3);
    }

    /**
     * Handle quick move (shift-click) of items between inventories
     * 
     * @param player The player performing the action
     * @param index The slot index that was shift-clicked
     * @return The remaining ItemStack (empty if fully moved)
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        // Ensure the container used for slot count is the blockEntity's inventory size
        final int containerSize = this.blockEntity != null ? this.blockEntity.getInventory().getContainerSize() : 0;

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            
            // If the slot is in the container inventory (slots 0 to containerSize-1)
            if (index < containerSize) {
                // Try to move to player inventory
                if (!this.moveItemStackTo(slotStack, containerSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } 
            // If the slot is in the player inventory
            else {
                // Try to move to container inventory (input slots 0 and 1)
                // Output slot (2) should not be a target for quickMove from player inventory
                if (!this.moveItemStackTo(slotStack, 0, containerSize - 1, false)) { // only target input slots
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        
        return itemstack;
    }

    public int getCurrentTier() {
        int tier = blockEntity != null ? blockEntity.getCurrentTier() : data.get(1);
        return tier;
    }

    /**
     * Returns the number of unique trades completed for the current tier.
     */
    public int getUniqueTradesCompletedForCurrentTier() {
        int completed = blockEntity != null ? blockEntity.getUniqueTradesCompletedForCurrentTier() : data.get(0);
        return completed;
    }

    /**
     * Returns the number of unique trades required for the current tier.
     */
    public int getUniqueTradesRequiredForCurrentTier() {
        int tier = getCurrentTier();
        int required = com.funcation.data.TradeManager.getUniqueTradesRequiredForTier(tier);
        return required;
    }
}
