package com.funcation;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class TradeModConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> uniqueTradesRequiredPerTier;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("trade");
            uniqueTradesRequiredPerTier = builder
                .comment("List of unique trades required to unlock each trade tier. Each value corresponds to a trade tier (starting from tier 1).")
                .defineListAllowEmpty(
                    "unique_trades_required_per_tier",
                    java.util.Arrays.asList(2, 3, 4),
                    obj -> obj instanceof Integer && (Integer) obj > 0
                );
            builder.pop();
        }
    }
}

