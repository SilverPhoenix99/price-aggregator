package com.ekorn.port.websocket.bitstamp

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant
import java.util.concurrent.TimeUnit

class MicrotimestampDeserializer : JsonDeserializer<Instant>() {

    override fun deserialize(
        p: JsonParser?,
        ctxt: DeserializationContext?
    ): Instant? {

        val microseconds = p?.text?.toLong()
        if (microseconds == null) {
            return null
        }

        // The last 3 digits are always 0, so we can ignore them

        val milliseconds = TimeUnit.MICROSECONDS.toMillis(microseconds)
        return Instant.ofEpochMilli(milliseconds)
    }
}
