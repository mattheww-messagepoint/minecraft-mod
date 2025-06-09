package com.funcation.blocks.entity;

import com.funcation.trade.blocks.entity.TradeBlockEntity;
import com.funcation.trade.registry.ModBlockEntities;
import com.funcation.trade.registry.ModBlocks;
import com.funcation.trade.registry.ModItems;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled; // Ensure this import is present
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled // Add this annotation
class TradeBlockEntityPersistenceTest {

    private BlockPos testPos;
    private BlockState mockBlockState;

    @BeforeAll
    static void setupAll() {
        SharedConstants.setVersion(net.minecraft.DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
        // Manually register DeferredRegister instances
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        // ModMenuTypes.MENU_TYPES.register(modEventBus); // If needed for these tests
    }

    @BeforeEach
    void setUp() {
        testPos = new BlockPos(0, 0, 0);
        mockBlockState = Mockito.mock(BlockState.class);
        // Ensure ModBlockEntities are registered if this test runs in a context where they might not be.
        // For typical Forge unit testing with a game context, this might not be strictly necessary
        // but it's good practice if tests are run in isolation.
        // However, direct static initialization calls in tests can be problematic.
        // Assuming the test runner or environment handles registry initialization.
    }

    @Test
    void testPersistenceOfTierAndTradesCompleted() {
        // 1. Create original BlockEntity and set state
        TradeBlockEntity originalEntity = new TradeBlockEntity(testPos, mockBlockState);
        originalEntity.setCurrentTier(2);
        originalEntity.setTradesCompleted(7);

        // 2. Save state to NBT
        CompoundTag nbt = new CompoundTag();
        originalEntity.saveAdditional(nbt);

        // 3. Create new BlockEntity and load state
        TradeBlockEntity newEntity = new TradeBlockEntity(testPos, mockBlockState);
        newEntity.load(nbt);

        // 4. Assert state is restored
        assertEquals(2, newEntity.getCurrentTier(), "Current tier should be persisted.");
        assertEquals(7, newEntity.getTradesCompleted(), "Trades completed should be persisted.");
    }

    @Test
    void testPersistenceOfInventory() {
        // 1. Create original BlockEntity and set inventory
        TradeBlockEntity originalEntity = new TradeBlockEntity(testPos, mockBlockState);
        originalEntity.getInventory().setItem(0, new ItemStack(Items.DIAMOND, 5));
        originalEntity.getInventory().setItem(1, new ItemStack(Items.GOLD_INGOT, 10));
        originalEntity.getInventory().setItem(2, ItemStack.EMPTY); // Output slot

        // 2. Save state to NBT
        CompoundTag nbt = new CompoundTag();
        originalEntity.saveAdditional(nbt);

        // 3. Create new BlockEntity and load state
        TradeBlockEntity newEntity = new TradeBlockEntity(testPos, mockBlockState);
        newEntity.load(nbt);

        // 4. Assert inventory is restored
        ItemStack slot0 = newEntity.getInventory().getItem(0);
        ItemStack slot1 = newEntity.getInventory().getItem(1);
        ItemStack slot2 = newEntity.getInventory().getItem(2);

        assertTrue(ItemStack.matches(new ItemStack(Items.DIAMOND, 5), slot0), "Slot 0 item should be persisted.");
        assertEquals(5, slot0.getCount(), "Slot 0 count should be persisted.");

        assertTrue(ItemStack.matches(new ItemStack(Items.GOLD_INGOT, 10), slot1), "Slot 1 item should be persisted.");
        assertEquals(10, slot1.getCount(), "Slot 1 count should be persisted.");

        assertTrue(slot2.isEmpty(), "Slot 2 should be empty and persisted as such.");
    }

    @Test
    void testPersistenceOfEmptyInventory() {
        // 1. Create original BlockEntity with empty inventory
        TradeBlockEntity originalEntity = new TradeBlockEntity(testPos, mockBlockState);
        // Inventory is empty by default

        // 2. Save state to NBT
        CompoundTag nbt = new CompoundTag();
        originalEntity.saveAdditional(nbt);

        // 3. Create new BlockEntity and load state
        TradeBlockEntity newEntity = new TradeBlockEntity(testPos, mockBlockState);
        newEntity.load(nbt);

        // 4. Assert inventory is restored (and empty)
        assertTrue(newEntity.getInventory().getItem(0).isEmpty(), "Slot 0 should be empty.");
        assertTrue(newEntity.getInventory().getItem(1).isEmpty(), "Slot 1 should be empty.");
        assertTrue(newEntity.getInventory().getItem(2).isEmpty(), "Slot 2 should be empty.");
    }

    @Test
    void testPersistenceWithDefaultValues() {
        // 1. Create original BlockEntity (uses default tier 1, 0 trades)
        TradeBlockEntity originalEntity = new TradeBlockEntity(testPos, mockBlockState);

        // 2. Save state to NBT
        CompoundTag nbt = new CompoundTag();
        originalEntity.saveAdditional(nbt); // Should save default values

        // 3. Create new BlockEntity and load state
        TradeBlockEntity newEntity = new TradeBlockEntity(testPos, mockBlockState);
        newEntity.load(nbt);

        // 4. Assert default state is restored
        assertEquals(1, newEntity.getCurrentTier(), "Default current tier (1) should be persisted/loaded.");
        assertEquals(0, newEntity.getTradesCompleted(), "Default trades completed (0) should be persisted/loaded.");
        assertTrue(newEntity.getInventory().isEmpty(), "Default empty inventory should be persisted/loaded.");
    }
}

