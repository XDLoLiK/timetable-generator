package org.mipt.timetable.bloc.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mipt.timetable.data.model.Settings

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow<SettingsState>(SettingsState.Saved(Settings()))
    val state = _state.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Reset -> onReloadSettings(event)
            is SettingsEvent.Save -> onSaveSettings(event)
            is SettingsEvent.UpdateServerUrl -> onUpdateServerUrl(event)
            is SettingsEvent.DiscardChanges -> onDiscardChanges(event)
        }
    }

    private fun onReloadSettings(event: SettingsEvent.Reset) {
        _state.update {
            SettingsState.Saved(Settings())
        }
    }

    private fun onSaveSettings(event: SettingsEvent.Save) {
        _state.update { current ->
            when (current) {
                is SettingsState.Saved -> current
                is SettingsState.Unsaved -> SettingsState.Saved(settings = current.changed)
            }
        }
    }

    private fun onUpdateServerUrl(event: SettingsEvent.UpdateServerUrl) {
        _state.update { current ->
            when (current) {
                is SettingsState.Saved -> SettingsState.Unsaved(
                    settings = current.settings,
                    changed = current.settings.copy(serverUrl = event.url)
                )

                is SettingsState.Unsaved -> current.copy(changed = current.changed.copy(serverUrl = event.url))
            }
        }
    }

    private fun onDiscardChanges(event: SettingsEvent.DiscardChanges) {
        _state.update { current ->
            when (current) {
                is SettingsState.Saved -> current
                is SettingsState.Unsaved -> SettingsState.Saved(settings = current.settings)
            }
        }
    }
}
