package com.ekorn.port.websocket

import com.ekorn.configuration.AppProperties
import com.ekorn.port.websocket.builder.SubscribeRequestBuilder
import com.fasterxml.jackson.databind.JsonNode
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.springframework.stereotype.Component
import java.util.concurrent.CancellationException

@Component
class WebSocketClient(
    private val client: HttpClient,
    private val app: AppProperties,
    private val subscribeRequestBuilder: SubscribeRequestBuilder,
    private val webSocketScope: CoroutineScope
) {
    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun start() = webSocketScope.launch {
        while(true) {
            val session = client.webSocketSession(urlString = app.websocket.url)
            logger.info { "Connected WebSocket to ${app.websocket.url}"}

            sendMarkets(session)

            val consumer = webSocketScope.launch { consume(session) }

            waitDisconnect(session, consumer)
        }
    }

    suspend fun sendMarkets(session: DefaultClientWebSocketSession) {

        logger.info { "Sending market subscriptions: ${app.markets}" }

        val markets = app.markets
            .map { market -> subscribeRequestBuilder.build(market) }

        for (market in markets) {
            session.sendSerialized(market)
        }
    }

    suspend fun consume(session: DefaultClientWebSocketSession) {
        while (true) {
            try {
                val message = session.receiveDeserialized<JsonNode>()
                logger.info { "Received event: $message" }
            } catch (_: CancellationException) {
                logger.info { "Shutting down WebSocket consumer gracefully" }
                return
            } catch (e: Exception) {
                logger.error(e) { "Error occurred while consuming a WebSocket message" }
            }
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
