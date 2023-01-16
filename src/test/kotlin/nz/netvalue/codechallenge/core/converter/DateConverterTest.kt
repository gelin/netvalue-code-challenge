package nz.netvalue.codechallenge.core.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DateConverterTest {

    @Test
    fun testDatePatterns_twoDigits() {
        val expected = LocalDateTime.of(LocalDate.of(2023, 10, 16), LocalTime.MIDNIGHT)
        assertEquals(expected, "2023-10-16".toLocalDateTime())
        assertEquals(expected, "2023/10/16".toLocalDateTime())
        assertEquals(expected, "10/16/2023".toLocalDateTime())
        assertEquals(expected, "16.10.2023".toLocalDateTime())
    }

    @Test
    fun testDatePatterns_oneDigit() {
        val expected = LocalDateTime.of(LocalDate.of(2023, 1, 9), LocalTime.MIDNIGHT)
        assertEquals(expected, "2023-1-9".toLocalDateTime())
        assertEquals(expected, "2023/1/9".toLocalDateTime())
        assertEquals(expected, "1/9/2023".toLocalDateTime())
        assertEquals(expected, "9.1.2023".toLocalDateTime())
    }

    @Test
    fun testDateTimePatterns_twoDigits() {
        val expected = LocalDateTime.of(LocalDate.of(2023, 10, 16), LocalTime.of(13, 42))
        assertEquals(expected, "2023-10-16 13:42".toLocalDateTime())
        assertEquals(expected, "2023/10/16 13:42".toLocalDateTime())
        assertEquals(expected, "10/16/2023 13:42".toLocalDateTime())
        assertEquals(expected, "16.10.2023 13:42".toLocalDateTime())
    }

    @Test
    fun testDateTimePatterns_oneDigit() {
        val expected = LocalDateTime.of(LocalDate.of(2023, 1, 9), LocalTime.of(9, 7))
        assertEquals(expected, "2023-1-9 9:7".toLocalDateTime())
        assertEquals(expected, "2023/1/9 9:7".toLocalDateTime())
        assertEquals(expected, "1/9/2023 9:7".toLocalDateTime())
        assertEquals(expected, "9.1.2023 9:7".toLocalDateTime())
    }

}
