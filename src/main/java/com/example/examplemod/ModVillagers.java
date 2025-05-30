package com.example.examplemod;

import com.google.common.collect.ImmutableSet;
import com.example.examplemod.ModBlocks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.Blocks;

// Change to FORGE bus for VillagerTradesEvent
@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ExampleMod.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ExampleMod.MODID);

    public static final RegistryObject<PoiType> COMMODITY_POI = POI_TYPES.register("commodity_poi",
            () -> new PoiType(ImmutableSet.of(com.example.examplemod.ModBlocks.TRADING_POST.get().defaultBlockState()), 1, 1));

    public static final RegistryObject<VillagerProfession> COMMODITY_TRADER = PROFESSIONS.register("commodity_trader",
            () -> new VillagerProfession("commodity_trader",
                    holder -> holder.value() == COMMODITY_POI.get(),
                    holder -> holder.value() == COMMODITY_POI.get(),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    net.minecraft.sounds.SoundEvents.VILLAGER_WORK_ARMORER));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        PROFESSIONS.register(eventBus);
    }

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (event.getType() == COMMODITY_TRADER.get()) {
            event.getTrades().get(1).add(new com.example.examplemod.TradeOffers.SimpleTrade(
                    Items.STONE.getDefaultInstance(),
                    Items.COAL.getDefaultInstance(),
                    16, 10, 0.05F
            ));

            event.getTrades().get(2).add(new com.example.examplemod.TradeOffers.SimpleTrade(
                    Items.COAL.getDefaultInstance(),
                    Items.IRON_INGOT.getDefaultInstance(),
                    12, 10, 0.05F
            ));

            event.getTrades().get(3).add(new com.example.examplemod.TradeOffers.SimpleTrade(
                    Items.IRON_INGOT.getDefaultInstance(),
                    Items.DIAMOND.getDefaultInstance(),
                    8, 10, 0.05F
            ));
        }
    }
}

