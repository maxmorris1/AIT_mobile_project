<div align="center">

# AIT Mobile Project
**A High-Performance Study and Productivity Suite for Android**

[![Latest Release](https://img.shields.io/github/v/release/maxmorris1/AIT_mobile_project?style=for-the-badge&color=orange)](https://github.com/maxmorris1/AIT_mobile_project/releases)
[![Build Status](https://img.shields.io/github/actions/workflow/status/maxmorris1/AIT_mobile_project/android.yml?branch=main&style=for-the-badge)](https://github.com/maxmorris1/AIT_mobile_project/actions)
[![Platform](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?style=for-the-badge)](https://kotlinlang.org)

---

![AIT Mobile Project Banner](media/banner.svg)

</div>

## Overview

The AIT Mobile Project is an Android-native application designed to streamline the academic workflow. It integrates advanced note-taking, a geometric flashcard system, and an intuitive custom navigation interface to provide a cohesive productivity environment.

## Key Features

### Custom Flashcard Engine
The application features a proprietary flashcard rendering system utilizing advanced path operations. This ensures mathematically perfect rounded "bites" at component junctions, providing a unique and modern aesthetic without rendering artifacts.

### Dial Navigation (NavWheel)
An ergonomic, thumb-optimized navigation dial allows users to switch between modules with fluid haptic-enabled gestures. This reduces cognitive load and improves one-handed usability.

### In-App Auto-Updater
Integrated Over-The-Air (OTA) updates via GitHub Releases. The application automatically detects new versions, manages the secure download process, and facilitates installation through a native system intent.

### Intelligent Note Taking
A streamlined interface for capturing and organizing academic content, built on top of modern Jetpack Compose principles for maximum responsiveness.

## Technology Stack

*   **UI Framework**: Jetpack Compose (Material 3)
*   **Language**: Kotlin (Coroutines, StateFlow)
*   **Networking**: OkHttp 4
*   **Data Handling**: Kotlinx Serialization
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **Build System**: Gradle Kotlin DSL

## Installation

### For Users
1.  Navigate to the [Releases](https://github.com/maxmorris1/AIT_mobile_project/releases) page.
2.  Download the latest `app-debug.apk`.
3.  Open the file on your Android device.
4.  If prompted, allow "Installation from unknown sources" for your file manager or browser.

### For Developers
1.  Clone the repository:
    ```bash
    git clone https://github.com/maxmorris1/AIT_mobile_project.git
    ```
2.  Open the project in **Android Studio (Ladybug or newer)**.
3.  Sync the project with Gradle files.
4.  Run the `app` module on a physical device or emulator (API 24+).

## Project Links

*   [Documentation](docs/README.md)
*   [Report an Issue](https://github.com/maxmorris1/AIT_mobile_project/issues)
*   [Latest APK](https://github.com/maxmorris1/AIT_mobile_project/releases/latest)

## License
Distributed under the MIT License. See `LICENSE` for more information.

---

<div align="center">

[![Star Tracker](https://img.shields.io/github/stars/maxmorris1/AIT_mobile_project?style=social)](https://github.com/maxmorris1/AIT_mobile_project)

</div>
