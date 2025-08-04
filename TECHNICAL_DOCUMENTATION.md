# Space Kayak Server Manager - Technical Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [What We Built](#what-we-built)
3. [Architecture Analysis](#architecture-analysis)
4. [Technical Decisions & Alternatives](#technical-decisions--alternatives)
5. [System Design](#system-design)
6. [Component Breakdown](#component-breakdown)
7. [Failure Handling & Improvements](#failure-handling--improvements)
8. [Interview Questions & Answers](#interview-questions--answers)
9. [Technical Definitions](#technical-definitions)

---

## Project Overview

**Space Kayak Server Manager** is a cloud server management Android application that allows users to provision, monitor, and manage virtual servers with real-time billing, state transitions, and offline-first capabilities.

### Core Business Problem Solved
- **Server Lifecycle Management**: Provision, start, stop, and terminate servers
- **Real-time Billing**: Track server uptime and calculate costs
- **State Synchronization**: Maintain consistency between local and cloud data
- **Offline Operations**: Work without internet and sync when online

---

## What We Built

### 1. **Server Management System**
- **Server Provisioning**: Create new servers with custom configurations
- **State Machine**: Implemented Finite State Machine (FSM) for server states
- **Real-time Monitoring**: Track server status, uptime, and billing

### 2. **Background Processing**
- **BootWorker**: Automatically transitions PENDING servers to RUNNING on app startup
- **BillingWorker**: Calculates billing every 15 minutes for running servers
- **WorkManager Integration**: Reliable background task scheduling

### 3. **Data Layer**
- **Local Storage**: Room database for offline-first operations
- **Cloud Sync**: Firebase Firestore for data persistence and cross-device sync
- **Repository Pattern**: Clean abstraction between data sources

### 4. **User Interface**
- **Jetpack Compose**: Modern declarative UI
- **Navigation**: Multi-screen app with dashboard, server list, and provisioning
- **Real-time Updates**: Reactive UI with StateFlow

---

## Architecture Analysis

### **Clean Architecture + MVVM Pattern**

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Screens   │  │ ViewModels  │  │ Navigation  │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                     Domain Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Models    │  │ Use Cases   │  │ Repository  │        │
│  │             │  │             │  │ Interface   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                      Data Layer                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │ Repository  │  │ Local Data  │  │ Remote Data │        │
│  │ Impl        │  │ Source      │  │ Source      │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

### **Why This Architecture?**

1. **Separation of Concerns**: Each layer has a single responsibility
2. **Testability**: Easy to unit test each component in isolation
3. **Maintainability**: Changes in one layer don't affect others
4. **Scalability**: Easy to add new features or modify existing ones

---

## Technical Decisions & Alternatives

### 1. **Jetpack Compose vs XML Layouts**

**Chosen**: Jetpack Compose
- **Pros**: Declarative UI, less boilerplate, better state management, preview support
- **Alternatives**: XML layouts with ViewBinding/DataBinding
- **Why Compose**: Modern approach, better integration with Kotlin, reactive programming

### 2. **Hilt vs Koin vs Manual DI**

**Chosen**: Hilt
- **Pros**: Compile-time validation, Google support, integration with Android lifecycle
- **Alternatives**: Koin (runtime DI), Manual DI, Dagger
- **Why Hilt**: Type safety, better performance, official Android recommendation

### 3. **Room vs SQLite vs Realm**

**Chosen**: Room
- **Pros**: Compile-time SQL validation, Kotlin coroutines support, easy migrations
- **Alternatives**: SQLite (manual), Realm (object database), SharedPreferences
- **Why Room**: Type safety, reactive queries, excellent Kotlin integration

### 4. **Firebase Firestore vs REST API**

**Chosen**: Firebase Firestore
- **Pros**: Real-time updates, offline support, automatic scaling
- **Alternatives**: REST API with Retrofit, GraphQL, custom backend
- **Why Firestore**: Rapid development, real-time capabilities, Google infrastructure

### 5. **WorkManager vs AlarmManager vs JobScheduler**

**Chosen**: WorkManager
- **Pros**: Battery optimization, network constraints, guaranteed execution
- **Alternatives**: AlarmManager, JobScheduler, Foreground Services
- **Why WorkManager**: Best practices, battery efficiency, constraint support

---

## System Design

### **High-Level System Architecture**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Android App   │    │  Firebase       │    │  Background     │
│                 │    │  Firestore      │    │  Workers        │
│  ┌───────────┐  │    │                 │    │                 │
│  │   UI      │  │◄──►│  ┌───────────┐  │    │  ┌───────────┐  │
│  │   Layer   │  │    │  │   Cloud   │  │    │  │ BootWorker │  │
│  └───────────┘  │    │  │  Database │  │    │  └───────────┘  │
│                 │    │  └───────────┘  │    │                 │
│  ┌───────────┐  │    │                 │    │  ┌───────────┐  │
│  │  Local    │  │    │  ┌───────────┐  │    │  │BillingWrkr│  │
│  │ Database  │  │    │  │  Auth &   │  │    │  └───────────┘  │
│  │  (Room)   │  │    │  │  Security │  │    │                 │
│  └───────────┘  │    │  └───────────┘  │    └─────────────────┘
└─────────────────┘    └─────────────────┘
```

### **Data Flow Architecture**

```
User Action → ViewModel → UseCase → Repository → Data Sources
     ↑                                                      ↓
     └─────────────── StateFlow ←──────────────────────────┘
```

### **State Management**

```kotlin
// Server State Machine
PENDING → RUNNING → STOPPED → TERMINATED
   ↑         ↓         ↑
   └─────────┴─────────┘
```

---

## Component Breakdown

### 1. **Domain Layer**

#### **Models**
```kotlin
@Serializable
data class Server(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val region: String = "",
    val ip: String = "",
    val state: ServerState = ServerState.PENDING,
    val uptime: Long = 0L,
    val lastBillingTimestamp: Long = 0L,
    val updatedAt: Long = 0L,
)

enum class ServerState {
    PENDING, RUNNING, STOPPED, TERMINATED
}
```

#### **Repository Interface**
```kotlin
interface ServerRepository {
    fun getAllServers(): Flow<List<Server>>
    suspend fun getServer(id: String): Server?
    suspend fun provisionServer(server: Server)
    suspend fun updateServer(server: Server)
    suspend fun transitionState(id: String, newState: ServerState): Boolean
    suspend fun syncServersToCloud()
}
```

### 2. **Data Layer**

#### **Local Data Source (Room)**
- **Entity**: `ServerEntity` with Room annotations
- **DAO**: `ServerDao` with reactive queries and FSM logic
- **Database**: `AppDatabase` with migration support

#### **Remote Data Source (Firebase)**
- **DTO**: `ServerDto` for serialization
- **Firestore**: Real-time document updates
- **Sync Logic**: Bidirectional synchronization

#### **Repository Implementation**
```kotlin
@Singleton
class ServerRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val appContext: Context
) : ServerRepository {
    private val mutex = Mutex() // Thread safety
    
    // Implementation with offline-first approach
}
```

### 3. **Presentation Layer**

#### **ViewModels**
- **ServerListViewModel**: Manages server list and state transitions
- **ProvisionViewModel**: Handles server creation

#### **Screens**
- **DashboardScreen**: Overview of servers and billing
- **ServerListScreen**: List of servers with actions
- **ProvisionScreen**: Create new servers

#### **Navigation**
```kotlin
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object ServerList : Screen("server_list")
    object Provision : Screen("provision")
}
```

### 4. **Background Workers**

#### **BootWorker**
- **Purpose**: Initialize server states on app startup
- **Trigger**: One-time work on app launch
- **Logic**: Transition PENDING → RUNNING

#### **BillingWorker**
- **Purpose**: Calculate billing for running servers
- **Trigger**: Periodic work every 15 minutes
- **Logic**: Calculate uptime and update billing

---

## Failure Handling & Improvements

### **Current Failure Points**

1. **Network Failures**
   - **Issue**: No retry mechanism for failed Firestore operations
   - **Impact**: Data loss during sync
   - **Solution**: Implement exponential backoff and retry logic

2. **Database Migration**
   - **Issue**: `fallbackToDestructiveMigration()` loses data
   - **Impact**: User data loss on schema changes
   - **Solution**: Implement proper migration strategies

3. **Worker Failures**
   - **Issue**: Workers can fail silently
   - **Impact**: Billing not calculated, servers not booted
   - **Solution**: Add failure monitoring and retry policies

4. **Memory Management**
   - **Issue**: No pagination for large server lists
   - **Impact**: Performance degradation with many servers
   - **Solution**: Implement pagination and lazy loading

### **Improvement Recommendations**

#### **1. Error Handling & Resilience**
```kotlin
// Add to Repository
suspend fun syncServersToCloud() = withContext(Dispatchers.IO) {
    try {
        // Sync logic with retry
    } catch (e: Exception) {
        // Log error, notify user, queue for retry
    }
}
```

#### **2. Data Consistency**
```kotlin
// Add conflict resolution
@Transaction
suspend fun resolveConflicts(local: Server, remote: Server): Server {
    return when {
        local.updatedAt > remote.updatedAt -> local
        else -> remote
    }
}
```

#### **3. Performance Optimization**
```kotlin
// Add pagination
fun getServersPaginated(page: Int, pageSize: Int): Flow<List<Server>>
```

#### **4. Monitoring & Analytics**
```kotlin
// Add crash reporting
implementation("com.google.firebase:firebase-crashlytics")
```

### **Why Services Are Not Working**

1. **Missing Permissions**: Some background operations require specific permissions
2. **Battery Optimization**: Android may kill background processes
3. **WorkManager Constraints**: Network or battery constraints not met
4. **Hilt Integration**: Worker factory not properly configured

### **How to Fix Services**

```kotlin
// 1. Add proper constraints
val billingWork = PeriodicWorkRequestBuilder<BillingWorker>(
    15, TimeUnit.MINUTES
).setConstraints(
    Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()
).build()

// 2. Add retry policy
.setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.MINUTES)

// 3. Monitor worker status
WorkManager.getInstance(context)
    .getWorkInfoByIdLiveData(billingWork.id)
    .observe(lifecycleOwner) { workInfo ->
        when (workInfo.state) {
            WorkInfo.State.FAILED -> // Handle failure
            WorkInfo.State.SUCCEEDED -> // Handle success
        }
    }
```

---

## Interview Questions & Answers

### **Architecture & Design**

**Q: Why did you choose Clean Architecture?**
A: Clean Architecture provides clear separation of concerns, making the codebase testable, maintainable, and scalable. The domain layer is independent of frameworks, making it easy to change implementations without affecting business logic.

**Q: Explain the Repository pattern in your app.**
A: The Repository pattern abstracts data access logic. `ServerRepository` interface defines contracts, while `ServerRepositoryImpl` handles both local (Room) and remote (Firestore) data sources. This allows easy switching between data sources and testing.

**Q: How do you handle offline-first architecture?**
A: Data is stored locally in Room database first. Users can perform operations offline, and changes are queued for sync when online. Firestore provides offline persistence, and we use WorkManager for reliable background sync.

### **Android Specific**

**Q: Why use Jetpack Compose over XML layouts?**
A: Compose is declarative, reducing boilerplate code. It has better state management with StateFlow, preview support for faster development, and better integration with Kotlin coroutines.

**Q: How does WorkManager differ from other background processing options?**
A: WorkManager is the recommended solution for deferrable background work. It handles battery optimization, network constraints, and guarantees work execution. Unlike AlarmManager, it respects system optimizations.

**Q: Explain your dependency injection setup with Hilt.**
A: Hilt provides compile-time dependency injection. We use `@HiltAndroidApp` for the Application class, `@AndroidEntryPoint` for activities, and `@Module` with `@Provides` for dependencies. This ensures type safety and better performance.

### **Data & Persistence**

**Q: How do you handle database migrations in Room?**
A: Currently using `fallbackToDestructiveMigration()` for simplicity, but in production, we'd implement proper migration strategies using `Migration` classes to preserve user data during schema changes.

**Q: Explain your state management approach.**
A: Using StateFlow for reactive state management. ViewModels expose StateFlow, UI observes changes, and data flows unidirectionally from Repository → UseCase → ViewModel → UI.

**Q: How do you ensure data consistency between local and remote?**
A: Using timestamps (`updatedAt`) to determine the most recent data. Local operations are prioritized for offline scenarios, and conflicts are resolved based on timestamp comparison.

### **Testing**

**Q: How would you test this architecture?**
A: 
- **Unit Tests**: Test UseCases, Repository, and ViewModels in isolation
- **Integration Tests**: Test Repository with real Room database
- **UI Tests**: Test Compose screens with Compose testing library
- **Worker Tests**: Test WorkManager workers with TestWorkManager

**Q: What mocking strategy would you use?**
A: Use MockK for Kotlin-specific mocking, mock Repository interface for ViewModel tests, and use in-memory Room database for integration tests.

### **Performance & Optimization**

**Q: How do you handle large datasets?**
A: Implement pagination in DAO queries, use lazy loading in Compose LazyColumn, and consider using Paging 3 library for more complex scenarios.

**Q: What performance optimizations have you implemented?**
A: 
- Using Flow for reactive data streams
- Coroutines for asynchronous operations
- Room with compile-time SQL validation
- WorkManager for efficient background processing

---

## Technical Definitions

### **Clean Architecture**
A software design philosophy that emphasizes separation of concerns and dependency inversion. The domain layer is independent of external frameworks and databases.

### **MVVM (Model-View-ViewModel)**
An architectural pattern where:
- **Model**: Data and business logic
- **View**: UI components (Compose screens)
- **ViewModel**: UI logic and state management

### **Repository Pattern**
A design pattern that abstracts data access logic. It provides a clean API for data operations and can handle multiple data sources.

### **StateFlow**
A Kotlin coroutines Flow that represents a state holder. It's used for reactive state management and automatically emits values to collectors.

### **WorkManager**
An Android library for managing deferrable background work. It handles system constraints and guarantees work execution.

### **Hilt**
A dependency injection library for Android that reduces boilerplate code and provides compile-time validation.

### **Room**
An Android persistence library that provides an abstraction layer over SQLite. It offers compile-time SQL validation and Kotlin coroutines support.

### **Jetpack Compose**
A modern UI toolkit for Android that uses declarative programming to build native user interfaces.

### **Firebase Firestore**
A NoSQL cloud database that provides real-time updates, offline support, and automatic scaling.

### **Finite State Machine (FSM)**
A computational model that defines a set of states and transitions between them. In our app, servers transition between PENDING → RUNNING → STOPPED → TERMINATED.

### **Offline-First Architecture**
A design approach where the app works primarily with local data and syncs with remote servers when possible. This ensures good user experience even without internet connectivity.

---

## Conclusion

The Space Kayak Server Manager demonstrates modern Android development practices with Clean Architecture, Jetpack Compose, and robust background processing. The app successfully handles server lifecycle management, real-time billing, and offline operations while maintaining code quality and testability.

**Key Strengths:**
- Clean separation of concerns
- Offline-first approach
- Reactive UI with StateFlow
- Reliable background processing
- Comprehensive state management

**Areas for Improvement:**
- Enhanced error handling and retry logic
- Proper database migrations
- Performance optimization for large datasets
- Comprehensive testing coverage
- Monitoring and analytics integration

This architecture provides a solid foundation for scaling the application and adding new features while maintaining code quality and developer productivity. 