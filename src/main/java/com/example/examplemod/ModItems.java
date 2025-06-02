package com.example.examplemod;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item",
            () -> new Item(new Properties()));

    public static final RegistryObject<Item> TRADE_TERMINAL_ITEM = ITEMS.register("trade_terminal",
            () -> new BlockItem(com.example.examplemod.ModBlocks.TRADE_TERMINAL.get(), new Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
