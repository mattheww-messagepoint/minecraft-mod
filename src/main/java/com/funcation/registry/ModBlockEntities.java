package com.funcation.registry;

import com.funcation.blocks.entity.TradeBlockEntity;
import com.funcation.Funcation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.funcation.registry.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Funcation.MOD_ID);

    public static final RegistryObject<BlockEntityType<TradeBlockEntity>> TRADE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "trade_block_entity",
            () -> BlockEntityType.Builder.of(TradeBlockEntity::new, ModBlocks.TRADE_BLOCK.get()).build(null)
    );
}

