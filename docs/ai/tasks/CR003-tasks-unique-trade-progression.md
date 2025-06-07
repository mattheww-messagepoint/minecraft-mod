# CR003 - Refactor Trade Progression to Unique Trade Completion

## Objective

Change the trade progression system so that players progress to the next trade tier by completing a configurable number of unique trades, rather than by the total number of trades performed in the current tier. The number of unique trades required should be configurable for each tier individually.

## Relevant Files

- `src/main/java/com/funcation/data/TradeManager.java` - Handles trade tiers, progression, and configuration.
- `src/main/java/com/funcation/blocks/entity/TradeBlockEntity.java` - Stores and updates player trade progress.
- `src/main/java/com/funcation/screen/TradeBlockMenu.java` - May need UI updates for new progression logic.
- `src/main/java/com/funcation/data/trades/TradeOffer.java` - Used for trade identity and uniqueness.
- `src/main/resources/config/funcation-common.toml` - Config file for unique trades required per tier.
- `src/main/java/com/funcation/player/PlayerTradeProgress.java` - Stores and manages each player's unique trade progress (capability or NBT).
- `src/main/java/com/funcation/ModConfig.java` - Forge config definition for unique_trades_required_per_tier.

## Tasks

- [x] 1.0 Add Configurable Unique Trade Requirement Per Tier
  - [x] 1.1 Add a config option for the number of unique trades required to unlock each tier (e.g., a list or map in the config file, one value per tier).
  - [x] 1.2 Document the config option in the config file and technical docs.

- [x] 2.0 Track Unique Trades Per Player
  - [x] 2.1 Implement a data structure to track which unique trades a player has completed per tier (e.g., using trade IDs or hashes).
  - [x] 2.2 Store this data in the player's persistent data (capability or NBT).
  - [x] 2.3 Ensure data is saved/loaded correctly for singleplayer and multiplayer.

- [x] 3.0 Update Progression Logic
  - [x] 3.1 Refactor progression checks to use the count of unique trades completed in the current tier.
  - [x] 3.2 Only allow tier progression when the configured number of unique trades for that tier is reached.
  - [x] 3.3 Prevent duplicate counting of the same trade.

- [x] 4.0 Update Trade Completion Handling
  - [x] 4.1 On trade completion, record the trade as completed for the player if it is unique for the current tier.
  - [x] 4.2 Ensure this works for all trade tiers.

- [x] 5.0 UI/Feedback (Optional but Recommended)
  - [x] 5.1 Update the UI to show the number of unique trades completed and required for the next tier (per-tier display).
  - [x] 5.2 Add tooltips or progress bars as needed.

- [x] 6.0 Testing
  - [x] 6.1 Test progression in singleplayer and multiplayer.
  - [x] 6.2 Ensure duplicate trades do not count multiple times.
  - [x] 6.3 Verify per-tier config option works and is respected.
  - [x] 6.4 Confirm data is persistent across sessions.

## Notes

- Use `TradeOffer#equals` and `hashCode` for trade uniqueness.
- Ensure backward compatibility for existing saves if possible.
- Document all changes in the technical specification.
