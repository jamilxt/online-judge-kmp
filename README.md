# 📱 CodeJudge KMP - Multi-platform App

This is the **CodeJudge Mobile and Desktop app**. It lets you solve programming problems on your Android phone or your Computer (Windows/Linux/macOS) using the exact same app!

This app uses **Kotlin Multiplatform (KMP)**, which means we write the code once, and it runs everywhere.

---

## ✨ Features

- **📱 Android App**: Native performance on your phone.
- **💻 Desktop App**: Same app running on your Laptop or PC.
- **🎨 Dark Mode**: Easy on the eyes, matching the web version.
- **📏 Adaptive Layout**: The app "stretches" or "shrinks" perfectly for any screen size.
- **⚡ Fast & Lite**: Quick to open and sub-second code submissions.

---

## 🛠️ Getting Started

Before running this app, you **must** have the [CodeJudge Backend Server](../online-judge) running.

### 1. Requirements

- **Java 17+**: Required to build the app.
- **Android Studio** (Optional): Only if you want to run it on an Android emulator or phone.

### 2. Running on Desktop (Fastest)

Open your terminal in this folder and run:

```bash
./gradlew :composeApp:run
```

### 3. Running on Android

1. Open this folder in **Android Studio**.
2. Wait for the project to load (sync).
3. Select an emulator or plug in your phone.
4. Press the green **Run** button.

> [!NOTE]
> If you are using an Android emulator, it will automatically look for the backend at `http://10.0.2.2:8081`.

---

## 🔗 How it Works

The app works as a "Client". It talks to the **CodeJudge Backend** to fetch problems and submit your code.

- **Frontend (This project)**: The beautiful UI you see and interact with.
- **Backend ([online-judge](../online-judge))**: The "Brain" that stores problems and actually runs your code safely.

---

## 🚀 Check out the Backend

The backend is where all the magic happens! It's built with Spring Boot and supports Local or Docker execution modes.

👉 **[Go to Backend Repository](../online-judge/README.md)**
