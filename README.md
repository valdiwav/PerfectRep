# PerfectRep

![Android CI](https://github.com/valdiwav/PerferctRep-public/actions/workflows/android.yml/badge.svg)

Aplicación Android que usa la cámara del móvil como **entrenador personal**: detecta la postura del usuario en tiempo real, **cuenta las repeticiones**, **corrige la técnica** mientras entrena y **estima el esfuerzo (RIR)** de cada serie.

Todo el análisis de pose se ejecuta **en el dispositivo** con un modelo MoveNet (TensorFlow Lite / LiteRT), sin enviar vídeo a ningún servidor.

## Funcionalidades

- **Ejercicios soportados:** sentadilla, peso muerto, curl de bíceps y press de hombros.
- **Conteo de repeticiones** a partir de los ángulos articulares (rodilla, cadera, codo, hombro) y de las fases concéntrica y excéntrica.
- **Corrección de técnica en vivo:** el esqueleto se dibuja en rojo cuando se detecta un error, con avisos como «Mantén el equilibrio» o «Mantén el torso recto». Detecta, entre otros:
  - desbalance de fuerza entre piernas o hombros,
  - velocidad excesiva en la fase excéntrica,
  - posición incorrecta de codos, rodillas o barra.
- **Estimación de RIR (repeticiones en reserva):** las primeras repeticiones calibran una velocidad base y, después, la caída de velocidad se traduce en RIR (de 4 a 0).
- **Feedback por serie y por entrenamiento**, con los errores detectados y recomendaciones.
- **Historial y progreso** por ejercicio y por sesión, con gráficas (MPAndroidChart).
- **Cuentas de usuario:** registro, inicio de sesión (email o Google), recuperación de contraseña y perfil con foto.

## Tecnologías

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Views + ViewBinding, Material Components, Navigation (bottom nav) |
| Cámara | Camera2 / CameraX |
| Visión por computador | MoveNet Thunder (int8 y float16) con LiteRT (TensorFlow Lite), con aceleración opcional por GPU o NNAPI |
| Backend | Firebase Authentication, Cloud Firestore, Cloud Storage, Crashlytics, Analytics |
| Otros | Glide, MPAndroidChart |
| Calidad | Tests unitarios con JUnit, Android Lint, GitHub Actions |

## Arquitectura

```
app/src/main/java/com/valdi/perfectrepapp/
├── ml/            # Inferencia: MoveNet y la interfaz PoseDetector
├── data/          # Modelos de dominio: Person, KeyPoint, BodyPart…
├── tracker/       # Seguimiento de personas entre fotogramas
├── utils/
│   ├── counters/  # Lógica de cada ejercicio (reps, errores, RIR) + ExerciseMath
│   ├── CameraSource.kt        # Captura de cámara → modelo → overlay
│   └── VisualizationUtils.kt  # Dibujo del esqueleto sobre la imagen
├── ui/            # Activities y fragments (auth, ejercicio, feedback, progreso)
└── navigation/    # Navegación inferior
```

Flujo principal: `CameraSource` captura los fotogramas → `MoveNet` obtiene 17 puntos clave del cuerpo → el contador del ejercicio (`SquatCounter`, `DeadLiftCounter`…) calcula ángulos y velocidad, cuenta repeticiones y detecta errores → la UI muestra el feedback y los resultados se guardan en Firestore (`users`, `workout_sessions`, `series`, `exercises`).

## Cómo ejecutarlo

Requisitos: Android Studio (JDK 17) y un dispositivo con **Android 8.0 (API 26)** o superior y cámara.

```bash
git clone https://github.com/valdiwav/PerferctRep-public.git
cd PerferctRep-public
./gradlew assembleDebug        # genera app/build/outputs/apk/debug/app-debug.apk
./gradlew testDebugUnitTest    # tests unitarios
```

El proyecto incluye la configuración de Firebase (`app/google-services.json`) del proyecto de demostración. Para usar tu propio backend, sustitúyela por la de tu proyecto de Firebase.

## Tests

La lógica matemática compartida por los contadores (cálculo de ángulos articulares y estimación de RIR) está en `ExerciseMath`, en Kotlin puro, y está cubierta por tests unitarios en `app/src/test`. GitHub Actions ejecuta la compilación, los tests y lint en cada push.

## Autor

Desarrollado por **valdiwav**.
