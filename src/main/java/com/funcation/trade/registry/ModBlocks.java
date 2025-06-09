package com.funcation.trade.registry;

import com.funcation.trade.blocks.TradeBlock;
import com.funcation.FuncationTradeMod;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FuncationTradeMod.MOD_ID);

    public static final RegistryObject<Block> TRADE_BLOCK = BLOCKS.register("trade_block", TradeBlock::new);
}

