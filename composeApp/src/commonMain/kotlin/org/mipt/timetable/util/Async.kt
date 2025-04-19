//package org.mipt.timetable.util
//
//import arrow.core.Either
//import arrow.core.left
//import arrow.core.right
//import kotlinx.coroutines.TimeoutCancellationException
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.withTimeout
//
//inline fun <reified T> runWithTimeout(
//    timeout: Long,
//    crossinline runnable: suspend () -> T
//): Either<TimeoutCancellationException, T> {
//    return runBlocking {
//        try {
//            withTimeout(timeout) {
//                runnable().right()
//            }
//        } catch (e: TimeoutCancellationException) {
//            e.left()
//        }
//    }
//}
