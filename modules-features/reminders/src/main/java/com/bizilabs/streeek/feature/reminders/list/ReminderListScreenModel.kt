package com.bizilabs.streeek.feature.reminders.list

import android.Manifest
import android.content.Context
import android.os.Build
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bizilabs.streeek.feature.reminders.manager.ReminderManager
import com.bizilabs.streeek.lib.common.helpers.launcherState
import com.bizilabs.streeek.lib.common.helpers.permissionIsGranted
import com.bizilabs.streeek.lib.common.models.FetchListState
import com.bizilabs.streeek.lib.domain.models.ReminderDomain
import com.bizilabs.streeek.lib.domain.repositories.ReminderRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlin.enums.EnumEntries

data class ReminderListScreenState(
    val isEditing: Boolean = false,
    val fetchListState: FetchListState<ReminderDomain> = FetchListState.Loading,
    val label: String = "",
    val repeat: EnumEntries<DayOfWeek> = DayOfWeek.entries,
    val reminder: ReminderDomain? = null,
    val isAlarmPermissionGranted: Boolean = true,
    val selectedReminder: ReminderDomain? = null,
    val selectedDays: List<DayOfWeek> = emptyList(),
    val selectedHour: Int? = null,
    val selectedMinute: Int? = null,
) {

    val isEditActionEnabled: Boolean
        get() = when {
            reminder != null -> {
                label != reminder.label || selectedDays != reminder.repeat || selectedHour != reminder.hour || selectedMinute != reminder.minute
            }

            else -> {
                label.isNotBlank() && label.length > 4 && selectedDays.isNotEmpty() && selectedHour != null && selectedMinute != null
            }
        }

    val time: String?
        get() = when {
            selectedHour != null && selectedMinute != null -> "$selectedHour:$selectedMinute"
            else -> ""
        }
}

class ReminderListScreenModel(
    private val context: Context,
    private val manager: ReminderManager,
    private val repository: ReminderRepository,
) : StateScreenModel<ReminderListScreenState>(ReminderListScreenState()) {

    init {
        observePermissionState()
        observeReminders()
    }

    private fun observePermissionState() {
        screenModelScope.launch {
            launcherState.collectLatest { checkAlarmPermission() }
        }
    }

    private fun observeReminders() {
        screenModelScope.launch {
            repository.reminders.collectLatest {
                val list = it.values.toList()
                mutableState.update { state ->
                    state.copy(
                        fetchListState = if (list.isEmpty())
                            FetchListState.Empty
                        else
                            FetchListState.Success(list)
                    )
                }
            }
        }
    }

    private fun checkAlarmPermission() {
        val granted =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.permissionIsGranted(permission = Manifest.permission.USE_EXACT_ALARM)
            } else {
                true
            }
        mutableState.update { it.copy(isAlarmPermissionGranted = granted) }
    }

    fun onDismissSheet() {
        mutableState.update { it.copy(isEditing = false, reminder = null) }
    }

    fun onClickCreate() {
        mutableState.update { it.copy(isEditing = true) }
    }

    fun onClickReminder(reminder: ReminderDomain) {
        mutableState.update {
            it.copy(
                isEditing = true,
                reminder = reminder,
                selectedHour = reminder.hour,
                selectedMinute = reminder.minute,
                selectedDays = reminder.repeat,
                label = reminder.label
            )
        }
    }

    fun onLongClickReminder(reminder: ReminderDomain) {

    }

    fun onValueChangeReminderLabel(label: String) {
        mutableState.update { it.copy(label = label) }
    }

    fun onClickReminderDayOfWeek(day: DayOfWeek) {

    }

}
