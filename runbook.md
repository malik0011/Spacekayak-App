# Runbook – Spacekayak Android Task

## ✅ FSM Logic
- States: `PENDING`, `RUNNING`, `STOPPED`
- Transitions allowed:
    - `PENDING -> RUNNING`
    - `RUNNING -> STOPPED`
- Enforced in: `ServerRepository.transitionState()`

## ✅ Worker Strategy
- `BootWorker`: Runs once at app launch. Converts all `PENDING` → `RUNNING`
- `BillingWorker`: Runs every 15 min to update billing info using uptime

## ✅ Sync Notes
- Firestore as source of truth
- Room used for offline-first support (local DB)
- All changes pushed to both Room & Firestore (write-through)
- App launches with data synced from local DB and updated in background

## ✅ Background Work Handling
- Workers scheduled via `WorkManager` in `MyApp`
- Workers use `@HiltWorker` for dependency injection
- Proper retry handling using `Result.retry()`

