# 📱 Pokedex — Lab 4: RecyclerView, Sensors, Fragments y Navigation Component

> Aplicación móvil desarrollada en Android (Java) que consume la PokéAPI para consultar tipos y Pokémon, como parte del laboratorio del curso Servicios y Aplicaciones para IoT [1TEL05] — PUCP.

## 📋 Tabla de Contenidos

- [Descripción](#-descripción-del-proyecto)
- [Tecnologías](#-tecnologías-usadas)
- [Estructura](#-estructura-principal)
- [Flujo Funcional](#-flujo-funcional-implementado)
- [Sensor](#-regla-del-sensor-acelerómetro)
- [Estado](#-estado-frente-a-la-consigna)
- [Ejecución](#-cómo-ejecutar)

---

## 📝 Descripción del Proyecto

Una aplicación Android que permite explorar el universo Pokémon mediante el consumo de una API REST, permitiendo al usuario:

✅ Visualizar los diferentes tipos de Pokémon en un RecyclerView  
✅ Seleccionar un tipo y navegar al listado de sus Pokémon  
✅ Consultar el detalle (id, name, base_experience, height, weight) de cada Pokémon mediante una doble consulta a la API  
✅ Detectar movimientos bruscos con el acelerómetro para volver automáticamente a la lista de tipos

## 💻 Tecnologías Usadas

| Tecnología | Versión | Uso |
|-----------|---------|-----|
| Java | 17 | Lenguaje base (100% Java, sin Kotlin) |
| Android SDK | API 34 (min) / 37 (target) | Plataforma móvil |
| Retrofit | 2.9.0 | Cliente HTTP para consumo de API |
| Gson | 2.9.0 (converter) | Deserialización de respuestas JSON |
| Navigation Component | 2.9.3 | Navegación entre fragments con argumentos |
| ViewBinding | — | Binding de vistas en fragments y adapters |
| PokeAPI | — | API REST pública (https://pokeapi.co) |

## 📂 Estructura Principal

```
Pokedex/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/Pokedex/
│   │   │   ├── MainActivity.java
│   │   │   ├── adapter/
│   │   │   │   ├── PokemonAdapter.java
│   │   │   │   └── TipoAdapter.java
│   │   │   ├── dto/
│   │   │   │   ├── PokemonDetailResponse.java
│   │   │   │   ├── TipoDetailResponse.java
│   │   │   │   └── TipoListResponse.java
│   │   │   ├── fragments/
│   │   │   │   ├── PokemonFragment.java
│   │   │   │   └── TiposFragment.java
│   │   │   └── network/
│   │   │       ├── PokeAPI.java
│   │   │       └── RetrofitClient.java
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── fragment_tipos.xml
│   │   │   │   ├── fragment_pokemons.xml
│   │   │   │   ├── item_tipo.xml
│   │   │   │   └── item_pokemon.xml
│   │   │   └── navigation/
│   │   │       └── nav_graph.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle
```

## 🔄 Flujo Funcional Implementado

```
┌─────────────────────────────────────────────────────────────┐
│                     FLUJO DE LA APP                         │
├─────────────────────────────────────────────────────────────┤
│  1. MainActivity (NavHostFragment)                          │
│     ↓ startDestination: TiposFragment (Fragment A)          │
│                                                             │
│  2. GET api/v2/type                                         │
│     ↓ RecyclerView con los tipos de Pokémon                 │
│                                                             │
│  3. Click en un tipo → Navigation Component                 │
│     ↓ Navega a PokemonFragment enviando argumento "tipo"    │
│                                                             │
│  4. GET api/v2/type/{tipo}                                  │
│     ↓ Lista de Pokémon del tipo (nombre + URL)              │
│                                                             │
│  5. Por cada Pokémon: GET api/v2/pokemon/{nombre}           │
│     ↓ Segunda consulta: id, name, base_experience,          │
│       height, weight → RecyclerView                         │
│                                                             │
│  6. Agitar el dispositivo (acelerómetro)                    │
│     ↓ navigateUp() automático al Fragment A                 │
└─────────────────────────────────────────────────────────────┘
```

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `api/v2/type` | GET | Lista todos los tipos de Pokémon |
| `api/v2/type/{tipo}` | GET | Lista los Pokémon de un tipo seleccionado |
| `api/v2/pokemon/{nombre}` | GET | Detalle de un Pokémon (id, name, base_experience, height, weight) |

## 🧮 Regla del Sensor Acelerómetro

La fórmula para detectar un movimiento significativo:

```math
A = \sqrt{X^2 + Y^2 + Z^2}
```

**Lógica implementada en `PokemonFragment`:**

| Variable | Valor / Comportamiento |
|----------|------------------------|
| `magnitud` | `√(x² + y² + z²)` en m/s² |
| `aceleracion` | `|magnitud − GRAVITY_EARTH|` (≈ 9.81 en reposo) |
| Umbral | `> 4` m/s² (a criterio propio) |
| Cooldown | 3000 ms entre disparos (evita navegaciones repetidas) |
| Acción | `navigateUp()` → regresa al Fragment A |

**Ciclo de vida del sensor:**
- `onResume()` → `registerListener` (sensor activo solo en Fragment B)
- `onPause()` → `unregisterListener`

## ✅ Estado frente a la Consigna

### ✔️ Implementado

- [x] MainActivity con NavHostFragment como contenedor de navegación
- [x] Fragment A: consulta `api/v2/type` y muestra los tipos en RecyclerView
- [x] Navegación Fragment A → Fragment B con Navigation Component y argumento
- [x] Fragment B: consulta `api/v2/type/{tipo}` con el tipo recibido
- [x] Segunda consulta por cada Pokémon para obtener id, name, base_experience, height y weight
- [x] Dos RecyclerViews con sus respectivos adapters (`TipoAdapter`, `PokemonAdapter`)
- [x] Acelerómetro activo solo en Fragment B con cálculo de magnitud y umbral
- [x] Regreso automático al Fragment A al detectar movimiento significativo
- [x] Permiso de INTERNET en el AndroidManifest

### 💡 Observación de Mejora

⚠️ Las consultas de detalle de cada Pokémon se realizan de forma **independiente y concurrente**, por lo que el orden de la lista puede variar según la latencia de cada respuesta.

**Recomendación:** Para un orden garantizado, se sugiere:
- Contar respuestas recibidas y notificar al adapter solo cuando se completen todas
- Ordenar la lista por `id` antes de mostrarla
- Usar un índice en el callback para insertar cada Pokémon en su posición original

## 🚀 Cómo Ejecutar

### Requisitos Previos

- Android Studio (versión reciente)
- JDK 17
- Emulador o dispositivo con Android 14 (API 34) o superior

### Ejecutar la Aplicación

1. Clonar o abrir el proyecto en Android Studio
2. Esperar a que Gradle sincronice las dependencias
3. Seleccionar un emulador/dispositivo con API 34+
4. Presionar **Run ▶**

### Probar el Acelerómetro en el Emulador

1. Con la app corriendo en Fragment B, abrir el panel **Extended Controls** (ícono ⋮ del emulador)
2. Ir a **Virtual Sensors → Accelerometer**
3. Mover los ejes **X / Y** para simular la agitación
4. Verificar el regreso automático al Fragment A

**API base:** `https://pokeapi.co/`

---

## 📚 Recursos Adicionales

- [PokeAPI Documentation](https://pokeapi.co/docs/v2)
- [Android Navigation Component](https://developer.android.com/guide/navigation)
- [Retrofit](https://square.github.io/retrofit/)
- [Android Sensors Overview](https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview)

---

## 📄 Licencia

Este proyecto es de uso académico y educativo como parte de un laboratorio de curso universitario.
