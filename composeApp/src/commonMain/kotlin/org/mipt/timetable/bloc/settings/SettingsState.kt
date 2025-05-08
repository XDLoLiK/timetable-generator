package org.mipt.timetable.bloc.settings

import org.mipt.timetable.data.model.Settings

sealed class SettingsState {
    data class Unsaved(val settings: Settings, val changed: Settings) : SettingsState()
    data class Saved(val settings: Settings) : SettingsState()
}
