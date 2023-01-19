package nz.netvalue.codechallenge.web.converter

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

private val logger = Slf4JLoggerFactory.getInstance("nz.netvalue.codechallenge.core.converter")

/**
 * Converts a date+time (or just date) defined as a string into [LocalDateTime].
 * If time is not defined the time part is set to specified [defaultTime] (midnight by default).
 * This convertor tries to guess a date using different patterns.
 */
fun String.toLocalDateTime(defaultTime: LocalTime = LocalTime.MIDNIGHT) : LocalDateTime? {
    // TODO: if time is not defined in the string, replace with default time
    FORMATTERS.forEach { baseFormatter ->
        val builder = DateTimeFormatterBuilder().append(baseFormatter)
        val formatter = TIME_FIELDS
            .filter { defaultTime.isSupported(it) }
            .fold(builder) { b, f -> b.parseDefaulting(f, defaultTime.getLong(f)) }
            .toFormatter(Locale.ROOT)
        try {
            val result = LocalDateTime.parse(this.trim(), formatter)
            logger.debug("Parsed '{}' to {} with {}", this, result, formatter)
            return result
        } catch (e: Exception) {
            logger.debug("Failed to convert '{}' with {}: {}", this, formatter, e.message)
            // ignore
        }
    }
    return null
}

private val FORMATTERS = listOf(
    DateTimeFormatterBuilder()      // semi-universal formatter for year, month, day sequence
        .appendValue(ChronoField.YEAR)
        .optionalStart().appendLiteral('-').optionalEnd()
        .optionalStart().appendLiteral('/').optionalEnd()
        .appendValue(ChronoField.MONTH_OF_YEAR)
        .optionalStart().appendLiteral('-').optionalEnd()
        .optionalStart().appendLiteral('/').optionalEnd()
        .appendValue(ChronoField.DAY_OF_MONTH)
        .optionalStart()
        .optionalStart().appendLiteral(' ').optionalEnd()
        .optionalStart().appendLiteral('T').optionalEnd()
        .appendValue(ChronoField.HOUR_OF_DAY)
        .optionalStart().appendLiteral(':').optionalEnd()
        .appendValue(ChronoField.MINUTE_OF_HOUR)
        .optionalEnd()
        .parseLenient()
        .toFormatter(Locale.ROOT),
    DateTimeFormatterBuilder()      // semi-universal formatter for month, day, year sequence
        .appendValue(ChronoField.MONTH_OF_YEAR)
        .optionalStart().appendLiteral('/').optionalEnd()
        .appendValue(ChronoField.DAY_OF_MONTH)
        .optionalStart().appendLiteral('/').optionalEnd()
        .appendValue(ChronoField.YEAR)
        .optionalStart()
        .optionalStart().appendLiteral(' ').optionalEnd()
        .optionalStart().appendLiteral('T').optionalEnd()
        .appendValue(ChronoField.HOUR_OF_DAY)
        .optionalStart().appendLiteral(':').optionalEnd()
        .appendValue(ChronoField.MINUTE_OF_HOUR)
        .optionalEnd()
        .parseLenient()
        .toFormatter(Locale.ROOT),
    DateTimeFormatterBuilder()      // semi-universal formatter for day, month, year sequence
        .appendValue(ChronoField.DAY_OF_MONTH)
        .optionalStart().appendLiteral('.').optionalEnd()
        .appendValue(ChronoField.MONTH_OF_YEAR)
        .optionalStart().appendLiteral('.').optionalEnd()
        .appendValue(ChronoField.YEAR)
        .optionalStart()
        .optionalStart().appendLiteral(' ').optionalEnd()
        .optionalStart().appendLiteral('T').optionalEnd()
        .appendValue(ChronoField.HOUR_OF_DAY)
        .optionalStart().appendLiteral(':').optionalEnd()
        .appendValue(ChronoField.MINUTE_OF_HOUR)
        .optionalEnd()
        .parseLenient()
        .toFormatter(Locale.ROOT),
)

private val TIME_FIELDS = listOf(
    ChronoField.HOUR_OF_DAY,
    ChronoField.MINUTE_OF_HOUR,
    ChronoField.SECOND_OF_MINUTE,
    ChronoField.MILLI_OF_SECOND,
    ChronoField.MICRO_OF_SECOND,
    ChronoField.NANO_OF_SECOND
)
