# 50ZO Card Game
> **A fully playable turn-based card game built with JavaFX and pure Java — featuring human-versus-machine gameplay, custom checked and unchecked exceptions, background thread coordination, synchronized turn handling, automated opponents, deck recycling, and JUnit class tests, all organized through an MVC architecture.**

---

[![50z0 game interface](https://github.com/user-attachments/assets/3f9daa4b-521e-4e7b-95f8-5beb239f7bb8)](#)
## Purpose

Turn-based card games provide a practical environment for applying object-oriented programming, concurrency, exception handling, automated testing, and graphical interface design. This project implements 50ZO, a card game in which every player must contribute to a shared score without causing it to exceed 50

---

## Project Structure

```text
50z0/
│
├── src/main/java/com/examplez/demo/
│   ├── Launcher.java                          # Standard Java entry point
│   ├── GameLauncher.java                      # JavaFX Application subclass
│   ├── module-info.java                       # Java module configuration
│   │
│   ├── Controller/
│   │   ├── MenuController.java                # Player-count selection and navigation
│   │   ├── PlayController.java                # Turn loop, UI updates, and card interaction
│   │   └── FinalController.java               # Winner screen and final navigation
│   │
│   ├── Exceptions/
│   │   ├── InvalidCardException.java          # Checked exception for invalid plays
│   │   └── PlayerNotFoundException.java       # Runtime exception for missing players
│   │
│   └── Model/
│       ├── Card.java                          # Card value and image identifier
│       ├── Desk.java                          # Draw-deck management
│       ├── DiscardPile.java                   # Played-card collection
│       ├── Game.java                          # Main rules and game state
│       ├── Player.java                        # Base player abstraction
│       ├── PlayerHuman.java                   # Human turn state and validation
│       └── PlayerMachine.java                 # Automatic card selection
│
├── src/main/resources/com/examplez/demo/
│   ├── images/
│   │   ├── Cards/                             # Card image resources
│   │   ├── game-image.png                     # Game-table background
│   │   └── logo-50zo.png                      # Project logo
│   │
│   └── view/
│       ├── menu-view.fxml                     # Player-selection screen
│       ├── game-view.fxml                     # Main game board
│       ├── final-view.fxml                    # Winner screen
│       └── theme.css                          # Shared JavaFX styling
│
├── src/test/java/com/examplez/demo/Model/
│   ├── GameTest.java                          # Game initialization and rule tests
│   ├── PlayerTest.java                        # Player hand and turn tests
│   └── DiscardPileTest.java                   # Discard-pile behavior tests
│
├── JavaDoc/                                   # Generated project documentation
└── pom.xml                                    # Maven build and dependencies
```

---

## Architecture — MVC Design

### What It Does

The project follows a Model–View–Controller structure connected through JavaFX FXML:

1. **Displays the player-selection menu** through `menu-view.fxml`, allowing the user to choose a game with two, three, or four total participants.
2. **Loads the main game view** and passes the selected player count from `MenuController` to `PlayController`.
3. **Builds the card set** by reading the filenames stored in the card-resource directory and extracting each card's identifier and numeric value.
4. **Creates the participants** by assigning turn `0` to one `PlayerHuman` and the remaining turn identifiers to `PlayerMachine` objects.
5. **Deals four cards to every player**, selects an initial card for the discard pile, and initializes the shared score.
6. **Starts a background turn task** that controls human and machine turns without blocking the JavaFX interface.
7. **Waits for human interaction** through synchronized `wait()` and `notifyAll()` coordination between the controller thread and the game model.
8. **Processes machine turns automatically** by selecting the first valid card and applying a randomized delay before playing and drawing.
9. **Eliminates players without valid cards**, returns their remaining cards to the deck, and hides their cards in the interface.
10. **Loads the final view** when only one participant remains and displays the winning human or machine player.

### Design Decisions

#### Turn Management — Background Task and Synchronization

| Step | Description |
|---|---|
| 1 | `PlayController.createThreadTurnTask()` creates a JavaFX `Task<Void>` |
| 2 | The task is assigned to a separate daemon `Thread` |
| 3 | The task iterates through the active turn identifiers stored by `Game` |
| 4 | UI changes are delegated to the JavaFX Application Thread through `Platform.runLater()` |
| 5 | During the human turn, `Game.waitUntilRoundEnds()` suspends the background thread |
| 6 | Clicking a valid card calls `Game.endRound()`, changes the human turn state, and invokes `notifyAll()` |
| 7 | Machine players wait for a randomized period, play automatically, and draw a replacement card |
| 8 | The loop continues until only one active player remains |

This design keeps `Thread.sleep()` and human-turn waiting outside the JavaFX Application Thread, preventing the graphical interface from becoming unresponsive.

#### Card Validation — Maximum Score Rule

| Card Type | Validation |
|---|---|
| Positive card | Valid only when its value plus the current score does not exceed 50 |
| Negative card | Reduces the accumulated score and normally remains playable |
| Zero card | Leaves the accumulated score unchanged |
| Ace | Can contribute either 1 or 10 when the selected value remains within the limit |
| Invalid card | Produces an `InvalidCardException` and leaves the human turn active |

The human player chooses an Ace value through a `ChoiceDialog` when both 1 and 10 are valid. Machine players automatically choose 10 when possible and otherwise use 1.

#### Machine Selection — First Valid Card

| Step | Description |
|---|---|
| 1 | Traverse the machine player's hand in its current order |
| 2 | Check whether an Ace can be used as 1 or 10 |
| 3 | Otherwise, verify whether the card value keeps the score at or below 50 |
| 4 | Return the first valid card found |
| 5 | Return `null` when the machine has no playable card |

#### Exception Handling

| Exception | Type | Purpose |
|---|---|---|
| `InvalidCardException` | Checked exception | Represents a play that would cause the accumulated score to exceed the permitted limit |
| `PlayerNotFoundException` | Unchecked exception | Represents an unexpected request for a human or machine player that is no longer present |
| `IllegalArgumentException` | Standard unchecked exception | Rejects invalid constructor data such as fewer than two players, a null hand, a null card identifier, or a card value above 10 |

`InvalidCardException` is caught by `PlayController`, which displays the exception message in the game interface and allows the human player to select another card.

`PlayerNotFoundException` extends `RuntimeException` because a missing player represents an inconsistent internal state rather than a normal user mistake.

#### Class Testing — JUnit Test Suite

| Test Class | Tests | Main Responsibilities Verified |
|---|---:|---|
| `GameTest` | 8 | Player creation, human-player lookup, initial hand size, valid and invalid cards, turn lists, elimination, and initial discard |
| `PlayerTest` | 5 | Initial hand, turn identifier, drawing, discarding, and removal of nonexistent cards |
| `DiscardPileTest` | 4 | Initialization, last-card access, card insertion, and retrieval of all cards except the active card |

The test classes use `@BeforeEach` to create isolated model objects before every test and `@DisplayName` to document the expected behavior of each case.

### Pipeline

```text
User selects 2, 3, or 4 players
    │
    └──► MenuController.changeView()
              │
              └──► PlayController.initialize(numberOfPlayers)
                        │
                        └──► startGame()
                                  │
                                  ├──► Game.startGame()
                                  │       ├──► setCards() → Read card resources
                                  │       ├──► drawRandomCard() → Initial discard
                                  │       ├──► setPlayers() → Human + machine players
                                  │       └──► Desk(setCards) → Remaining draw deck
                                  │
                                  ├──► showHandCardPlayer()
                                  │       ├──► Load each card image
                                  │       └──► Attach mouse-click listeners
                                  │
                                  ├──► showCardPile()
                                  │       ├──► Display active card
                                  │       └──► Display accumulated score
                                  │
                                  └──► createThreadTurnTask()
                                            │
                                            └──► Background Task<Void>
                                                      │
                                                      ├──► Human turn
                                                      │       ├──► Set turn state to true
                                                      │       ├──► Check playable cards
                                                      │       ├──► waitUntilRoundEnds()
                                                      │       └──► Card click
                                                      │               ├──► Validate card
                                                      │               ├──► Update score and discard
                                                      │               ├──► Draw replacement
                                                      │               └──► endRound() → notifyAll()
                                                      │
                                                      ├──► Machine turn
                                                      │       ├──► Check playable cards
                                                      │       ├──► Randomized delay
                                                      │       ├──► Select first valid card
                                                      │       ├──► Update score and discard
                                                      │       └──► Draw replacement
                                                      │
                                                      ├──► No valid card
                                                      │       ├──► Eliminate player
                                                      │       ├──► Return hand to deck
                                                      │       └──► Hide cards and update label
                                                      │
                                                      └──► One player remains
                                                              └──► FinalController.showWinner()
```

---

## Setup

### Prerequisites

- Java **17 or higher**
- Maven **3.8+**

### 1. Clone the repository

```bash
git clone https://github.com/LuisFelipeVelasco/50ZO.git
cd 50ZO
```

### 2. Run the game

```bash
mvn clean javafx:run
```

The application opens with an **800×600 px** player-selection menu. The main game board uses a fixed **1200×800 px** window.

### 3. Run tests

```bash
mvn test
```

---

## Learnings

**Checked and Unchecked Exceptions**
- `InvalidCardException` extends `Exception` because an invalid human move is expected and recoverable.
- `PlayerNotFoundException` extends `RuntimeException` because requesting a missing player represents an unexpected internal state.
- Catching `InvalidCardException` in the controller prevents rule-validation details from being duplicated in the UI layer.
- Constructor-level `IllegalArgumentException` checks prevent invalid cards, players, and games from entering the model.

**JavaFX Task and Background Threads**
- Running the turn loop directly on the JavaFX Application Thread would freeze the interface during sleeps and while waiting for human input.
- Wrapping the loop in `Task<Void>` and executing it on a daemon `Thread` keeps the UI responsive.
- A daemon thread does not prevent the Java Virtual Machine from closing after the JavaFX application exits.
- `Platform.runLater()` transfers interface updates from the background task to the JavaFX Application Thread.

**Wait and Notify Synchronization**
- `waitUntilRoundEnds()` uses `wait()` inside a `while` condition so the background task remains suspended until the human turn state actually changes.
- `endRound()` changes the shared condition before calling `notifyAll()`.
- Both methods are synchronized so they use the same `Game` object monitor.
- This avoids constant polling while the application waits for the player to click a card.

**Class-Level Unit Testing**
- The test suite separates model responsibilities into `GameTest`, `PlayerTest`, and `DiscardPileTest`.
- `@BeforeEach` creates fresh objects for every test, preventing one test from affecting another.
- `assertEquals`, `assertTrue`, `assertFalse`, `assertNotNull`, and `assertSame` verify both state changes and object identity.
- Testing the model independently from the controller avoids dependencies on JavaFX window initialization.

**Resource-Driven Card Creation**
- Card objects are generated from image filenames rather than being declared individually in the source code.
- Each filename contains the card identifier and numeric value used to create its corresponding `Card`.
- The same identifier and value are later used by the controller to locate the correct card image.

**Ace Representation**
- An Ace is stored with the internal value `-1`, indicating that its final contribution has not yet been selected.
- The model calculates whether 1, 10, or both values remain possible.
- This separates the card's identity from the value selected for a particular play.
