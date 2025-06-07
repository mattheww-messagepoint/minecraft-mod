# CR002 - Implementation Tasks: Trade Guide Book (Dynamic Trade Reference)

## Overview
This task covers the implementation of an in-game book item ("Trade Guide Book") that dynamically displays all available trades and their unlock requirements for the Early Game Trade Block. The book's content should always reflect the current trade data, updating automatically if trades are changed in code. This ensures players have a single, always-accurate reference for all trade tiers and requirements.

## Relevant Files
- `src/main/java/com/funcation/item/TradeGuideBookItem.java` - Book item class
- `src/main/java/com/funcation/registry/ModItems.java` - Item registration
- `src/main/java/com/funcation/data/TradeManager.java` - Source of trade data (tiers, requirements)
- `src/main/java/com/funcation/data/trades/TradeOffer.java` - Trade offer structure
- `src/main/resources/assets/funcation/models/item/trade_guide_book.json` - Item model
- `src/main/resources/assets/funcation/textures/item/trade_guide_book.png` - Item texture
- `src/main/resources/data/funcation/recipes/trade_guide_book.json` - Crafting recipe
- (Optional) `src/main/resources/assets/funcation/lang/en_us.json` - Localization for book title/pages

## Tasks

- [x] 1.0 Create Trade Guide Book Item
  - [x] 1.1 Create `TradeGuideBookItem` class in `com.funcation.item`
  - [x] 1.2 Register the item in `ModItems.java`
  - [x] 1.3 Add item model and placeholder texture
  - [x] 1.4 Add crafting recipe

- [x] 2.0 Dynamic Book Content Generation
  - [x] 2.1 Implement logic in `TradeGuideBookItem` to generate book content from `TradeManager`'s trade lists (all tiers)
  - [x] 2.2 Format content for readability (tiers, locked/unlocked status, requirements)
  - [x] 2.3 (Optional) Add localization support for book title and content

- [x] 3.0 Book Usage and Display
  - [x] 3.1 Implement right-click/use behavior to open the book and display generated content (as a written book or custom GUI)
  - [x] 3.2 Ensure book always reflects current trade data (no hardcoded text)

- [x] 4.0 Testing and Polish
  - [x] 4.1 Test book in-game for accuracy and formatting
  - [x] 4.2 Adjust formatting for clarity (pages, sections, locked trade notes)
  - [x] 4.3 Confirm book updates automatically if trades are changed in code
  - [x] 4.4 (Optional) Add to creative tab or starting inventory for testing

## Success Criteria
- Book always displays all trades (all tiers) and unlock requirements
- Book content updates automatically if trades are changed in code
- No need to update book text in multiple places
- Book is accessible and readable in-game
- Formatting is clear and user-friendly
