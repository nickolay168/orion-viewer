package universe.constellation.orion.viewer.test.engine

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runners.Parameterized
import universe.constellation.orion.viewer.test.framework.BookDescription
import universe.constellation.orion.viewer.test.framework.BookTest

class SearchTest(
    bookDescription: BookDescription,
    private val page1Based: Int,
    private val text: String,
    private val occurrences: Int
) : BookTest(bookDescription) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Test `{2}` text search on page {1} at {0}")
        fun testData(): Iterable<Array<Any>> {
            return listOf(
                    arrayOf(BookDescription.SICP, 12, "These programs", 1),
                    arrayOf(BookDescription.SICP, 12, "These", 2),
                    arrayOf(BookDescription.SICP, 12, "12334", 0),
                    arrayOf(BookDescription.ALICE, 6, "tunnel", 1),
                    arrayOf(BookDescription.ALICE, 6, "Then", 3),
                    arrayOf(BookDescription.ALICE, 6, "The123", 0)
            )
        }
    }

    @Test
    fun testSelection() {
        val result = document.searchPage(page1Based - 1, text)
        assertEquals("Number of `$text` occurrences", occurrences, result?.size ?: 0)
    }
}