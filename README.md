# Mancala Game

<div align="center">

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)

Object-oriented desktop implementation of the Mancala board game built with Java Swing, featuring event-driven architecture with MVC separation of concerns, Strategy pattern for extensible board rendering, and stateful game management for move validation and rollback functionality.

[Features](#features) • [Getting Started](#getting-started) • [How to Play](#how-to-play) • [Architecture](#architecture) • [SystemDesign](#diagrams)

</div>

---

## Overview
Mancala is a two-player turn-based strategy game which features a board with two rows of six pits each, plus two Mancala stores (one for each player). Players begin by selecting the initial number of stones per pit (3 or 4), which are then distributed across all pits. Players take turns selecting pits on their side, distributing stones counter-clockwise according to game rules, with special conditions for landing in their own Mancala (free turn) or capturing opponent stones from opposite pits. This implementation provides an intuitive graphical user interface with customizable board styles, undo functionality.

### Key Highlights

- **Multiple Board Styles**: Choose from different visual themes using the Strategy pattern
- **Smart Undo System**: Undo up to 3 moves per turn with intelligent state management
- **Clean Architecture**: Built with MVC and Strategy design patterns for maintainability
- **Two-Player Local Gameplay**: Classic head-to-head gameplay on a single device
- **Configurable Setup**: Start with 3 or 4 stones per pit

---

## Features

### Gameplay Features
- **Full Mancala Rules Implementation**
  - Counter-clockwise stone distribution
  - Capture opponent's stones
  - Free turn on landing in your Mancala
  - Automatic end-game stone collection

### User Interface
- **Interactive Board**: Click-to-select pit interface
- **Visual Feedback**: Real-time stone count updates
- **Undo Functionality**: Revert moves with constraints (max 3 per turn, no consecutive undos)
- **Style Selection**: Choose your preferred board aesthetic before gameplay

### Technical Features
- **Design Patterns**: MVC architecture with Strategy pattern for style flexibility
- **Extensible Design**: Easy to add new board styles or game variants
- **Event-Driven Architecture**: Responsive UI with proper separation of concerns

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or command line tools

### Installation

1. **Clone the repository**
```bash
   git clone https://github.com/yourusername/mancala-game.git
   cd mancala-game
```

2. **Compile the project**
```bash
   javac MancalaTest.java
```

3. **Run the game**
```bash
   java MancalaTest
```

### Quick Start

1. Launch the application
2. Select your preferred board style (2 options available)
3. Enter the number of stones per pit (3 or 4)
4. Click on any pit on your side to begin playing!

---

## How to Play

### Game Rules

**Objective**: Collect more stones in your Mancala (store) than your opponent.

**Setup**: 
- Board has 12 pits (6 per player) and 2 Mancalas (stores)
- Each pit starts with 3-4 stones (player's choice)

**Gameplay**:
1. **Pick a Pit**: Click any pit on your side containing stones
2. **Distribute Stones**: Stones are distributed counter-clockwise, one per pit
3. **Your Mancala**: When passing your Mancala, drop a stone in it
4. **Skip Opponent's Mancala**: Never drop stones in opponent's Mancala

**User Interface**:
- Visual representation of the Mancala board with labeled pits (A1-A6, B1-B6) and Mancalas (A, B)
- Graphical display of individual stones (not just numbers)
- Style selection screen with at least two distinct board styles
- Click-based pit selection for gameplay
- Undo functionality with constraints (max 3 undos per turn, no consecutive undos without a move)

**Game Rules**:
- Counter-clockwise stone distribution
- Skipping opponent's Mancala
- Free turn when landing in own Mancala
- Stone capture from opposite pits
- End-game detection and winner determination


**Special Rules**:
- **Free Turn**: Landing your last stone in your Mancala grants another turn
- **Capture**: Landing your last stone in an empty pit on your side captures that stone plus all stones in the opposite pit
- **Game End**: When one side is empty, the other player captures all remaining stones

**Winner**: Player with the most stones in their Mancala wins!

### Undo Feature

- Press the **Undo** button to revert your last move
- Maximum 3 undos per turn
- Cannot undo consecutively (must make a move between undos)
- Undo is unavailable after opponent's turn begins

---

## Architecture

### Design Patterns

#### **Model-View-Controller (MVC)**
- Separates game logic, visual presentation, and user input handling

```
Model      → Game state, rules, and business logic
View       → Graphical board representation
Controller → User input handling and model updates
```

#### **Strategy Pattern**
- Enables pluggable board styles with different visual appearances (shapes, colors, layouts)
  
```
BoardStyle Interface → Defines rendering contract
ConcreteStyle1      → Implementation for style variant 1
ConcreteStyle2      → Implementation for style variant 2
```

### Project Structure
```
mancala-game/
├── src/
│   ├── model/
│   │   ├── MancalaModel.java       # Game state and logic
│   │   └── GameState.java          # State management
│   ├── view/
│   │   ├── MancalaView.java        # Main board view
│   │   └── BoardStyle.java         # Style interface
│   ├── controller/
│   │   └── MancalaController.java  # Input handling
│   └── MancalaTest.java            # Entry point
├── docs/
│   └── mancala.jpg                 # Board reference image
└── README.md
```

### Key Components

- **MancalaModel**: Manages game state, validates moves, applies rules
- **MancalaView**: Renders the board, handles style switching
- **BoardStyle**: Interface enabling pluggable visual themes
- **UndoManager**: Tracks move history with constraints

---

## Board Styles

The application includes two distinct visual styles:

1. **Classic Style**: Traditional circular pits with earth tones
2. **Modern Style**: Sleek rectangular design with vibrant colors


---


<div align="center">


Made with Java

</div>
