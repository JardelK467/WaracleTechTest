# Cake List App

An Android app that loads a list of cakes from a remote JSON file and displays them, built for the Waracle mobile coding exercise. Written in Kotlin with Jetpack Compose.

Data source: https://raw.githubusercontent.com/Waracle/mobile-coding-test-api/refs/heads/main/cakes

## Features

| Requirement | How it is met |
| --- | --- |
| Load cakes on start | The ViewModel loads in `init` |
| Remove duplicates, order by name | `GetCakesUseCase` removes duplicate titles and sorts by title, ignoring case |
| Image and title for each entry | `CakeListItem`, with Coil loading the image |
| Divider between entries | `HorizontalDivider` between items |
| Description in a popup on click | Tapping an item opens an `AlertDialog` |
| Refresh option | Pull to refresh |
| Error message when loading fails | Error screen with the message |
| Retry on error | "Try again" button on the error screen |
| Orientation changes without reloading | The ViewModel survives rotation, and the open dialog is saved |
| Animated list items | Items fall down and fade in, once per item |

## Setup

1. Open the project in Android Studio and let Gradle sync. JDK 17 is used (the same as CI).
2. Run the `app` configuration on an emulator or a physical device. An internet connection is needed to load the cakes.

## Running the tests

```
./gradlew :app:testDebugUnitTest
```

A GitHub Actions workflow (`.github/workflows`) builds the debug app and runs the unit tests on every push to `main` and on pull requests.

## Architecture

MVVM with clean architecture layers, wired by hand.

1. **data**: `CakeService` (Retrofit), `CakeDto`, `CakeRepositoryImpl`, which maps DTOs to domain models.
2. **domain**: `Cake`, the `CakeRepository` interface, and `GetCakesUseCase`, which holds the business rules.
3. **presentation**: `CakeListViewModel`, `CakeListUiState`, and the Compose UI.

Libraries: Jetpack Compose with Material 3, Retrofit with kotlinx serialization, Coil 3 for images, and coroutines with StateFlow. Tests use JUnit 4, MockWebServer and kotlinx-coroutines-test.

## Tests

1. `GetCakesUseCaseTest`: duplicates removed and cakes sorted.
2. `CakeRepositoryImplTest`: DTOs are mapped to domain models, and service failures reach the caller.
3. `CakeServiceTest`: the real Retrofit stack against MockWebServer, checking the endpoint and the JSON mapping.
4. `CakeListViewModelTest`: a successful load presents sorted unique cakes, a failed load presents an error, refresh keeps the list visible until the reload finishes, and a refresh is ignored while one is running.

The tests use hand written fakes rather than a mocking library. Each collaborator is a small interface, so fakes are short and keep the tests readable.

## Design decisions

**Dependency injection.** Constructor injection with manual wiring. `CakeServiceFactory` builds the Retrofit service and `MainActivity` assembles the repository, use case and ViewModel factory. For one screen this keeps the dependency graph easy to read. The factory takes the base URL as a parameter, which lets `CakeServiceTest` point the real Retrofit stack at a local server.

**UI state.** A sealed interface (`Loading`, `Success`, `Error`) makes impossible combinations unrepresentable and gives the UI an exhaustive `when`. Refreshing is a separate `isRefreshing` flow because a refresh happens on top of existing content, so the list stays on screen and the full screen spinner only appears on the first load.

**Error handling.** The repository lets failures propagate and the ViewModel is the single place that turns them into the error state. An earlier exception wrapper was removed because nothing consumed it.

**Duplicates and sorting.** Duplicates are detected by exact title and the first occurrence is kept. Sorting ignores case so "apple pie" does not sort after "Zebra cake".

## Future work

1. Add a Room database as the single source of truth, so the list works offline and the repository can support local add, delete and update.
2. Replace the manual wiring with Hilt or Koin once the app has more than one screen.
3. Move to a single `UiState` data class if the screen gains more interactions, so loading, refreshing and pending actions are tracked together.
4. Add separate use cases for add, delete and update.
5. Map network failures to typed domain errors if the UI needs to tell offline apart from a server error.
6. Add Compose UI tests for the list, dialog and error screens.
7. Keep list animations from replaying after rotation.
8. Externalise the base URL through build configuration if there are multiple environments.

## AI disclosure

I used AI assistance (Claude, by Anthropic) in this project, to save time and to
keep my own effort on the architecture and the parts the exercise focuses on. I
treated it as a generator whose output I review, run and take responsibility for.

AI generated:

1. The Compose UI components: the list, list items, description dialog, loading
   and error states, pull to refresh, the item animation, and the navigation bar
   inset handling.
2. `CakeListViewModelFactory`.
3. The refresh state in `CakeListViewModel` (separating `isRefreshing` from
   `uiState`), the ViewModel unit tests, and the test dispatcher rule.
4. An exception wrapper in the repository. I questioned it, found nothing used
   it, and removed it.
5. A small Go script that I run locally to execute the unit tests before each
   commit. It is a local helper and not part of the app.
6. The wording of this README and the TODO comments.

Written by me: the architecture and layering, the library choices, the
interpretation of the specification, the models and the data and domain code, and
the split between `CakeListScreenRoot` (which collects state from the ViewModel)
and the stateless `CakeListScreen`, together with the Compose previews.

I reviewed, ran and tested all the code, removed what I judged unnecessary,
and I can explain every part of it.