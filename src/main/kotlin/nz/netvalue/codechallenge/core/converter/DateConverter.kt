package nz.netvalue.codechallenge.core.converter

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.time.temporal.ChronoField
import java.util.*

private val logger = Slf4JLoggerFactory.getInstance("nz.netvalue.codechallenge.core.converter")

/**
 * Converts a date+time (or just date) defined as a string into [LocalDateTime].
 * If time is not defined the time part is set to 00:00 (midnight).
 * This convertor tries to guess a date using different patterns.
 */
// TODO: use another algo instead of trying all patterns sequentially?
fun String.toLocalDateTime() : LocalDateTime? {
    FORMATTERS.forEach { formatter ->
        try {
            val result = LocalDateTime.parse(this.trim(), formatter)
            logger.debug("Parsed '{}' with {}", this, formatter)
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
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
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
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
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
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseLenient()
        .toFormatter(Locale.ROOT),
)
