# Velmora Client

A fresh client-side Minecraft project named **Velmora Client**.

## Target

- Minecraft Java Edition: **1.21.11**
- Mod loader: **Fabric**
- Mappings: **Yarn 1.21.11+build.6**
- Java: **21**
- Build system: **Gradle + Fabric Loom**

## Build

Install JDK 21 and Gradle 9.2.1, then run:

```bash
gradle --no-daemon build
```

The remapped mod JAR will be created in `build/libs/`.

GitHub Actions automatically builds the project on pushes and pull requests and uploads the JAR as a workflow artifact.

## Project layout

- `src/client/java/dev/velmora/client/VelmoraClient.java` — client entrypoint
- `src/main/resources/fabric.mod.json` — Fabric metadata
- `build.gradle` — Loom, Yarn, Fabric Loader and Fabric API dependencies
- `gradle.properties` — centralized version settings

The project currently contains only the build foundation and client entrypoint. Gameplay and GUI features will be added after the base build is verified.
