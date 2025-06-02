package com.example.examplemod;

import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;


public class TradeOffers {
    public static class SimpleTrade implements ItemListing {
        private final ItemStack input;
        private final ItemStack output;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public SimpleTrade(ItemStack input, ItemStack output, int maxUses, int villagerXp, float priceMultiplier) {
            this.input = input;
            this.output = output;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource randomSource) {
            // Optionally check if entity is a Villager
            return new MerchantOffer(input, output, maxUses, villagerXp, priceMultiplier);
        }
    }
}

