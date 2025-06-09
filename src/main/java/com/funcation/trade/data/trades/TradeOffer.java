package com.funcation.trade.data.trades;

import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single trade offer for the Trade Block.
 * Contains input items, output item, and optional trade tier/limits.
 * @since 1.0.0
 */
public class TradeOffer {
    private final List<ItemStack> inputs;
    private final ItemStack output;
    private final int tier;
    private final int maxUses;

    /**
     * @param inputs   List of input ItemStacks required for the trade
     * @param output   Output ItemStack given by the trade
     * @param tier     Trade tier (1 = early, 2 = mid, 3 = late)
     * @param maxUses  Maximum number of times this trade can be used (use Integer.MAX_VALUE for unlimited)
     */
    public TradeOffer(List<ItemStack> inputs, ItemStack output, int tier, int maxUses) {
        this.inputs = inputs;
        this.output = output;
        this.tier = tier;
        this.maxUses = maxUses;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getTier() {
        return tier;
    }

    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeOffer that = (TradeOffer) o;
        return tier == that.tier &&
                Objects.equals(inputs, that.inputs) &&
                Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, output, tier);
    }
}
