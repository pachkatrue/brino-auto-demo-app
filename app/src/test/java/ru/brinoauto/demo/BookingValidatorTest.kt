package ru.brinoauto.demo
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookingValidatorTest {
    @Test fun acceptsCompleteBooking() {
        assertTrue(BookingValidator.isValid("Павел", "+7 900 000-00-00", "Замена масла в ДВС"))
    }
    @Test fun rejectsMissingName() {
        assertFalse(BookingValidator.isValid("  ", "+7 900 000-00-00", "Замена масла в ДВС"))
    }
    @Test fun rejectsPhoneWithoutEnoughDigits() {
        assertFalse(BookingValidator.isValid("Павел", "123", "Замена масла в ДВС"))
    }
    @Test fun rejectsMissingService() {
        assertFalse(BookingValidator.isValid("Павел", "+7 900 000-00-00", ""))
    }
}
