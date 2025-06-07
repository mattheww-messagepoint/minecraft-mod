package com.example.examplemod;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class TradeTerminalMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private final Container container;
    private final ContainerLevelAccess access;
    private final List<MerchantOffer> offers = new ArrayList<>();
    private MerchantOffer currentOffer = null;

    public TradeTerminalMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(2));
    }

    public TradeTerminalMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(id, playerInventory, new SimpleContainer(2));
    }

    public TradeTerminalMenu(int id, Inventory playerInventory, Container container) {
        super(ModMenus.TRADE_TERMINAL_MENU.get(), id);
        this.container = container;
        this.access = ContainerLevelAccess.NULL;
        this.addSlot(new Slot(container, INPUT_SLOT, 44, 35) {
            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                updateOffer();
            }
        });
        this.addSlot(new Slot(container, OUTPUT_SLOT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                if (currentOffer != null && !container.getItem(INPUT_SLOT).isEmpty()) {
                    container.getItem(INPUT_SLOT).shrink(currentOffer.getBaseCostA().getCount());
                    updateOffer();
                }
                super.onTake(player, stack);
            }
        });
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
        offers.add(new MerchantOffer(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.DIAMOND, 1), 9999, 0, 0));
        offers.add(new MerchantOffer(new ItemStack(Items.IRON_INGOT, 5), new ItemStack(Items.GOLD_INGOT, 1), 9999, 0, 0));
    }

    private void updateOffer() {
        ItemStack input = container.getItem(INPUT_SLOT);
        currentOffer = null;
        for (MerchantOffer offer : offers) {
            if (ItemStack.isSameItemSameTags(input, offer.getBaseCostA()) && input.getCount() >= offer.getBaseCostA().getCount()) {
                currentOffer = offer;
                break;
            }
        }
        if (currentOffer != null) {
            container.setItem(OUTPUT_SLOT, currentOffer.getResult().copy());
        } else {
            container.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Simple shift-click logic: just return EMPTY for now (no shift-click support)
        return ItemStack.EMPTY;
    }
}
