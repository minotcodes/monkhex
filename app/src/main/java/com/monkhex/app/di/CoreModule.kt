package com.monkhex.app.di

import com.monkhex.app.core.common.DateTimeProvider
import com.monkhex.app.core.common.DefaultDateTimeProvider
import com.monkhex.app.core.common.DefaultMonkHexDispatchers
import com.monkhex.app.core.common.MonkHexDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindDateTimeProvider(impl: DefaultDateTimeProvider): DateTimeProvider

    @Binds
    @Singleton
    abstract fun bindDispatchers(impl: DefaultMonkHexDispatchers): MonkHexDispatchers
}

