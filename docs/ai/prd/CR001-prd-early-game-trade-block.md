# CR001 - Product Requirements Document: Early Game Trade Block

## Introduction/Overview

The Early Game Trade Block is a feature designed to reduce the tedium of early game resource collection without compromising game balance or challenge. This block allows players to exchange common early game resources for other materials they might need, reducing repetitive mining and gathering activities while maintaining the core progression and challenge of the game.

The trade block currently exists as a proof of concept with placeholder trades including late-game materials like diamonds and gold. This PRD outlines the requirements for refining this feature to focus exclusively on early game resources and implementing a progression system for unlocking better trades.

## Goals

1. Reduce tedious mining and collecting activities in the early game
2. Maintain game balance and challenge while improving quality of life
3. Provide a progression system for trades that rewards continued use
4. Create balanced exchange rates that feel fair but not overpowered
5. Ensure the feature benefits players at all experience levels
6. Slightly increase early game progression speed without trivializing gameplay
7. Lay groundwork for future mid and late game trade blocks

## User Stories

- As a new player, I want to exchange excess dirt and stone for more useful resources so that I can progress without excessive mining.
- As an experienced player, I want to trade basic resources for iron more efficiently so that I can focus on more engaging gameplay.
- As a builder, I want to exchange common materials for different varieties so that I can obtain specific building blocks without excessive gathering.
- As a survival player, I want to trade excess wood for coal so that I can continue smelting operations without constantly mining for fuel.
- As a player with limited playtime, I want to accelerate basic resource gathering so that I can enjoy more aspects of the game in my available time.

## Functional Requirements

1. The trade block shall be available to players from the start of the game
2. The trade block shall only offer trades for early game resources (dirt, stone, wood, coal, iron, copper, gravel, sticks)
3. The trade block shall implement a progression system where completing certain trades unlocks more advanced early game trades
4. The trade block shall have balanced exchange rates that maintain game challenge while reducing tedium
5. Each trade shall require a reasonable amount of input resources to prevent exploitation
6. The progression of trades shall follow the natural game progression (dirt/stone → wood/coal → copper → iron)
7. The trade block shall include trades for different variants of basic materials (e.g., different wood types, stone variants)
8. The trade block interface shall clearly indicate which trades are available and which are locked
9. The trade block shall maintain its state when placed in the world (remember unlocked trades)
10. The trade block shall have appropriate crafting costs that reflect its early game utility

## Non-Goals (Out of Scope)

1. The trade block will NOT include trades for mid or late game resources (gold, diamonds, netherite, etc.)
2. The trade block will NOT implement trades that create resource loops allowing infinite resource generation
3. The trade block will NOT significantly alter the overall difficulty or pace of early game progression
4. The trade block will NOT replace the need for mining and gathering completely
5. The trade block will NOT include specialized trades for enchantments, potions, or other advanced game features
6. The trade block will NOT include trades related to mob drops or combat rewards

## Design Considerations

1. The trade block UI should visually distinguish between available and locked trades
2. Locked trades should show a preview of required resources to unlock them
3. Consider using a progress bar or similar indicator to show how close the player is to unlocking the next tier
4. The trade block appearance should reflect its early game focus (using textures that align with early game aesthetics)
5. Consider tooltips explaining trade benefits for new players

## Technical Considerations

1. Ensure the trade block is added to the starting inventory for testing purposes
2. Implement a save data system to track unlocked trades per player or per block
3. Consider how the trade progression system will interact with multiplayer environments
4. Optimize the code to prevent lag when many trades are being processed
5. Ensure compatibility with other mods that might modify early game resources

## Trade Balance Framework

### Tier 1 Trades (Available Initially)
- Basic earth materials exchanges (dirt, gravel, stone variants)
- Simple wood exchanges (logs to planks with bonus, stick crafting efficiency)

### Tier 2 Trades (Unlocked after completing Tier 1 trades)
- Coal for wood logs or stone
- Stone for small amounts of copper
- More efficient wood variant exchanges

### Tier 3 Trades (Unlocked after completing Tier 2 trades)
- Copper for small amounts of iron
- More efficient coal acquisition
- Stone and wood for more valuable variants

### Exchange Rate Guidelines
- Exchanges should provide 10-25% efficiency gain compared to standard crafting/mining
- Higher tier materials should require exponentially more basic materials
- No trade should feel like a "cheat" but should represent a meaningful time savings

## Success Metrics

1. Players utilize the trade block regularly throughout early game progression
2. Reduction in player complaints about early game resource gathering tedium
3. No significant imbalance observed in early game progression speed
4. Players continue to engage with normal mining and crafting activities
5. Positive player feedback regarding quality of life improvement
6. No exploits discovered that allow unlimited resource generation

## Open Questions

1. Should there be a cooldown period between certain high-value trades?
2. Would implementing a durability system for the trade block enhance balance?
3. Should the trade block have different variants with specialized trade focuses?
4. How will the transition to mid-game trade blocks work in the future?
5. Should certain trades be tied to advancements or achievements?
