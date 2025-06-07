# CR001 - Early Game Trade Block

## Overview
The Early Game Trade Block is a Minecraft mod feature designed to streamline early game resource collection by allowing players to exchange common materials for other useful resources. It aims to reduce repetitive mining and gathering, while maintaining game balance and progression.

## Key Features
- **Accessible Early**: Available from the start of the game, with a crafting recipe reflecting its early utility.
- **Tiered Trades**: Trades are organized into three tiers, unlocked through usage and progression:
  - **Tier 1**: Basic exchanges (dirt, gravel, stone, saplings, seeds, leaves, stone tools)
  - **Tier 2**: Unlocks after 5 Tier 1 trades (charcoal to coal, tool recycling, flint to arrows, rotten flesh to leather)
  - **Tier 3**: Unlocks after 10 Tier 2 trades (advanced crafting alternatives, copper ingots, gold nuggets, specialty items)
- **Progression System**: Completing trades unlocks higher tiers, following natural game progression.
- **Balanced Exchange Rates**: Trades offer a 10-25% efficiency gain over standard crafting/mining, with no infinite loops or exploits.
- **Persistent State**: The block remembers unlocked trades when placed in the world.
- **User-Friendly UI**: The interface clearly shows available and locked trades, with tooltips and progress indicators.

## Usage
1. **Crafting**: Craft the Trade Block using the provided recipe.
2. **Placement**: Place the block in the world.
3. **Trading**: Interact with the block to open the UI. Insert required resources into input slots and collect outputs.
4. **Progression**: Complete trades to unlock higher tiers. Locked trades show requirements and progress.

## Technical Details
- **Main Classes**:
  - `TradeBlock.java`: Block logic and placement
  - `TradeBlockEntity.java`: Handles inventory and trade state
  - `TradeBlockMenu.java` & `TradeBlockScreen.java`: UI and interaction
  - `TradeManager.java` & `TradeOffer.java`: Trade data, tiers, and progression
- **Persistence**: Trade unlocks are saved per block (or per player, if configured)
- **Multiplayer**: All trade actions and progression are synchronized across clients
- **Testing**: The block is added to starting inventory for testing; all trades and progression are tested for balance and exploits

## Design Considerations
- Trades are visually separated by tier in the UI
- Locked trades display requirements and progress bars
- Tooltips explain trade benefits for new players
- The block uses early-game textures and models

## Limitations
- Only early game resources are tradable (no diamonds, netherite, etc.)
- No resource loops or infinite generation
- Does not replace mining/gathering, only reduces tedium
- No trades for advanced features (enchantments, potions, most mob drops)

## Extending the Feature
- The system is designed to allow future mid/late game trade blocks
- Trade tiers and rates can be adjusted in `TradeManager.java`

## Success Metrics
- Regular player use in early game
- Reduced complaints about resource tedium

## Trade Details
### Tier 1 Trades
- Dirt to Gravel (8:1): Early flint access
- Gravel to Flint (6:1): Saves RNG grind for arrows/tools
- Sticks to Charcoal (4:1): Early fuel option
- Cobblestone to Andesite (3:1): Aesthetic building assistance
- Saplings to Bone Meal (5:1): All sapling types supported
- Stone Tools to Cobblestone (1:1): Disposal option for old tools
- Wheat Seeds to Bread (10:1): Reduces surplus seeds
- Sand + Gravel to Gray Concrete Powder: Early decorative blocks
- Apple to Sticks (1:2): Recycle apples if not needed
- Oak Leaves to Stick (4:1): Encourages leaf gathering

### Tier 2 Trades (Unlocks after 5 Tier 1 trades)
- Charcoal to Coal (4:1): Transition fuel, ore equivalency
- Stone Tools to Iron Nugget (4:1): Tool recycling incentive
- Flint to Arrows (6:4): Prepares for ranged combat
- Rotten Flesh to Leather (10:1): Alternative to cow farming
- Wheat Seeds to Bone Meal (8:1): Farming conversion
- Saplings to Apple (6:1): All sapling types supported
- Sand + Charcoal to Glass: Early access to decorative blocks

### Tier 3 Trades (Unlocks after 10 Tier 2 trades)
- Charcoal + Gravel to Copper Ingot: Renewable ore pathway
- Stone Tools + Flint to Iron Nugget: Advanced tool recycling
- Rotten Flesh + Dirt to Leather: Enhanced farming alternative
- Sapling + Apple to Gold Nugget: Woodland value conversion
- Torch + Flint + Gravel to Arrows: Combat utility
- Copper Ingot + Charcoal to Lightning Rod: Weather protection
- Sand + Glass Pane to Blue Stained Glass: Decorative options
- Rotten Flesh + Gravel to Compass: Navigation aid
- Flint + Stick to Crossbow: Ranged combat upgrade
