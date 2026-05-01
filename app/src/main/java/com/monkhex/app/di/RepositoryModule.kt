package com.monkhex.app.di

import com.monkhex.app.data.repository.AuthRepositoryImpl
import com.monkhex.app.data.repository.CommitmentRepositoryImpl
import com.monkhex.app.data.repository.NotificationRepositoryImpl
import com.monkhex.app.data.repository.SuggestionRepositoryImpl
import com.monkhex.app.data.repository.TaskRepositoryImpl
import com.monkhex.app.data.repository.UserRepositoryImpl
import com.monkhex.app.domain.repository.AuthRepository
import com.monkhex.app.domain.repository.CommitmentRepository
import com.monkhex.app.domain.repository.NotificationRepository
import com.monkhex.app.domain.repository.SuggestionRepository
import com.monkhex.app.domain.repository.TaskRepository
import com.monkhex.app.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindCommitmentRepository(impl: CommitmentRepositoryImpl): CommitmentRepository

    @Binds
    @Singleton
    abstract fun bindSuggestionRepository(impl: SuggestionRepositoryImpl): SuggestionRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}

