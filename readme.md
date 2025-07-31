# Spacekayak Server Manager – Android Assignment

This is the technical assignment for the Android Developer role at [Spacekayak](https://spacekayak.xyz), built using **Kotlin**, **Jetpack Compose**, **MVVM**, **Hilt**, **WorkManager**, and **Firebase Firestore**.

## 📱 Project Overview

The app allows users to manage cloud servers with the ability to:
- **Create new servers**
- **Start / Stop servers**
- **Automatically transition states**
- **Track uptime and billing**
- **Sync changes with Firestore**
- **Work offline and sync when online**

---

## 🧱 Architecture

- **MVVM + Clean Architecture**
- **Unidirectional Data Flow (UI → ViewModel → UseCase → Repository)**
- **Jetpack Compose UI**
- **Hilt for DI**
- **Room for local persistence**
- **Firebase Firestore for remote sync**
- **WorkManager for background billing and boot logic**

---

## 🔧 Tech Stack

| Tool/Library        | Purpose                                |
|---------------------|----------------------------------------|
| Kotlin + Coroutines | Language and concurrency               |
| Jetpack Compose     | UI                                     |
| Hilt                | Dependency Injection                   |
| Room                | Local Database                         |
| Firebase Firestore  | Remote DB + Syncing                    |
| WorkManager         | Background Work (Billing + Boot)       |
| StateFlow / Flow    | Reactive state and data streams        |
| ViewModel           | UI logic holder                        |

---

## 💡 Features Implemented

- ✅ **Add, start, stop servers**
- ✅ **State transitions (e.g. PENDING → RUNNING)**
- ✅ **Server billing based on uptime**
- ✅ **Offline-first Room cache with Firestore sync**
- ✅ **WorkManager jobs (BootWorker + BillingWorker)**
- ✅ **Notification support (POST_NOTIFICATIONS permission)**
- ✅ **Composable UI with previewable components**
- ✅ **MVVM and clean separation of concerns**
- ✅ **Dependency Injection via Hilt**

---

## ⚙️ How to Run

1. Clone the project:
   ```bash
   git clone https://github.com/yourusername/spacekayak-server-manager.git
