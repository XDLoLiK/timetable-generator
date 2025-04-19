package org.mipt.timetable.bloc.timetable

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mipt.timetable.data.repository.TimetableRepository

class TimetableViewModel(
    private val _repository: TimetableRepository = TimetableRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(TimetableState())
    val state = _state.asStateFlow()

    fun onEvent(event: TimetableEvent) {
    }
}
