package org.mipt.timetable.util

import org.mipt.timetable.data.model.ArrangedClass

expect fun exportExcel(
    filePath: String,
    content: List<ArrangedClass>
)
