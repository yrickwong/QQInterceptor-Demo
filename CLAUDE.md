# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android application demo that displays a list of installed applications with their signature information. The app demonstrates modern Android architecture patterns using MVI (Model-View-Intent) with Airbnb's Mavericks framework and Epoxy for RecyclerView management.

**Important**: This is a security research and defensive analysis tool. It should only be used for legitimate security testing and educational purposes.

## Architecture

This project demonstrates three different Android architecture approaches for educational comparison:

### 1. Traditional Architecture (MainActivity)
- Basic Activity + Adapter pattern
- Direct UI manipulation
- Legacy approach for comparison

### 2. MVI Pattern with Mavericks (MainActivityMVI)
- **State Management**: Uses `AppListState` to manage UI state immutably  
- **ViewModel**: `AppListViewModel` extends `MavericksViewModel` for reactive state updates
- **UI Layer**: Fragment-based UI with ViewBinding and DataBinding integration

### 3. Compose + Mavericks Architecture (MainActivityCompose) ⭐ NEW!
- **Declarative UI**: Compose functions describe UI state rather than imperatively modifying it
- **State Management**: Same `AppListViewModel` and `AppListState`, showcasing framework flexibility
- **Reactive Integration**: `collectAsState()` automatically subscribes to state changes
- **Component-based**: Modular, reusable `@Composable` functions
- **Material Design 3**: Modern design system with dynamic theming

### Key Libraries
- **Mavericks (3.0.9)**: MVI architecture framework for predictable state management
- **Mavericks-Compose (3.0.9)**: Compose integration for Mavericks (`collectAsState()`)
- **Jetpack Compose (2024.04.01)**: Modern declarative UI toolkit
- **Material3**: Latest Material Design components for Compose
- **Coil-Compose (2.4.0)**: Image loading library for Compose
- **Epoxy (5.1.4)**: Declarative RecyclerView management with KSP annotation processing
- **ViewBinding + DataBinding**: Type-safe view references and data binding
- **KSP**: Kotlin Symbol Processing for faster compilation (replaced kapt)

## Build System

This project uses Gradle with Kotlin DSL and centralized dependency management via `gradle/libs.versions.toml`.

### Common Commands

**Note**: This project requires Java 17. If you encounter "Unable to locate a Java Runtime" errors, use the `gradlew-java.sh` script instead of `./gradlew`.

```bash
# Using gradlew-java.sh (recommended):
./gradlew-java.sh build
./gradlew-java.sh assembleDebug
./gradlew-java.sh lintDebug

# Or set environment variables manually:
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
./gradlew build
```

**Standard Gradle Commands:**

```bash
# Build the project
./gradlew-java.sh build

# Build debug APK
./gradlew-java.sh assembleDebug

# Build release APK  
./gradlew-java.sh assembleRelease

# Run unit tests
./gradlew-java.sh test

# Run instrumentation tests
./gradlew-java.sh connectedAndroidTest

# Run lint checks
./gradlew-java.sh lintDebug

# Clean build
./gradlew-java.sh clean

# Install debug APK to connected device
./gradlew-java.sh installDebug
```

**Environment Setup:**

The Java environment variables have been added to `~/.zshrc`:
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
```

For new terminal sessions, these will be loaded automatically. For the current session, use `source ~/.zshrc` or use the `gradlew-java.sh` script.

## Project Structure

```
com.example.qqinterceptor_demo/
├── data/                           # Data layer
│   ├── AppInfo.kt                 # Data model for app information
│   └── AppListManager.kt          # App list data management with signature reading
├── presentation/                   # Presentation layer
│   ├── ui/
│   │   ├── activity/
│   │   │   ├── LauncherActivity.kt        # Entry point activity with version selection
│   │   │   ├── MainActivity.kt            # Original activity (legacy)
│   │   │   ├── MainActivityMVI.kt         # MVI-based main activity
│   │   │   └── MainActivityCompose.kt     # Compose + MVI main activity ⭐
│   │   ├── fragment/
│   │   │   └── AppListFragment.kt     # Main UI fragment with search functionality
│   │   ├── adapter/               # RecyclerView adapters (legacy)
│   │   ├── epoxy/                 # Epoxy components
│   │   │   ├── AppListController.kt   # Epoxy controller for app list
│   │   │   └── items/             # Epoxy model views
│   │   │       ├── AppItemView.kt     # Individual app item view
│   │   │       ├── EmptyItemView.kt   # Empty state view
│   │   │       └── LoadingItemView.kt # Loading state view
│   │   └── compose/               # Compose components ⭐ NEW!
│   │       ├── AppListScreen.kt       # Main Compose screen
│   │       ├── components/            # Reusable Composables
│   │       │   ├── AppItem.kt         # App item Composable
│   │       │   ├── AppDetailDialog.kt # Detail dialog Composable
│   │       │   └── EmptyState.kt      # Empty state Composable
│   │       └── theme/                 # Material3 theming
│   │           ├── Color.kt           # Color definitions
│   │           ├── Theme.kt           # Main theme setup
│   │           └── Type.kt            # Typography definitions
│   ├── mvi/                       # MVI components
│   │   ├── AppListState.kt        # Immutable state for app list
│   │   └── AppListViewModel.kt    # MVI ViewModel with search debouncing
│   └── base/                      # Base classes
│       └── BaseMavericksFragment.kt   # Base fragment for Mavericks integration
└── utils/                         # Utility classes
    ├── viewbinding/               # ViewBinding utilities
    │   ├── FragmentViewBindingDelegate.kt
    │   └── ViewBindingExtensions.kt
    └── AppSigning.kt              # Signature extraction utilities
```

## Key Features

### App List Management
- **Dual Mode Loading**: All apps vs user-installed apps only
- **Real-time Search**: Debounced search with 300ms delay for optimal performance
- **App Information Display**: Name, package, version, signatures, install/update times
- **Memory Optimization**: Safe icon loading to prevent OutOfMemoryError

### Search Functionality
- **Debounced Input**: Prevents excessive filtering during rapid typing
- **Search by Package/Name**: Supports both package name and display name filtering
- **Clear Search**: X button integration for clearing search queries
- **Immediate UI Feedback**: Search query updates immediately while filtering is debounced

### Signature Analysis
- **Multiple Hash Support**: MD5, SHA-1, SHA-256 signature extraction
- **Modern Android Support**: Handles both legacy and current signing schemes
- **Memory Safety**: Protected against icon loading crashes
- **Detailed Information**: Install time, update time, version info

## Android Configuration

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34
- **Uses Jetpack Compose**: Yes, with Material3 theming
- **Uses Traditional Views**: Yes, with ViewBinding/DataBinding
- **Build Tools**: Android Gradle Plugin 8.5.1, Kotlin 1.9.0, KSP 1.9.0-1.0.13

## Development Notes

### MVI Best Practices
- Always use `setState { copy(...) }` for state updates
- Use `withState { state -> }` when reading current state before updating
- Avoid nesting `withState` inside `setState` - this is an anti-pattern
- Leverage `execute` for true async operations, not for simple filtering

### Search Implementation
- Manual Job management is acceptable for non-async filtering operations
- `execute` is designed for network/database operations with loading states
- Debouncing prevents excessive computations during user input
- Immediate query state updates provide better user experience

### Performance Optimizations
- KSP instead of kapt for 2-3x faster compilation
- Memory-safe icon loading with fallback to default icons
- Efficient filtering with early returns for empty queries
- ViewBinding delegate pattern reduces boilerplate

### Testing Strategy
- The project includes both unit test and instrumentation test configurations
- Use the launcher activity to switch between all three architecture implementations
- Test both "All Apps" and "User Apps" loading modes across all versions
- Verify search functionality with various query patterns
- Compare performance and user experience between traditional Views and Compose

### Compose Development Guidelines ⭐ NEW!
- **State Management**: Reuse existing `AppListViewModel` and `AppListState` - no changes needed
- **Declarative Patterns**: Use `collectAsState()` for Mavericks integration
- **Component Structure**: Build UI with small, focused `@Composable` functions
- **Material Design 3**: Utilize modern design tokens and dynamic theming
- **Performance**: Use stable keys in `LazyColumn` and avoid unnecessary recomposition

### Architecture Comparison
- **Traditional**: Direct UI manipulation, XML layouts, ViewBinding
- **MVI + Fragment**: State-driven UI, Fragment lifecycle, Epoxy for lists  
- **MVI + Compose**: Declarative UI, automatic recomposition, `@Composable` functions

For detailed learning materials, see `COMPOSE_TUTORIAL.md` for comprehensive Compose + Mavericks integration guide.