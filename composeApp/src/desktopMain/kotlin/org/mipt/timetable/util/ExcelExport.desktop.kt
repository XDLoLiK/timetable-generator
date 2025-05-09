package org.mipt.timetable.util

import io.github.evanrupert.excelkt.workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.mipt.timetable.data.model.ArrangedClass
import org.mipt.timetable.data.model.WeekDay

actual fun exportExcel(
    filePath: String,
    content: List<ArrangedClass>
) {
    val groups = content.groupBy {
        it.group
    }.mapValues { (_, classes) ->
        classes.sortedWith(
            compareBy(
                { it.day },
                { it.classNumber }
            )
        )
    }

    workbook {
        sheet("Schedule") {
            row {
                cell("Days")
                cell("Hours")
                groups.keys.forEach {
                    cell(it)
                }
            }

            WeekDay.entries.take(6).forEach { day ->
                (0..6).forEach { classNum ->
                    row {
                        if (classNum == 0) {
                            cell("$day.name")
                        } else {
                            cell("")
                        }
                        cell(classNumberToHours(classNum))
                        groups.forEach { (_, classes) ->
                            val classInSlot = classes.find {
                                it.day == day && it.classNumber == classNum
                            }
                            cell(
                                classInSlot?.let {
                                    "${it.teacher}\n" +
                                            "${it.room}\n" +
                                            "Class = ?"
                                } ?: ""
                            )
                        }
                    }
                }

                val currentRow = 1 + (7 * day.ordinal)
                xssfSheet.addMergedRegion(
                    CellRangeAddress(
                        currentRow,
                        currentRow + 6,
                        0,
                        0
                    )
                )
            }
        }
    }.write(filePath)
}

private fun classNumberToHours(classNumber: Int): String {
    return when (classNumber) {
        0 -> "9:00 - 10:25"
        1 -> "10:45 - 12:10"
        2 -> "12:20 - 13:35"
        3 -> "13:55 - 15:20"
        4 -> "15:30 - 16:55"
        5 -> "17:05 - 18:30"
        6 -> "18:35 - 20:00"
        else -> ""
    }
}
