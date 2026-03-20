# 📱 CodeJudge KMP - Multi-platform App

This is the **CodeJudge Mobile and Desktop app**. It lets you solve programming problems on your Android phone or your Computer (Windows/Linux/macOS) using the exact same app!

This app uses **Kotlin Multiplatform (KMP)**, which means we write the code once, and it runs everywhere.


---

## 📸 Screenshots

### 🖥️ Desktop (Windows/Linux/macOS)

#### Problem List & Challenges
![Desktop Homepage](./docs/screenshots/homepage-desktop.png)

#### Code Editor with Dark Theme
![Desktop Editor](./docs/screenshots/problem_editor-desktop.png)

#### Success Verdict
![Desktop Result](./docs/screenshots/submission_accepted-desktop.png)

### 📱 Android App

#### Mobile Home Screen
![Android Homepage](./docs/screenshots/homepage-android.png)

#### Coding on Mobile (Portrait)
![Android Editor](./docs/screenshots/problem_editor_1-android.png)

#### Submission Accepted!
![Android Result](./docs/screenshots/submission_accepted-android.png)

---

## ✨ Features

- **📱 Android App**: Native performance on your phone.
- **💻 Desktop App**: Same app running on your Laptop or PC.
- **🎨 Dark Mode**: Easy on the eyes, matching the web version.
- **📏 Adaptive Layout**: The app "stretches" or "shrinks" perfectly for any screen size.
- **⚡ Fast & Lite**: Quick to open and sub-second code submissions.

---

## 🛠️ Getting Started

Before running this app, you **must** have the [CodeJudge Backend Server](https://github.com/jamilxt/online-judge) running.

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
- **Backend ([online-judge](https://github.com/jamilxt/online-judge))**: The "Brain" that stores problems and actually runs your code safely.

---

## 🚀 Check out the Backend

The backend is where all the magic happens! It's built with Spring Boot and supports Local or Docker execution modes.

👉 **[Go to Backend Repository](https://github.com/jamilxt/online-judge)**

---

## 👨 Developed By

<a href="https://twitter.com/jamil_xt" target="_blank">
  <img src="https://avatars.githubusercontent.com/jamilxt" width="90" align="left">
</a>

**Md Jamilur Rahman**

[![Twitter](https://img.shields.io/badge/-Twitter-1DA1F2?logo=x&logoColor=white&style=for-the-badge)](https://twitter.com/jamil_xt)
[![Medium](https://img.shields.io/badge/-Medium-00AB6C?logo=medium&logoColor=white&style=for-the-badge)](https://medium.com/@jamilxt)
[![Linkedin](https://img.shields.io/badge/-LinkedIn-0077B5?logo=linkedin&logoColor=white&style=for-the-badge)](https://www.linkedin.com/in/jamilxt/)
[![Web](https://img.shields.io/badge/-Web-0073E6?logo=appveyor&logoColor=white&style=for-the-badge)](https://jamilxt.com/)
