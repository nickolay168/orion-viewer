/*
 * Orion Viewer - pdf, djvu, xps and cbz file viewer for android devices
 *
 * Copyright (C) 2011-2017 Michael Bogdanov & Co
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package universe.constellation.orion.viewer

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/*pages are zero based*/
val Int.isZeroBasedEvenPage: Boolean
    get() = this % 2 == 1

inline fun <T> task(name: String, task: () -> T) {
    log("Starting $name task...")
    task()
    log("Task $name is finished!")
}

@OptIn(ExperimentalContracts::class)
inline fun <R> timing(message: String, block: () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    log("Starting task '$message'...")

    val start = currentTimeMillis()
    return block().also {
        log("Task '$message' is finished in ${currentTimeMillis() - start} ms")
    }
}

fun memoryInMB(memoryInBytes: Long): String {
    return "${memoryInBytes / 1024 / 1024}Mb"
}

expect fun currentTimeMillis(): Long

expect inline  fun <R> Any.mySynchronized(p: () -> R): R