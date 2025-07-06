package com.ekorn.adapter.websocket.bitstamp

import com.ekorn.adapter.websocket.bitstamp.builder.SubscribeRequestBuilder
import com.ekorn.adapter.websocket.bitstamp.model.BitstampEvent
import com.ekorn.configuration.AppProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.springframework.stereotype.Component
import java.util.concurrent.CancellationException

/**
 * Manages the lifecycle of a WebSocket client connection to the Bitstamp API.
 */
@Component
class WebSocketClient(
    private val client: HttpClient,
    private val app: AppProperties,
    private val webSocketScope: CoroutineScope,
    private val subscribeRequestBuilder: SubscribeRequestBuilder
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun start(
        markets: List<String>,
        consume: suspend (BitstampEvent) -> Unit = {},
    ) = webSocketScope.launch {
        while(true) {
            val session = client.webSocketSession(urlString = app.websocket.url)
            logger.info { "Connected WebSocket to ${app.websocket.url}"}

            val consumer = webSocketScope.launch {

                subscribe(session, markets)

                while (true) {
                    try {
                        val message = session.receiveDeserialized<BitstampEvent>()
                        consume(message)
                    } catch (_: CancellationException) {
                        logger.info { "Shutting down WebSocket consumer gracefully" }
                        break
                    } catch (e: Exception) {
                        logger.error(e) { "Error occurred while consuming a WebSocket message" }
                    }
                }
            }

            waitDisconnect(session, consumer)
        }
    }

    suspend fun subscribe(
        session: DefaultClientWebSocketSession,
        markets: List<String>
    ) {
        logger.info { "Sending market subscriptions: $markets" }

        val subscriptions = markets.map { market -> "live_trades_$market" }
            .map { channel -> subscribeRequestBuilder.build(channel) }

        for (subscription in subscriptions) {
            session.sendSerialized(subscription)
        }
    }

    suspend fun waitDisconnect(
        session: DefaultClientWebSocketSession,
        consumer: Job
    ) {
        select {
            consumer.onJoin {
                logger.info { "Consumer finished" }
            }
            session.closeReason.onAwait { closeReason ->
                if (closeReason != null) {
                    logger.info { "Disconnected gracefully: ${closeReason.message}" }
                } else {
                    logger.info { "Disconnected ungracefully" }
                }
            }
        }

        consumer.cancelAndJoin()
        session.cancel("Requested cancellation of the WebSocket session")
    }
}
