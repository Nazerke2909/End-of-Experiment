# End of Experiment

**End of Experiment** is a 2D pixel-art RPG built with [libGDX](https://libgdx.com/). The player progresses through a 3-level campaign using 3 unique heroes — **Sakura (Rabbit)**, **Gojo (Horse)**, and **Yuki (Deer)**. The core gameplay consists of a **Hub** (upgrade heroes, select levels), **Combat** (turn-based card battles), and **Level Transition** (walk to the portal to advance).

---

## Table of Contents

- [Project Overview](#project-overview)
- [Team](#team)
- [Controls](#controls)
- [How to Run](#how-to-run)
  - [Prerequisites](#prerequisites)
  - [Run with Gradle (recommended)](#run-with-gradle-recommended)
  - [Run as a JAR](#run-as-a-jar)
  - [Run in IntelliJ IDEA](#run-in-intellij-idea)
  - [Platform-specific JARs](#platform-specific-jars)
- [Game Phases](#game-phases)
  - [Hub Phase](#hub-phase)
  - [Combat Phase](#combat-phase)
  - [Level Transition Phase](#level-transition-phase)
- [Characters (Party)](#characters-party)
- [Enemies](#enemies)
- [Levels](#levels)
- [Win / Lose Screens](#win--lose-screens)
- [Currency & Upgrades](#currency--upgrades)
- [Known Issues](#known-issues)
- [Built With](#built-with)

---

## Project Overview

| Aspect           | Details |
|------------------|---------|
| **Genre**        | 2D Pixel-Art RPG |
| **Engine**       | libGDX (Java 11+) |
| **Platform**     | Desktop (Windows, macOS, Linux) |
| **Story**        | 3-level campaign (Hub → Combat → Level Transition) |
| **Currency**     | Pills (earned from battles, used for upgrades) |
| **Max Hero Level** | 3 |
| **Card Slots**   | 4 (per turn) |
| **Ultimate Trigger** | After 3 attacks per hero |

---

## Team

| Role               | Name                  |
|--------------------|-----------------------|
| Project Lead       | Kudaibergen Nazerke   |
| Lead Programmer    | Kudaibergen Nazerke   |
| Programmer         | Tolegenova Moldir     |
| Designer / Artist  | Tolegenova Moldir     |

### Tools Used
- **GitHub** — version control & collaboration
- **Trello** — task management
- **LibGDX** — game framework

---

## Controls

### Start Screen
| Action      | Input                           |
|-------------|---------------------------------|
| Start Game  | Click the **Play** button       |

### Hub (Overworld)
| Action                | Input                           |
|-----------------------|---------------------------------|
| View Hero Profile     | **Left-click** on hero avatar (Rabbit / Horse / Deer) |
| Upgrade a Hero        | **Left-click** the upgrade button in the hero profile |
| Select a Level        | **Left-click** a level flask    |
| Go Back               | **Left-click** the **BACK** button (top-left) |

### Combat (Turn-based Card Battle)
| Action                         | Input                                  |
|--------------------------------|----------------------------------------|
| Select / Drag a Card           | **Left-click** on a card slot          |
| Play a Card on an Enemy        | **Drag** card onto an enemy            |
| View Card/Enemy Info           | **Mouse hover** over cards or enemies  |
| Menu / Return to Hub           | **Left-click** the **MENU** button     |
| Continue after Victory         | **Left-click** the **CONTINUE** button |

### Level Transition (Walking)
| Action              | Input                        |
|---------------------|------------------------------|
| Move Up             | **W** or **Arrow Up**        |
| Move Down           | **S** or **Arrow Down**      |
| Move Left           | **A** or **Arrow Left**      |
| Move Right          | **D** or **Arrow Right**     |
| View Hero Profile   | **Left-click** on hero avatar|
| Advance to Next Lvl | Walk the Deer into the **Portal** |

### Overlays (Victory / Defeat)
| Action           | Input                       |
|------------------|-----------------------------|
| Menu (return)    | **Left-click** **MENU**     |
| Continue         | **Left-click** **CONTINUE** (only on non-final levels) |
| Retry on Defeat  | **Left-click** **RETRY**    |

---

## How to Run

### Prerequisites
- **Java 11** or higher installed ([Adoptium Temurin](https://adoptium.net/) recommended)
- (Optional) **IntelliJ IDEA** or another Java IDE

### Run with Gradle (recommended)

Clone the project, open a terminal in the project root, and run:

```shell
./gradlew lwjgl3:run       # macOS / Linux
gradlew.bat lwjgl3:run     # Windows
```

The game window will open at 640 x 480 pixels.

### Run as a JAR

Build a runnable JAR:

```shell
./gradlew lwjgl3:jar       # macOS / Linux
gradlew.bat lwjgl3:jar     # Windows
```

The JAR will be placed at:

```
lwjgl3/build/libs/End of Experiment-1.0.0.jar
```

Run it with:

```shell
java -jar "lwjgl3/build/libs/End of Experiment-1.0.0.jar"
```

> **Note:** Always run the JAR from the project root directory so the assets folder is found. Alternatively, copy the JAR into the `assets/` folder and run from there.

### Run in IntelliJ IDEA

1. Open the project folder in IntelliJ (the `build.gradle` file will be detected).
2. Let Gradle sync finish.
3. Navigate to `lwjgl3/src/main/java/com/mygame/lwjgl3/Lwjgl3Launcher.java`.
4. Right-click → **Run 'Lwjgl3Launcher.main()'**.

If you encounter a `NoClassDefFoundError`, run the Gradle `lwjgl3:run` task once from the terminal first, or re-sync Gradle in IntelliJ.

### Platform-specific JARs

Platform-optimized JARs (smaller file size) can be built with:

```shell
./gradlew lwjgl3:jarMac     # macOS only
./gradlew lwjgl3:jarLinux   # Linux only
./gradlew lwjgl3:jarWin     # Windows only
```

---

## Game Phases

### Hub Phase
The central hub where you:
- View hero avatars (Rabbit, Horse, Deer)
- Click on a hero to open their **Profile** screen
- Upgrade hero stats by spending **Pills**
- Select a level flask to start combat (only unlocked levels are clickable)
- Return to the title screen with the **BACK** button

### Combat Phase
Turn-based card battles:
- Each hero has **4 card slots** per turn with randomly assigned attack cards
- **Left-click** a card to select it, then **drag** onto a target enemy
- After **3 attacks**, that hero's **Ultimate card** becomes available (replaces a regular slot)
- Enemies attack in a fixed order: **Deer → Rabbit → Horse** with a **2-second delay** between attacks
- **Victory** when all enemies are defeated; **Defeat** if the party's HP reaches zero

### Level Transition Phase
After winning a battle:
- Control **Yuki (the Deer)** using **WASD / Arrow Keys**
- Walk toward the **Portal** to advance to the next level
- You can still view hero profiles and upgrade them during this phase
- A **BACK** button returns you to the Hub

---

## Characters (Party)

| Hero (Alias) | Real Name | Role | Attack Ability | Ultimate Ability |
|--------------|-----------|------|----------------|------------------|
| **Rabbit**   | Sakura    | AOE / Healer | Acid bomb — deals damage to **all enemies** | Deals 50% damage to all enemies and heals all allies by 50% of max HP |
| **Horse**    | Gojo      | Tank / Support | Single-target attack + applies **Taunt** for 1 turn | Deals damage to one enemy and grants **Shield** to allies |
| **Deer**     | Yuki      | DPS | Single-target attack — powerful damage | Deals **double damage** to one enemy |

Each hero has: **HP**, **Damage**, **Ultimate Damage**, and a **Level** (1–3).

---

## Enemies

| Enemy  | Description |
|--------|-------------|
| **Pig**   | Basic enemy, moderate HP and damage |
| **Rat**   | Quick enemy with moderate stats |
| **Croco** | High-HP enemy, high damage output |

- Enemies attack in a **fixed order**: Deer → Rabbit → Horse
- There is a **2-second delay** between enemy attacks

---

## Levels

| Level | Reward (Pills) | Enemies |
|-------|----------------|---------|
| 1     | 150            | Basic set |
| 2     | 360            | Harder set |
| 3     | 1000           | Final set (last level) |

**Flow:** Hub → Select Level → Combat → Victory/Defeat Overlay → Level Transition (walk to portal) → Next Level or Hub

---

## Win / Lose Screens

### Victory Overlay
- Shows **"VICTORY!"**
- Displays the number of **Pills collected**
- **MENU** button — returns to Hub
- **CONTINUE** button — advances to the next level (disabled on Level 3)

### Defeat Overlay
- Shows **"DEFEAT"**
- **MENU** button — returns to Hub
- **RETRY** button — restarts the level

> Pills are awarded immediately on victory.

---

## Currency & Upgrades

- **Currency:** Pills
- **Earned from:** Level rewards (150 / 360 / 1000 pills per victory)
- **Upgrade cost:**
  - Level 1 → 2: **50 pills**
  - Level 2 → 3: **120 pills**
- **Max hero level:** 3
- Upgrading increases the hero's **HP**, **Damage**, and **Ultimate Damage**

---

## Known Issues

1. **ESC / Pause not implemented** — The pause menu is listed in the design but not yet functional. Closing the window is the only way to pause mid-game.
2. **Window size** — The default window is 640 x 480 with a StretchViewport, meaning the canvas (1253 x 752) is scaled. On very small screens the UI may appear cramped.
3. **Music volume** — Background music plays at 50% volume by default. There is no in-game volume slider.
4. **Hero Profile in Transition Screen** — While viewing a hero profile during the Level Transition phase, tapping outside the upgrade button closes the profile, but the click is also registered by the walking input — this may cause an unintended step after closing.
5. **No confirmation dialog** — Spending pills on upgrades happens immediately with no confirmation prompt.
6. **Defeat after Level Transition** — On defeat, the player is returned to the Hub but the hero levels/state from that attempt are lost; you must re-select the level.
7. **Button hitboxes** — Some button hitboxes use hardcoded pixel coordinates matched to a fixed canvas size. If the viewport is stretched to an extreme aspect ratio, hit detection may be slightly off.
8. **Ultimate card slot persistence** — Ultimate cards persist in their slots until used, which can result in a hero having both an Ultimate card and a regular attack card on the same turn if multiple slots were available.

---

## Built With

- [libGDX](https://libgdx.com/) — cross-platform game development framework
- [LWJGL3](https://www.lwjgl.org/) — desktop backend
- [Gradle](https://gradle.org/) — build system (generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff))
- **Java 11+** — language runtime

---