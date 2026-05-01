package com.monkhex.app.feature.tasks

import com.monkhex.app.domain.model.Task

/**
 * Dedicated feature package placeholder for future task curation and custom task builder flows.
 */
data class TaskEditorState(
    val draftTitle: String = "",
    val draftDescription: String = "",
    val selectedTask: Task? = null
)
