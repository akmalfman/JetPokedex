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
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun providePokemonRepository(context: Context): PokemonRepository {
        return PokemonRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(repository: PokemonRepository): GetPokemonListUseCase {
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

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDb: UserDatabase,
        session: UserSession
    ): UserRepository {
        return UserRepositoryImpl(userDb, session)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: UserRepository): RegisterUseCase {
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

    @Provides
    @Singleton
    fun provideGetLoggedInEmailUseCase(repository: UserRepository): GetLoggedInEmailUseCase {
        return GetLoggedInEmailUseCase(repository)
    }
}