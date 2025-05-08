package org.mipt.timetable.bloc.settings

sealed class SettingsEvent {
    data class UpdateServerUrl(val url: String) : SettingsEvent()
    class Save() : SettingsEvent()
    class Reset() : SettingsEvent()
    class DiscardChanges() : SettingsEvent()
}
