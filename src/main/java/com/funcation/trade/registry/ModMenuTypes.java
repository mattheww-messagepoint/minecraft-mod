package com.funcation.trade.registry;

import com.funcation.FuncationTradeMod;
import com.funcation.trade.screen.TradeBlockMenu;
import com.funcation.FuncationTradeMod;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FuncationTradeMod.MOD_ID);

    public static final RegistryObject<MenuType<TradeBlockMenu>> TRADE_BLOCK_MENU =
            MENU_TYPES.register("trade_block_menu",
                    () -> new MenuType<>(
                            (windowId, inv) -> new TradeBlockMenu(windowId, inv, ContainerLevelAccess.NULL),
                            FeatureFlags.VANILLA_SET
                    )
            );
}
