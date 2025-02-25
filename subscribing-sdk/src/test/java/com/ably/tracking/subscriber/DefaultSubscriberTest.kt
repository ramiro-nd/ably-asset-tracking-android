package com.ably.tracking.subscriber

import com.ably.tracking.ConnectionException
import com.ably.tracking.common.Ably
import com.ably.tracking.test.common.mockConnectSuccess
import com.ably.tracking.test.common.mockDisconnectSuccess
import com.ably.tracking.test.common.mockSubscribeToPresenceError
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.UUID

class DefaultSubscriberTest {
    private val ably = mockk<Ably>(relaxed = true)
    private val trackableId = UUID.randomUUID().toString()
    private val subscriber = DefaultSubscriber(ably, null, trackableId)

    @Test(expected = ConnectionException::class)
    fun `should return an error when starting the subscriber with subscribing to presence error`() {
        // given
        ably.mockConnectSuccess(trackableId)
        ably.mockDisconnectSuccess(trackableId)
        ably.mockSubscribeToPresenceError(trackableId)

        // when
        runBlocking {
            subscriber.start()
        }

        // then
    }

    @Test()
    fun `should disconnect from the channel when starting the subscriber with subscribing to presence error`() {
        // given
        ably.mockConnectSuccess(trackableId)
        ably.mockDisconnectSuccess(trackableId)
        ably.mockSubscribeToPresenceError(trackableId)

        // when
        runBlocking {
            try {
                subscriber.start()
            } catch (exception: ConnectionException) {
                // ignoring exception in this test
            }
        }

        // then
        verify(exactly = 1) {
            ably.disconnect(trackableId, any(), any())
        }
    }
}
