package com.monkhex.app.domain.model

data class UserTask(
    val userId: String,
    val date: String,
    val selectedTaskIds: List<String>,
    val completedTaskIds: List<String>,
    val source: TaskSource
)

