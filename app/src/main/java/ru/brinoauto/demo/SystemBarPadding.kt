package ru.brinoauto.demo

internal object SystemBarPadding {
    fun withInset(basePadding: Int, systemInset: Int): Int =
        basePadding + systemInset.coerceAtLeast(0)
}
