package com.funcation.registry;

import com.funcation.screen.TradeBlockMenu;
import com.funcation.Funcation;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Funcation.MOD_ID);

    public static final RegistryObject<MenuType<TradeBlockMenu>> TRADE_BLOCK_MENU =
            MENU_TYPES.register("trade_block_menu",
                    () -> new MenuType<>(
                            (windowId, inv) -> new TradeBlockMenu(windowId, inv, ContainerLevelAccess.NULL),
                            FeatureFlags.VANILLA_SET
                    )
            );
}
