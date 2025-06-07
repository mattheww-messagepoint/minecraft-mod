package com.funcation.registry;

import com.funcation.blocks.TradeBlock;
import com.funcation.Funcation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Funcation.MOD_ID);

    public static final RegistryObject<Block> TRADE_BLOCK = BLOCKS.register("trade_block", TradeBlock::new);
}

