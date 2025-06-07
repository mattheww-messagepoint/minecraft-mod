# Funcation Mod - Technical Specification

This document serves as a technical guide for development standards applicable to the Funcation Minecraft mod. It contains information about tooling versions, coding standards, and best practices to maintain consistency throughout development.

## Table of Contents
- [Tooling Versions](#tooling-versions)
- [Project Structure](#project-structure)
- [Coding Standards and Best Practices](#coding-standards-and-best-practices)
  - [Project Organization](#project-organization)
  - [Registration Patterns](#registration-patterns)
  - [Event Handling](#event-handling)
  - [Item/Block Creation](#itemblock-creation)
  - [Naming Conventions](#naming-conventions)
  - [Documentation](#documentation)
  - [Localization](#localization)
  - [Block Entity Implementation](#block-entity-implementation)
  - [MenuType Registration (Forge 1.21.x)](#menutype-registration-forge-121x)
  - [Menu/Screen Construction with Block Entities (Forge 1.21.x)](#menuscreen-construction-with-block-entities-forge-121x)
  - [ItemStack Comparison in Forge 1.21.x](#itemstack-comparison-in-forge-121x)
- [Common Pitfalls and Best Practices](#common-pitfalls-and-best-practices)
  - [BlockEntity Implementation Pitfalls](#blockentity-implementation-pitfalls)
  - [@Override Annotation Usage](#override-annotation-usage)
  - [Method Implementation Requirements](#method-implementation-requirements)
  - [Mod Configuration](#mod-configuration)
  - [Debugging Tips](#debugging-tips)
  - [Debugging and Testing Tips](#debugging-and-testing-tips)

## Tooling Versions

- **Minecraft**: 1.21.5
- **Forge**: 55.0.21-mdk
- **Java**: 17 (required for Minecraft 1.21.x)
- **Gradle**: 8.x (as configured in the Forge MDK)
- **IDE**: Any with proper Gradle support (IntelliJ IDEA recommended)

## Project Structure

```
forge-1.21.5-55.0.21-mdk/
├── src/main/java/com/funcation/           # Main Java source files
│   ├── Funcation.java                     # Main mod class
│   ├── registry/                          # Registration classes
│   ├── events/                            # Event handlers
│   ├── items/                             # Custom item classes
│   ├── blocks/                            # Custom block classes
│   └── util/                              # Utility classes
├── src/main/resources/
│   ├── META-INF/mods.toml                 # Mod metadata
│   ├── pack.mcmeta                        # Resource pack metadata
│   └── assets/funcation/                  # Textures, models, etc.
├── src/generated/                         # Generated resources (data generators)
├── build.gradle                           # Gradle build script
├── gradle.properties                      # Gradle properties
└── docs/ai/                               # AI documentation and specifications
```

## Coding Standards and Best Practices

### Project Organization
- Use `com.funcation` as the root package
- Organize classes by functionality in subpackages (items, blocks, etc.)
- Use the following folder structure for clarity:
  - `registry/` for registration classes
  - `events/` for event handlers
  - `items/` for custom item classes
  - `blocks/` for custom block classes
  - `util/` for utility classes

### Registration Patterns
- Use Forge's DeferredRegister system for all registrable objects
- Create separate registration classes for each type (blocks, items, etc.)
- Example registration:
  ```java
  // Example: Registering a custom item. Replace 'example_item' with your item's registry name.
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Funcation.MOD_ID);
  public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", 
      () -> new Item(new Item.Properties()));
  ```

### Event Handling
- Use the `@Mod.EventBusSubscriber` annotation for event handlers
- Separate event handlers by functionality or bus type
- Follow this pattern for event methods:

```java
@SubscribeEvent
public static void onEvent(EventType event) {
    // Event handling code
}
```

### Item/Block Creation
- Use builder pattern for properties
- Use method chaining where appropriate
- Define item properties in a consistent way:

```java
new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(64));
```

### Naming Conventions
- Use snake_case for resource identifiers (textures, models, etc.)
- Use PascalCase for Java classes
- Use camelCase for methods and variables
- Use UPPER_SNAKE_CASE for constants

### Documentation
- Add Javadoc comments for all public classes and methods
- Include parameter descriptions and return value descriptions
- Include `@since` tags to track when features were added

### Localization
- Use en_us.json as the base language file
- Use translation keys in the format: `item.funcation.example_item`

### Block Entity Implementation
- **Registration**: Each custom `BlockEntity` requires a corresponding `BlockEntityType` to be registered.
  - Use `DeferredRegister<BlockEntityType<?>>` and `ForgeRegistries.BLOCK_ENTITY_TYPES`.
  - Example registration:
    ```java
    // In your registration class (e.g., ModBlockEntities.java)
    // Replace 'YourBlockEntity' and 'your_block_entity_name' with your actual class and registry name.
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Funcation.MOD_ID);

    public static final RegistryObject<BlockEntityType<YourBlockEntity>> YOUR_BLOCK_ENTITY_TYPE =
        BLOCK_ENTITY_TYPES.register("your_block_entity_name", () ->
            BlockEntityType.Builder.of(YourBlockEntity::new, ModBlocks.YOUR_ASSOCIATED_BLOCK.get()).build(null));
    ```
- **Constructor**: The `BlockEntity` subclass constructor must call `super()` with the registered `BlockEntityType` and the `BlockPos` and `BlockState`.
  - Example:
    ```java
    // In YourBlockEntity.java
    public YourBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.YOUR_BLOCK_ENTITY_TYPE.get(), pos, state);
    }
    ```

### MenuType Registration (Forge 1.21.x)
- Always use Forge's DeferredRegister for menu types.
- Register your MenuType with a MenuSupplier (lambda or method reference) matching your menu's constructor signature.
- Always pass `FeatureFlags.VANILLA_SET` as the second argument to the MenuType constructor.
- Example:

```java
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

// Replace 'MyMenu' and 'my_menu' with your actual menu class and registry name.
public static final DeferredRegister<MenuType<?>> MENU_TYPES =
    DeferredRegister.create(ForgeRegistries.MENU_TYPES, Funcation.MOD_ID);

public static final RegistryObject<MenuType<MyMenu>> MY_MENU =
    MENU_TYPES.register("my_menu",
        () -> new MenuType<>((windowId, inv) -> new MyMenu(windowId, inv, ContainerLevelAccess.NULL), FeatureFlags.VANILLA_SET));
```
- If your menu requires extra data (e.g., BlockPos), use a custom factory and handle data transfer with FriendlyByteBuf.

### Menu/Screen Construction with Block Entities (Forge 1.21.x)
- When creating a custom Menu (container) for a BlockEntity:
  - Only resolve the BlockEntity instance in the server-side constructor (using ContainerLevelAccess).
  - The client-side constructor (using FriendlyByteBuf) must NOT access the BlockEntity, as it does not exist on the client at menu construction time. Instead, set the reference to null or use a safe fallback.
  - All usages of the blockEntity field in the menu must be guarded with null checks or provide safe defaults to prevent client-side crashes or menu auto-closing.
- Example pattern:
  ```java
  // Server-side constructor (replace 'MyMenu' and 'MyBlockEntity' with your actual classes)
  public MyMenu(int id, Inventory inv, ContainerLevelAccess access) {
      this.blockEntity = access.evaluate((level, pos) -> level.getBlockEntity(pos) instanceof MyBlockEntity be ? be : null).orElse(null);
      // ...
  }
  // Client-side constructor
  public MyMenu(int id, Inventory inv, FriendlyByteBuf buf) {
      this(id, inv, ContainerLevelAccess.create(inv.player.level(), buf.readBlockPos()));
      // blockEntity will be null on the client
  }
  // Usage
  if (blockEntity != null) { /* server logic */ } else { /* client fallback */ }
  ```
- This pattern prevents menu auto-closing, NPEs, and sync issues, and is recommended for Forge 1.21.x and newer.

### ItemStack Comparison in Forge 1.21.x
- The `sameItem` method does **not** exist on `ItemStack` in Minecraft Forge 1.21.x.
- To check if two `ItemStack`s are the same item type, use `ItemStack#is(Item)`:
  ```java
  // Example: Check if two ItemStacks are the same item type
  if (stack1.is(stack2.getItem())) { /* ... */ }
  ```
- Do **not** use `sameItem` or `is(ItemStack)` for simple item type checks.
- This is important for trade logic, inventory checks, and similar code.

## Common Pitfalls and Best Practices

### BlockEntity Implementation Pitfalls
- **Never use `null` for BlockEntityType**: Always use your registered BlockEntityType in the constructor:
  ```java
  // WRONG
  super(null, pos, state); 
  
  // CORRECT
  super(ModBlockEntities.YOUR_BLOCK_ENTITY_TYPE.get(), pos, state);
  ```

- **NBT Serialization Methods**: Use the correct method names for saving/loading data:
  ```java
  // For reading NBT data:
  @Override
  public void load(CompoundTag tag) {
      super.load(tag);
      // Your loading code here
  }
  
  // For writing NBT data:
  @Override
  protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      // Your saving code here
  }
  ```

- **Container NBT Handling**: When using `SimpleContainer` for inventory:
  ```java
  // Loading container items
  @Override
  public void load(CompoundTag tag) {
      super.load(tag);
      if (tag.contains("Items")) {
          inventory.fromTag(tag.getList("Items", 10)); // 10 is the NBT tag type for compound tags
      }
  }
  
  // Saving container items
  @Override
  protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.put("Items", inventory.createTag());
  }
  ```

### @Override Annotation Usage
- Only use `@Override` when a method actually overrides a method from a parent class or implemented interface
- Verify method signatures match exactly with the parent method before using `@Override`
- IDE features like "Generate Override Methods" can help ensure correct signatures

### Method Implementation Requirements
- When implementing `getTicker()` in a Block class, ensure the corresponding `tick()` method exists in the BlockEntity class
- Implement content dropping methods for BlockEntities to handle block breaking:
  ```java
  // Example: This method should be placed in your custom BlockEntity class
  public void dropContents(Level level, BlockPos pos) {
      Containers.dropContents(level, pos, this.inventory);
  }
  ```

### Mod Configuration
- Ensure `mods.toml` file contains entries for all mod IDs referenced in your code
- Match Forge and Minecraft version ranges in `mods.toml` to your development environment
- When migrating from example code, make sure to update all mod ID references
- **Trade Progression Configuration:**
  - The number of unique trades required to unlock each trade tier is configurable in `src/main/resources/config/funcation-common.toml`.
  - Use the `unique_trades_required_per_tier` list under the `[trade]` section. Each value in the list corresponds to a trade tier, starting from tier 1. For example: `[2, 3, 4]` means:
    - Tier 1 requires 2 unique trades to unlock Tier 2
    - Tier 2 requires 3 unique trades to unlock Tier 3
    - Tier 3 requires 4 unique trades to unlock Tier 4
  - Only unique trades (not repeated trades) count toward progression. A trade is considered unique if it has a different `TradeOffer` (uniqueness is determined by `TradeOffer#equals` and `hashCode`).
  - To change the requirement for a specific tier, adjust the corresponding value in the list. Changes will apply to all new and existing worlds after a reload.
  - If the list is shorter than the number of tiers, the last value will be used for all subsequent tiers.

### Debugging Tips
- Read compiler errors carefully, focusing on line numbers and missing/incorrect method signatures
- For NBT-related errors, check if you're using correct NBT compound tag types
- When BlockEntity-related crashes occur, ensure BlockEntityType registration happens before the Block registration
- Use logging liberally during development to trace execution flow and data handling

### Debugging and Testing Tips
- **Verify Registration:**
  - Use log statements in your registration classes to confirm that objects are registered as expected.
  - Check the logs for messages from DeferredRegister and Forge during startup.
  - If a block, item, or entity is missing in-game, check for typos in the registry name and ensure the registration code is executed.
- **Use Logging Effectively:**
  - Use SLF4J (Forge's default logger) for debug/info/warn/error messages.
  - Example (replace `Funcation.MOD_ID` with your actual mod ID if different):
    ```java
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    // Place this in your main mod class
    public static final Logger LOGGER = LoggerFactory.getLogger(Funcation.MOD_ID);
    // Usage example:
    LOGGER.info("Registering custom block entity");
    ```
  - Add log statements in lifecycle events (setup, registration, etc.) to trace execution.
- **Testing Registration:**
  - Use the /give and /summon commands in-game to test if items, blocks, and entities are registered.
  - Use the F3+H debug screen to see item/block IDs.
- **Crash Reports:**
  - Review crash reports in the run/crash-reports/ directory for stack traces and error messages.
  - Pay attention to the first error in the stack trace, as it often indicates the root cause.
- **Common Debugging Steps:**
  - Clean and rebuild the project if changes are not reflected.
  - Delete the run directory if persistent data is causing issues.
  - Use breakpoints and the debugger in your IDE to step through code.
- **Unit Testing:**
  - For logic that can be tested outside Minecraft, use JUnit tests in src/test/java.
  - Mock Minecraft classes where possible for isolated testing.

### Do's and Don'ts: Common Pitfalls and Best Practices

**Do:**
- Use DeferredRegister for all registry objects (blocks, items, entities, etc.).
- Use RegistryObject references until after registration is complete; only call `.get()` when safe.
- Add Javadoc to all public classes and methods.
- Use translation keys for all user-facing text.
- Guard all server-only logic with null checks or side checks in menus/screens.
- Use logging to trace registration and lifecycle events.
- Test registration in-game with /give, /summon, and F3+H.
- Clean and rebuild the project if you encounter unexplained issues.
- Use breakpoints and the debugger for complex bugs.

**Don't:**
- Don't call `.get()` on RegistryObject in static initializers or before registration events.
- Don't use `null` for BlockEntityType in constructors.
- Don't access BlockEntity or Level in client-side menu constructors.
- Don't use `sameItem` or `is(ItemStack)` for item type checks in 1.21.x; use `is(Item)` instead.
- Don't perform world or registry operations off the main thread.
- Don't forget to update mod IDs and version ranges in mods.toml when copying example code.
- Don't ignore the first error in a crash report—it's usually the root cause.
