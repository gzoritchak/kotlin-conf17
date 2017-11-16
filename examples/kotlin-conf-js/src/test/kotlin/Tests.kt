

import io.data2viz.presentation.PresState
import io.data2viz.presentation.toCurrentState
import io.data2viz.presentation.toLocation
import kotlin.test.*

class ConfContent {

    @Test
    fun test_Titles() {
        assertEquals("Frontend Kotlin from the trenches", kotlinConf.title )
    }

    @Test
    fun test_page_numbers() {
        assertEquals(15, kotlinConf.pages.size )
    }

    @Test
    fun test_history_conversion() {
        assertNull("auie".toCurrentState())
        assertNull("--1-2".toCurrentState())
        assertEquals(PresState(0,0), "#0-0".toCurrentState())
        assertEquals(PresState(1,0), "#1-0".toCurrentState())

        assertEquals("0-0", PresState(0,0).toLocation())
        assertEquals("1-3", PresState(1,3).toLocation())

    }

    @Test
    @Ignore
    fun test_TitlesFalse() {
        assertEquals("Frontend Kotlin from the trenche", kotlinConf.title )
    }

}
