package com.funcation.item;

import com.funcation.data.TradeManager;
import com.funcation.data.trades.TradeOffer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Objects;

/**
 * TradeGuideBookItem - An in-game book that will display all trade tiers and their requirements.
 * Content will be generated dynamically in later steps.
 */
public class TradeGuideBookItem extends Item {
    public TradeGuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ItemStack dynamicBook = createTradeBook();
            // Replace the book in hand with the written book
            player.setItemInHand(hand, dynamicBook);
            // Use the vanilla written book's use method to open the GUI
            return Items.WRITTEN_BOOK.use(level, player, hand);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private ItemStack createTradeBook() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag tag = new CompoundTag();
        tag.putString("title", Component.translatable("book.funcation.trade_guide.title").getString());
        tag.putString("author", Component.translatable("book.funcation.trade_guide.author").getString());
        ListTag pages = new ListTag();
        for (String page : generateTradePages()) {
            pages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(page))));
        }
        tag.put("pages", pages);
        book.setTag(tag);
        return book;
    }

    private List<String> generateTradePages() {
        List<String> pages = new ArrayList<>();
        pages.add(Component.translatable("book.funcation.trade_guide.intro").getString());
        paginateConsolidatedTradesForTier(pages, Component.translatable("book.funcation.trade_guide.tier1").getString(), TradeManager.getTier1Trades(), "§1", Component.translatable("book.funcation.trade_guide.tier1_cont").getString());
        paginateConsolidatedTradesForTier(pages, Component.translatable("book.funcation.trade_guide.tier2").getString(), TradeManager.getTier2Trades(), "§9", Component.translatable("book.funcation.trade_guide.tier2_cont").getString());
        paginateConsolidatedTradesForTier(pages, Component.translatable("book.funcation.trade_guide.tier3").getString(), TradeManager.getTier3Trades(), "§b", Component.translatable("book.funcation.trade_guide.tier3_cont").getString());
        return pages;
    }

    private void paginateConsolidatedTradesForTier(List<String> pages, String tierName, List<TradeOffer> trades, String color, String contHeader) {
        final int PAGE_CHAR_LIMIT = 220;
        Map<OutputKey, Set<InputSet>> outputToInputs = new HashMap<>();
        for (TradeOffer offer : trades) {
            OutputKey outKey = new OutputKey(offer.getOutput());
            InputSet inSet = new InputSet(offer.getInputs());
            outputToInputs.computeIfAbsent(outKey, k -> new HashSet<>()).add(inSet);
        }
        List<OutputKey> sortedOutputs = new ArrayList<>(outputToInputs.keySet());
        sortedOutputs.sort(Comparator.comparing(OutputKey::toString));
        StringBuilder page = new StringBuilder();
        page.append(color).append("§l").append(tierName).append("§r\n");
        int count = 1;
        for (OutputKey outKey : sortedOutputs) {
            Set<InputSet> inputSets = outputToInputs.get(outKey);
            List<String> inputVariants = new ArrayList<>();
            for (InputSet inSet : inputSets) {
                inputVariants.add(inSet.toDisplayString());
            }
            String inputText = String.join(" OR ", inputVariants);
            String line = "§8" + count++ + ": " + inputText + " → " + outKey.toDisplayString();
            if (inputVariants.size() > 1) line += " (any order)";
            line += "\n";
            if (page.length() + line.length() > PAGE_CHAR_LIMIT) {
                pages.add(page.toString());
                page = new StringBuilder();
                page.append(color).append("§l").append(contHeader).append("§r\n");
            }
            page.append(line);
        }
        if (page.length() > 0) {
            pages.add(page.toString());
        }
    }

    /**
     * Helper class for grouping by output (item type + count).
     */
    private static class OutputKey {
        private final String itemName;
        private final int count;
        public OutputKey(ItemStack stack) {
            this.itemName = stack.getDisplayName().getString();
            this.count = stack.getCount();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OutputKey that = (OutputKey) o;
            return count == that.count && itemName.equals(that.itemName);
        }
        @Override
        public int hashCode() {
            return Objects.hash(itemName, count);
        }
        public String toDisplayString() {
            return count + "x " + itemName;
        }
        @Override
        public String toString() {
            return toDisplayString();
        }
    }

    /**
     * Helper class for grouping by input set (order-irrelevant, item type + count).
     */
    private static class InputSet {
        private final Set<String> items;
        public InputSet(List<ItemStack> stacks) {
            this.items = new HashSet<>();
            for (ItemStack stack : stacks) {
                this.items.add(stack.getCount() + "x " + stack.getDisplayName().getString());
            }
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InputSet inputSet = (InputSet) o;
            return items.equals(inputSet.items);
        }
        @Override
        public int hashCode() {
            return items.hashCode();
        }
        public String toDisplayString() {
            // Sort for consistent display
            List<String> sorted = new ArrayList<>(items);
            sorted.sort(String::compareTo);
            return String.join(" + ", sorted);
        }
    }
}
