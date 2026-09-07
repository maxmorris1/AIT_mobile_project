# In-App Auto-Updater Implementation Plan

Implement a mechanism to automatically check for updates from GitHub Releases, download the latest APK, and prompt the user for installation.

## User Review Required

> [!IMPORTANT]
> **GitHub Repository URL**: I need the owner and repository name (e.g., `owner/repo`) to query the GitHub API.
> **Installation Permission**: The user will be prompted to allow "Install from Unknown Sources" by the system. This is an Android security requirement for non-Play Store apps.

## Proposed Changes

### 1. Configuration & Dependencies
- **[MODIFY] [libs.versions.toml](file:///C:/Users/maxmo/AndroidStudioProjects/AITmobileproject/gradle/libs.versions.toml)**: Add OkHttp for networking and Kotlin Serialization for JSON parsing.
- **[MODIFY] [build.gradle.kts](file:///C:/Users/maxmo/AndroidStudioProjects/AITmobileproject/app/build.gradle.kts)**: Apply the new dependencies and enable `buildConfig` to access the current version in code.
- **[MODIFY] [AndroidManifest.xml](file:///C:/Users/maxmo/AndroidStudioProjects/AITmobileproject/app/src/main/AndroidManifest.xml)**:
    - Add `REQUEST_INSTALL_PACKAGES` permission.
    - Register a `FileProvider` to securely share the downloaded APK with the system installer.
- **[NEW] [filepaths.xml](file:///C:/Users/maxmo/AndroidStudioProjects/AITmobileproject/app/src/main/res/xml/filepaths.xml)**: Define the directory where the APK will be stored (internal cache or downloads).

### 2. Networking & Data
- **[NEW] `GitHubRelease` Model**: Data class to map the GitHub API response (`tag_name`, `assets.browser_download_url`, etc.).
- **[NEW] `UpdateChecker` Service**:
    - Function to fetch the latest release from GitHub.
    - Logic to compare `BuildConfig.VERSION_NAME` with the GitHub `tag_name`.

### 3. Update Flow Logic
- **[NEW] `UpdateViewModel`**:
    - Manage the state: `Idle`, `Checking`, `UpdateAvailable`, `Downloading`, `ReadyToInstall`.
    - Handle the download using `DownloadManager` or a background coroutine with OkHttp.
- **[MODIFY] `MainActivity`**:
    - Initialize the update check on app launch.
    - Handle the "Install" Intent when the download completes.

### 4. UI Components
- **[NEW] `UpdateDialog`**: A Material 3 dialog to inform the user about the new version and show download progress.

## Verification Plan

### Automated Tests
- Unit test for version comparison logic (e.g., comparing "1.0" with "1.1").
- Mocking the GitHub API response to verify the parsing logic.

### Manual Verification
1. **Mock a New Version**: Temporarily lower the app's `versionName` in `build.gradle.kts` and run the app.
2. **Trigger Check**: Verify the "Update Available" dialog appears.
3. **Download**: Click update and monitor the progress bar.
4. **Install**: Ensure the system installer opens and attempts to install the APK (verification of `FileProvider` and Permissions).
