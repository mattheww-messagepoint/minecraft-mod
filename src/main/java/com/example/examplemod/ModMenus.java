package com.example.examplemod;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags; // Import FeatureFlags
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExampleMod.MODID);

    public static final RegistryObject<MenuType<TradeTerminalMenu>> TRADE_TERMINAL_MENU = MENUS.register(
            "trade_terminal_menu",
            () -> new MenuType<>(TradeTerminalMenu::new, FeatureFlags.VANILLA_SET) // Pass FeatureFlags.VANILLA_SET
    );

    public static void register(net.minecraftforge.eventbus.api.IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}

