package com.example.mob.core.di

import com.example.mob.core.service.AuthService
import com.example.mob.core.service.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthService()
    }
    @Provides
    @Singleton
    fun provideStorageService() :StorageService {
        return StorageService()
    }

}
