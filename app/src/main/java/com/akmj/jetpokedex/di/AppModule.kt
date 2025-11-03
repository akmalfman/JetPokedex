package com.akmj.jetpokedex.di

import android.content.Context
import com.akmj.jetpokedex.data.local.UserDatabase
import com.akmj.jetpokedex.data.local.UserSession
import com.akmj.jetpokedex.data.repository.PokemonRepositoryImpl
import com.akmj.jetpokedex.data.repository.UserRepositoryImpl
import com.akmj.jetpokedex.domain.repository.PokemonRepository
import com.akmj.jetpokedex.domain.repository.UserRepository
import com.akmj.jetpokedex.domain.usecase.CheckLoginStatusUseCase
import com.akmj.jetpokedex.domain.usecase.CheckOfflineDataUseCase
import com.akmj.jetpokedex.domain.usecase.GetLoggedInEmailUseCase
import com.akmj.jetpokedex.domain.usecase.GetPokemonDetailUseCase
import com.akmj.jetpokedex.domain.usecase.GetPokemonListUseCase
import com.akmj.jetpokedex.domain.usecase.LoginUseCase
import com.akmj.jetpokedex.domain.usecase.LogoutUseCase
import com.akmj.jetpokedex.domain.usecase.RefreshDataUseCase
import com.akmj.jetpokedex.domain.usecase.RegisterUseCase
import com.akmj.jetpokedex.domain.usecase.SearchPokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Kita buat semua ini 'Singleton' (hidup 1x)
object AppModule {

    // --- Petunjuk 1: Cara membuat Context ---
    // (Diperlukan oleh PokemonRepositoryImpl)
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    // --- Petunjuk 2: Cara membuat PokemonRepository (Interface) ---
    // (Diperlukan oleh Use Cases)
    @Provides
    @Singleton
    fun providePokemonRepository(context: Context): PokemonRepository {
        // Hilt tahu cara 'provideContext' dari Petunjuk 1
        // Di sinilah kita bilang: "Kalau minta Interface, kasih Implementasi ini"
        return PokemonRepositoryImpl(context)
    }

    // --- Petunjuk 3: Cara membuat Use Cases ---
    // (Diperlukan oleh ViewModels)

    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(repository: PokemonRepository): GetPokemonListUseCase {
        // Hilt tahu cara 'providePokemonRepository' dari Petunjuk 2
        return GetPokemonListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPokemonDetailUseCase(repository: PokemonRepository): GetPokemonDetailUseCase {
        return GetPokemonDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchPokemonUseCase(repository: PokemonRepository): SearchPokemonUseCase {
        return SearchPokemonUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCheckOfflineDataUseCase(repository: PokemonRepository): CheckOfflineDataUseCase {
        return CheckOfflineDataUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRefreshDataUseCase(repository: PokemonRepository): RefreshDataUseCase {
        return RefreshDataUseCase(repository)
    }

    // --- (Tambahkan kode ini di dalam AppModule) ---

    // --- Petunjuk 4: Cara membuat UserDatabase & UserSession ---
    // (Dibutuhkan oleh UserRepositoryImpl)

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        // Hilt sudah tahu cara 'provideContext' dari Petunjuk 1
        return UserDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        // Hilt juga tahu cara 'provideContext'
        return UserSession(context)
    }

    // --- Petunjuk 5: Cara membuat UserRepository (Interface) ---
    // (Dibutuhkan oleh Use Cases Autentikasi)

    @Provides
    @Singleton
    fun provideUserRepository(
        userDb: UserDatabase,
        session: UserSession
    ): UserRepository {
        // Hilt tahu cara membuat 'userDb' dan 'session' dari Petunjuk 4
        // Di sinilah kita bilang: "Kalau ada yang minta UserRepository, kasih UserRepositoryImpl"
        return UserRepositoryImpl(userDb, session)
    }

    // --- Petunjuk 6: Cara membuat Use Cases Autentikasi ---
    // (Akan dibutuhkan oleh LoginRegisterViewModel)

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: UserRepository): RegisterUseCase {
        // Hilt tahu cara 'provideUserRepository' dari Petunjuk 5
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: UserRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: UserRepository): LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCheckLoginStatusUseCase(repository: UserRepository): CheckLoginStatusUseCase {
        return CheckLoginStatusUseCase(repository)
    }

    // ... (di bawah provider CheckLoginStatusUseCase) ...
    @Provides
    @Singleton
    fun provideGetLoggedInEmailUseCase(repository: UserRepository): GetLoggedInEmailUseCase {
        return GetLoggedInEmailUseCase(repository)
    }
}