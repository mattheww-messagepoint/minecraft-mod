package com.funcation.events;

import com.funcation.registry.ModMenuTypes;
import com.funcation.screen.TradeBlockMenu;
import com.funcation.screen.TradeBlockScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Handles client-side mod events, such as screen registration.
 * @since 1.0.0
 */
@Mod.EventBusSubscriber(modid = "funcation", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.TRADE_BLOCK_MENU.get(), TradeBlockScreen::new);
        });
    }
}

