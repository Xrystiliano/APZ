# SmartLock Android App

Android-додаток для системи розумних замків **SmartLock HNURE**, написаний на **Kotlin + Jetpack Compose**.

## Технологічний стек

| Компонент | Технологія |
|-----------|-----------|
| UI | Jetpack Compose + Material 3 |
| Навігація | Navigation Compose |
| HTTP | Retrofit 2 + OkHttp |
| DI | Hilt |
| Стан | ViewModel + StateFlow |
| Зберігання | DataStore Preferences |
| Async | Kotlin Coroutines |
| Min SDK | API 26 (Android 8.0) |

## Функціональність

- 🔐 **Вхід / Реєстрація** з JWT авторизацією
- 🏠 **Головна** — список замків, пошук, статистика, pull-to-refresh
- ➕ **Створення замку** через bottom sheet
- 🔒 **Деталі замку** — управління, інформація
  - **Вкладка Інфо** — ID, статус, стан, heartbeat
  - **Вкладка Користувачі** — запрошення, ролі, видалення
  - **Вкладка Ключі** — генерація, копіювання, відкликання
- 👤 **Профіль** — дані акаунту, вихід
- 🚪 **Авто-вихід** при 401 відповіді від сервера

## Запуск

### Вимоги
- Android Studio Hedgehog або новіший
- Java 17+
- Android SDK 34

### Відкрити проект
1. Відкрийте Android Studio
2. File → Open → оберіть папку `d:\HNURE\APZ\mobile`
3. Зачекайте синхронізації Gradle
4. Запустіть на емуляторі або пристрої

### Бекенд
Переконайтесь, що SmartLock backend запущено на `localhost:8080`.
Емулятор Android з'єднується з localhost через `10.0.2.2`.

## Структура проекту

```
app/src/main/java/com/hnure/smartlock/
├── data/
│   ├── api/          # Retrofit інтерфейс + моделі
│   ├── local/        # DataStore, SessionManager
│   └── repository/   # AuthRepository, LockRepository
├── di/               # Hilt модулі
├── ui/
│   ├── theme/        # Кольори, типографіка, тема
│   ├── navigation/   # NavGraph + MainViewModel
│   ├── components/   # LockCard, Chips, ConfirmDialog
│   └── screens/
│       ├── login/
│       ├── register/
│       ├── home/
│       ├── lockdetail/
│       └── profile/
├── SmartLockApp.kt
└── MainActivity.kt
```

## Дизайн

Палітра відповідає веб-версії:
- `#0F62FE` — IBM Blue (primary)
- `#161616` — Dark text
- `#F4F4F4` — Background
- `#FFFFFF` — Cards/surfaces
- `#198038` — Online/success green
