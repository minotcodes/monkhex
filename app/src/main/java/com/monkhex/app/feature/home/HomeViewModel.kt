package com.monkhex.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.monkhex.app.core.common.DateTimeProvider
import com.monkhex.app.domain.model.Task
import com.monkhex.app.domain.repository.AuthRepository
import com.monkhex.app.domain.repository.CommitmentRepository
import com.monkhex.app.domain.repository.NotificationRepository
import com.monkhex.app.domain.repository.TaskRepository
import com.monkhex.app.domain.repository.UserRepository
import com.monkhex.app.domain.usecase.CompleteTaskUseCase
import com.monkhex.app.domain.usecase.GetDailyTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
    private val commitmentRepository: CommitmentRepository,
    private val notificationRepository: NotificationRepository,
    private val getDailyTasksUseCase: GetDailyTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null
    private var taskCache: Map<String, Task> = emptyMap()

    init {
        bootstrap()
    }

    private fun bootstrap() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                val userId = authRepository.signInAnonymouslyIfNeeded()
                currentUserId = userId
                notificationRepository.subscribeToCoreTopics()
                runCatching {
                    val token = FirebaseMessaging.getInstance().token.await()
                    notificationRepository.registerToken(userId, token)
                }
                refreshDashboard()
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load dashboard."
                    )
                }
            }
        }
    }

    fun refreshDashboard() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val today = dateTimeProvider.today()
            val todayIso = today.toString()
            runCatching {
                val profile = userRepository.getUserProfile(userId)
                val plan = commitmentRepository.getCommitmentPlan(userId)
                val tasks = getDailyTasksUseCase(userId, today)
                val userTask = taskRepository.getUserTaskForDate(userId, todayIso)
                val completedIds = userTask?.completedTaskIds.orEmpty().toSet()

                taskCache = tasks.associateBy { it.taskId }
                val uiTasks = tasks.map { task ->
                    MissionTaskUi(
                        id = task.taskId,
                        title = task.title,
                        subtitle = task.description,
                        xpReward = task.xpReward,
                        icon = mapIcon(task.title),
                        isCompleted = completedIds.contains(task.taskId)
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userProfile = profile,
                        commitmentPlan = plan,
                        tasks = uiTasks,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Failed to refresh tasks"
                    )
                }
            }
        }
    }

    fun onTaskCompleted(taskId: String) {
        val userId = currentUserId ?: return
        val task = taskCache[taskId] ?: return
        val dateIso = dateTimeProvider.today().toString()

        if (_uiState.value.tasks.firstOrNull { it.id == taskId }?.isCompleted == true) return

        viewModelScope.launch {
            runCatching {
                val updatedProfile = completeTaskUseCase(userId, dateIso, task)
                _uiState.update { state ->
                    state.copy(
                        userProfile = updatedProfile ?: state.userProfile,
                        tasks = state.tasks.map { existing ->
                            if (existing.id == taskId) existing.copy(isCompleted = true) else existing
                        }
                    )
                }
            }
        }
    }

    fun onMissedDayRegistered() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            commitmentRepository.computeAndApplyPenalty(userId, missedToday = true)
            refreshDashboard()
        }
    }

    fun onTabSelected(tab: BottomTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}

