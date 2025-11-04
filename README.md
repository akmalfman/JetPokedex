# 🚀 JetPokedex

JetPokedex adalah aplikasi sederhana Pokedex modern untuk Android yang dibuat sebagai demonstrasi arsitektur aplikasi yang bersih, tangguh, dan skalabel. Aplikasi ini menampilkan daftar Pokemon dengan *infinite scrolling*, autentikasi pengguna, dan kemampuan *offline-first* yang solid.

Proyek ini adalah implementasi ketat dari **Clean Architecture** yang dikombinasikan dengan **Domain-Driven Design (DDD)**.

## 📸 Screenshot

| Layar Login | Daftar Pokemon | Detail Pokemon | Profil User |
| :---: | :---: | :---: | :---: |
| [Tempatkan screenshot Login] | [Tempatkan screenshot List] | [Tempatkan screenshot Detail] | [Tempatkan screenshot Profil] |

## ✨ Fitur

* **🔐 Autentikasi Pengguna:** Fungsionalitas Login & Registrasi yang aman.
* **📜 Daftar Pokemon (Pagination):** *Infinite scrolling* yang efisien untuk memuat daftar Pokemon.
* **📊 Detail Pokemon:** Tampilan detail untuk setiap Pokemon, termasuk *abilities* dan statistik.
* **👤 Profil Pengguna:** Menampilkan nama dan email pengguna yang sedang login.
* **⚡ Dukungan Offline-First:** Aplikasi dapat berfungsi penuh secara offline. Data diambil dari *cache* lokal (Couchbase Lite) jika tidak ada koneksi internet.
* **🔍 Pencarian (Search):** Pencarian Pokemon yang berjalan di sisi klien (dari *cache* lokal).

## 🏛️ Arsitektur: Clean Architecture + DDD

Arsitektur adalah fokus utama dari proyek ini. Kami memisahkan aplikasi menjadi tiga lapisan (layer) yang independen.



### 🟣 Domain Layer
* Modul Kotlin murni tanpa dependensi `android.*`.
* Berisi **Entitas (Entities)**: Model data murni (misal: `PokemonDetail`, `User`). Ini adalah inti dari **Domain-Driven Design (DDD)**.
* Berisi **Repository Interfaces (Kontrak):** Mendefinisikan *apa* yang bisa dilakukan aplikasi (misal: `PokemonRepository`, `UserRepository`).
* Berisi **Use Cases (Interactors):** Berisi logika bisnis (business logic) spesifik untuk setiap aksi (misal: `GetPokemonListUseCase`, `LoginUseCase`).

### 🔵 Data Layer
* Mengimplementasikan *interface* dari Domain Layer.
* Bertanggung jawab atas sumber data (tunggal).
* Mengelola **Sumber Data Remote** (via `Retrofit`) dan **Sumber Data Lokal** (via `Couchbase Lite`).
* Berisi **Data Transfer Objects (DTOs):** Model data yang strukturnya meniru JSON dari API.
* Berisi **Mappers:** Fungsi untuk mengubah model DTO (Data) menjadi Entitas (Domain), memisahkan *data layer* dari *domain layer* secara sempurna.

### 🟢 Presentation Layer (UI)
* Dibangun 100% menggunakan **Jetpack Compose**.
* Menggunakan pola **MVVM (Model-View-ViewModel)**.
* **Views (Composable)** hanya mengamati (observe) *StateFlow* dari ViewModel.
* **ViewModels** hanya berbicara dengan **Use Cases** (bukan Repository). ViewModel tidak tahu menahu soal dari mana data berasal.
* Menggunakan **Hilt** untuk *Dependency Injection* guna menyediakan Use Case ke ViewModel.

## 🛠 Tech Stack & Library Utama

* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Arsitektur:** MVVM, Clean Architecture, Domain-Driven Design (DDD)
* **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* **Asynchronous:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://developer.android.com/kotlin/flow)
* **Navigasi:** [Navigation-Compose](https://developer.android.com/jetpack/compose/navigation)
* **Networking (API):** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
* **Database (Lokal/Cache):** [Couchbase Lite](https://www.couchbase.com/products/lite)
* **Data Parsing (JSON):** [Gson](https://github.com/google/gson)
* **(Opsional - Tambahkan jika Anda pakai) Image Loading:** [Coil](https://coil-kt.github.io/coil/)

## 🚀 Cara Menjalankan (Getting Started)

1.  **Clone repositori ini:**
    ```bash
    git clone [https://github.com/NAMA_USER_ANDA/NAMA_REPO_ANDA.git](https://github.com/NAMA_USER_ANDA/NAMA_REPO_ANDA.git)
    ```
2.  **Buka di Android Studio:**
    Buka project dari Android Studio.
3.  **Sync Gradle:**
    Biarkan Gradle mengunduh semua dependensi yang diperlukan.
4.  **Run Aplikasi:**
    Tekan 'Run' (Shift + F10). Hilt (Kapt) akan meng-generate *dependency graph* pada *build* pertama, jadi mungkin perlu waktu sedikit lebih lama.

---
