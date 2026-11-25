package ru.brinoauto.demo

import org.junit.Assert.assertEquals
import org.junit.Test

class SystemBarPaddingTest {
    @Test fun preservesBasePaddingWhenThereIsNoSystemInset() {
        assertEquals(12, SystemBarPadding.withInset(12, 0))
    }

    @Test fun addsStatusOrNavigationBarInsetToBasePadding() {
        assertEquals(36, SystemBarPadding.withInset(12, 24))
    }

    @Test fun ignoresNegativeSystemInsets() {
        assertEquals(12, SystemBarPadding.withInset(12, -1))
    }
}
