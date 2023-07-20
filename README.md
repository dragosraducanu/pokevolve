# Solution Brief

The app integrates the Model-View-ViewModel (MVVM) design pattern with Clean Architecture and it's split into the following modules: App, Domain, Data and Testing.

### Dependency graph
```mermaid
flowchart LR
 App("App Module
 -----
    UI
    ViewModels
    Tests")
    
    Domain("Domain Module
    -----
    Models
    Repository Interfaces
    Use Cases
    Tests")
    
    Data("Data Module
    -----
    API Interfaces
    Repository Implementation")

   Testing("Testing Module
    -----
    Common utilities for testing")
    
    App-->|depends on|Domain
    Data-->|depends on|Domain
    App-->|depends on|Testing
    Domain-->|depends on|Testing
    
    
     classDef purple fill:#9747FF,stroke:#00,stroke-width:0px;
     classDef orange fill:#F24822,stroke:#00,stroke-width:0px;

     class App,Data,Domain purple
     class Testing orange
```

**PokéVolve** uses Hilt for dependency injection and Jetpack Compose for the UI.
For testing, the app uses Mockito to mock objects.

### Sequence diagram for loading the list of Pokémon species:
```mermaid
sequenceDiagram
  MainScreen->>MainViewModel: loadPokemonSpecies()
  MainViewModel->>GetPokemonSpeciesListUseCase: invoke()
  GetPokemonSpeciesListUseCase->>PokemonSpeciesRepository: getPokemonSpecies()
  PokemonSpeciesRepository->>PokemonSpeciesApi: getPokemonSpecies()
  PokemonSpeciesApi->>PokemonSpeciesRepository: Single<NamedAPIResourceList>
  PokemonSpeciesRepository->>GetPokemonSpeciesListUseCase: Single<NamedAPIResourceList>
  GetPokemonSpeciesListUseCase->>MainViewModel: Single<NamedAPIResourceList>
  MainViewModel->>MainScreen: ScreenState(pokemonSpeciesList)
```

### Application demo
https://github.com/dragosraducanu/pokevolve/assets/3254786/a13e3478-3803-4da1-b9c4-d09996e6d34d


### Other activities that would be needed for a production app, in no particular order (and are not part of this assignment):
- [ ] different build types and product flavors
- [ ] different app environments
- [ ] additional logging for errors, handled exceptions etc. (currently only HttpLoggingInterceptor is being used)
- [ ] crash reporting (Firebase Crashlytics or similar)
- [ ] smoother screen transitions
- [ ] network caching
- [ ] advanced error handling
- [ ] offline support
- [ ] CI/CD pipelines
