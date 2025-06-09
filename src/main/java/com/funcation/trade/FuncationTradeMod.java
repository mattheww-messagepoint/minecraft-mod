package com.funcation;

import com.funcation.trade.registry.ModBlockEntities;
import com.funcation.trade.registry.ModBlocks;
import com.funcation.trade.registry.ModItems;
import com.funcation.trade.registry.ModMenuTypes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Main mod class for Funcation.
 * Handles mod initialization and registry setup.
 * @since 1.0.0
 */
@Mod(FuncationTradeMod.MOD_ID)
public class FuncationTradeMod {
    public static final String MOD_ID = "funcation";

    public FuncationTradeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        net.minecraftforge.common.ForgeConfigSpec commonSpec = com.funcation.TradeModConfig.COMMON_SPEC;
        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
    }
}
