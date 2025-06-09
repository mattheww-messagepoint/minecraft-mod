package com.funcation.trade.registry;


import com.funcation.FuncationTradeMod;
import com.funcation.trade.blocks.entity.TradeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FuncationTradeMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<TradeBlockEntity>> TRADE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "trade_block_entity",
            () -> BlockEntityType.Builder.of(TradeBlockEntity::new, ModBlocks.TRADE_BLOCK.get()).build(null)
    );
}

