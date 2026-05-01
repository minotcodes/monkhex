package com.monkhex.app.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

interface DateTimeProvider {
    fun today(): LocalDate
    fun nowInstant(): Instant
}

@Singleton
class DefaultDateTimeProvider @Inject constructor() : DateTimeProvider {
    override fun today(): LocalDate = LocalDate.now(ZoneId.systemDefault())

    override fun nowInstant(): Instant = Instant.now()
}

