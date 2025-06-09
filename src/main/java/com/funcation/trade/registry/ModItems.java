package com.funcation.trade.registry;

import com.funcation.FuncationTradeMod;
import com.funcation.trade.blocks.TradeBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FuncationTradeMod.MOD_ID);

    public static final RegistryObject<Item> TRADE_BLOCK = ITEMS.register("trade_block",
            () -> new BlockItem(ModBlocks.TRADE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> TRADE_GUIDE_BOOK = ITEMS.register("trade_guide_book",
            () -> new com.funcation.trade.item.TradeGuideBookItem(new Item.Properties()));
}
