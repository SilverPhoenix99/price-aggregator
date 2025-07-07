package com.ekorn.adapter.websocket.bitstamp

import com.fasterxml.jackson.core.JsonParser
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit.MILLIS
import java.util.concurrent.TimeUnit.NANOSECONDS

class MicrotimestampDeserializerTest {

    val subject = MicrotimestampDeserializer()

    // In practice, the timestamp is in milliseconds, where the last 3 digits are always 0.
    @Test
    fun `deserialize should convert microseconds number into an Instant, truncated to milliseconds`() {

        // Given
        val now = Instant.now()
        val nanos = NANOSECONDS.toMicros(now.nano.toLong())
        val microseconds = "${now.epochSecond}${nanos}"

        val parser: JsonParser = mockk {
            every { text } returns microseconds
        }

        // When
        val actual = subject.deserialize(parser, null)

        // Then
        val expected = now.truncatedTo(MILLIS)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `deserialize should return null`() {

        // When
        val actual = subject.deserialize(null, null)

        // Then
        assertThat(actual).isNull()
    }

}
