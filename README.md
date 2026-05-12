# ComfyUI Remote

A native Android app for controlling ComfyUI on your local network.

## Features

- 🔌 **Server Connection**: Connect to your ComfyUI server via IP address
- 📋 **Workflows**: Browse and execute workflows from your device
- 📊 **Queue Monitoring**: Real-time monitoring of image generation tasks
- 📷 **History & Preview**: View generation history and image previews
- 🎨 **Dark Theme**: Material Design 3 dark interface

## Prerequisites

- ComfyUI must be running on your local network with the `--listen 0.0.0.0` flag
- Your Android device must be on the same network
- Android 8.0+ (API 26)

## Usage

### Start ComfyUI with LAN access

```bash
python main.py --listen 0.0.0.0
```

### Setup the app

1. Open ComfyUI Remote
2. Enter your ComfyUI server's IP address and port
3. Click "Connect"
4. Start browsing and executing workflows!

## Building

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17 or newer
- Android SDK 34

### Build from source

```bash
# Clone the repository
git clone https://github.com/LaR1417/comfy-remote.git
cd comfy-remote

# Build debug APK
./gradlew assembleDebug
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## Tech Stack

- **Kotlin 1.9.24**
- **Jetpack Compose**
- **Material Design 3**
- **MVVM + Clean Architecture**
- **Hilt** (Dependency Injection)
- **Retrofit 2.9.0**
- **Coil 2.6.0** (Image Loading)
- **DataStore Preferences**

## Project Structure

```
com.comfyui.remote/
├── data/
│   ├── api/          # API interfaces and responses
│   ├── repository/   # Data repositories
│   └── local/        # Preferences manager
├── domain/
│   └── model/        # Business models
├── ui/
│   ├── screens/      # Screen components
│   ├── navigation/   # NavHost and routing
│   └── theme/       # Colors and theme
└── di/              # Hilt modules
```

## License

MIT License
