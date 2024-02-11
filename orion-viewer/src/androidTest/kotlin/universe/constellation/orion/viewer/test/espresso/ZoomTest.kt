package universe.constellation.orion.viewer.test.espresso

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import universe.constellation.orion.viewer.R
import universe.constellation.orion.viewer.test.framework.BookDescription

@RunWith(Parameterized::class)
class ZoomTest(bookDescription: BookDescription): BaseEspressoTest(bookDescription) {

    @Test
    fun testZoomChange() {
        openZoom()
        onView(withId(R.id.zoom_picker_seeker)).perform(setSeekBarProgress { it / 3 })
        applyZoom()
        Thread.sleep(1000)
        onView(withId(R.id.view)).perform(swipeUp())

        Thread.sleep(1000)
        openZoom()
        onView(withId(R.id.zoom_picker_seeker)).perform(setSeekBarProgress { it * 2 })
        applyZoom()

        Thread.sleep(1000)
        onView(withId(R.id.view)).perform(swipeUp())
    }
}