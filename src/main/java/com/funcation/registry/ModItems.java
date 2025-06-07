package com.funcation.registry;

import com.funcation.Funcation;
import com.funcation.blocks.TradeBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Funcation.MOD_ID);

    public static final RegistryObject<Item> TRADE_BLOCK = ITEMS.register("trade_block",
            () -> new BlockItem(ModBlocks.TRADE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> TRADE_GUIDE_BOOK = ITEMS.register("trade_guide_book",
            () -> new com.funcation.item.TradeGuideBookItem(new Item.Properties()));
}
