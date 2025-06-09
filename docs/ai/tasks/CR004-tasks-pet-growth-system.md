## Relevant Files

- `src/main/java/com/examplemod/entity/PetEntity.java` - Main class for the custom pet entity, handling growth stages, interactions, and rapid testing logic.
- `src/main/java/com/examplemod/entity/PetEntityRenderer.java` - Handles rendering and appearance changes for the pet at each stage.
- `src/main/java/com/examplemod/item/PetSpawnEggItem.java` - Item to spawn the pet in the world for testing and gameplay.
- `src/main/java/com/examplemod/item/TreatItem.java` - Item to accelerate pet growth.
- `src/main/java/com/examplemod/block/FeederBlock.java` - Custom block for feeding and watering the pet.
- `src/main/java/com/examplemod/block/CageBlock.java` - Custom block/item for storing the pet in inventory.
- `src/main/java/com/examplemod/gui/PetStatusScreen.java` - UI for pet status and management.
- `src/main/resources/assets/examplemod/models/entity/pet/` - Directory for pet model and texture files (one per growth stage).
- `src/test/java/com/examplemod/entity/PetEntityTest.java` - Unit tests for pet entity logic and growth progression.
- `src/test/java/com/examplemod/item/TreatItemTest.java` - Unit tests for treat item logic.

### Notes

- Unit tests should typically be placed alongside the code files they are testing (e.g., `PetEntity.java` and `PetEntityTest.java` in the same directory).
- Use `npx jest [optional/path/to/test/file]` to run tests. Running without a path executes all tests found by the Jest configuration.
- For rapid testing, clicking the pet in development mode will progress its growth stage.

## Tasks

- [ ] 1.0 Create the Pet Entity and Growth Stages
  - [ ] 1.1 Design the pet's data structure, including growth stage state and persistence.
  - [ ] 1.2 Implement the pet entity class with four growth stages.
  - [ ] 1.3 Add logic for changing appearance, size, and abilities at each stage.
  - [ ] 1.4 Ensure pet state persists between sessions.
  - [ ] 1.5 Write unit tests for growth logic and persistence.
- [ ] 2.0 Implement Rapid Testing Mechanism for Growth
  - [ ] 2.1 Add a debug/development mode flag to enable rapid testing.
  - [ ] 2.2 Implement click-to-progress logic for the pet entity in dev mode.
  - [ ] 2.3 Add log or UI feedback when the pet progresses a stage via testing.
  - [ ] 2.4 Write unit tests for rapid testing mechanism.
- [ ] 3.0 Add Pet Spawn Egg for Easy Spawning
  - [ ] 3.1 Create the spawn egg item and register it.
  - [ ] 3.2 Ensure the spawn egg spawns the pet at stage 1.
  - [ ] 3.3 Write unit tests for spawn egg functionality.
- [ ] 4.0 Implement Renderer and Model Variants for Each Stage
  - [ ] 4.1 Create or import models and textures for each growth stage.
  - [ ] 4.2 Implement renderer logic to switch models/textures based on stage.
  - [ ] 4.3 Test rendering in-game for all stages.
- [ ] 5.0 Add Basic Interactions (Follow/Stay Toggle, Click to Progress)
  - [ ] 5.1 Implement follow/stay toggle logic for the pet.
  - [ ] 5.2 Add interaction logic for feeding, watering, and treat usage (stub for now).
  - [ ] 5.3 Ensure click-to-progress does not interfere with normal interactions.
  - [ ] 5.4 Write unit tests for interaction logic.
