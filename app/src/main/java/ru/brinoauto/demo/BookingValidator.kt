package ru.brinoauto.demo
object BookingValidator {
    fun isValid(name: String, phone: String, service: String): Boolean =
        name.isNotBlank() && phone.count(Char::isDigit) >= 10 && service.isNotBlank()
}
