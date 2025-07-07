package com.ekorn.adapter.websocket.bitstamp.builder

import com.ekorn.adapter.websocket.bitstamp.model.SubscribeDataRequest
import com.ekorn.adapter.websocket.bitstamp.model.SubscribeRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SubscribeRequestBuilderTest {

    val subject = SubscribeRequestBuilder()

    @Test
    fun `build should return SubscribeRequest with channel == argument`() {

        // When
        val actual = subject.build("test-channel")

        // Then
        val expected = SubscribeRequest(
            data = SubscribeDataRequest(
                channel = "test-channel"
            )
        )

        assertThat(actual).isEqualTo(expected)
    }

}
