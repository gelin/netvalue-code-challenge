package nz.netvalue.codechallenge.web.view

import java.time.Instant

data class ResponseView<T>(
    val timestamp: Instant = Instant.now(),
    val status: Int = 200,
    val result: T
)
