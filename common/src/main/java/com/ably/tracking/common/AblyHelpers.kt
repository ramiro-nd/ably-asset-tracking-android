package com.ably.tracking.common

import com.ably.tracking.ConnectionException
import com.ably.tracking.ErrorInformation
import com.ably.tracking.connection.Authentication
import com.ably.tracking.connection.TokenParams
import com.ably.tracking.connection.TokenRequest
import com.google.gson.Gson
import io.ably.lib.realtime.ChannelState
import io.ably.lib.rest.Auth
import io.ably.lib.types.ClientOptions

/**
 * Extension converting Ably Realtime connection state to the equivalent [ConnectionState] API presented to users of
 * the Ably Asset Tracking SDKs.
 */
fun io.ably.lib.realtime.ConnectionState.toTracking(): ConnectionState = when (this) {
    io.ably.lib.realtime.ConnectionState.initialized -> ConnectionState.OFFLINE
    io.ably.lib.realtime.ConnectionState.connecting -> ConnectionState.OFFLINE
    io.ably.lib.realtime.ConnectionState.connected -> ConnectionState.ONLINE
    io.ably.lib.realtime.ConnectionState.disconnected -> ConnectionState.OFFLINE
    io.ably.lib.realtime.ConnectionState.suspended -> ConnectionState.OFFLINE
    io.ably.lib.realtime.ConnectionState.closing -> ConnectionState.OFFLINE
    io.ably.lib.realtime.ConnectionState.closed -> ConnectionState.OFFLINE
    io.ably.lib.realtime.ConnectionState.failed -> ConnectionState.FAILED
}

/**
 * Extension converting Ably error info objects to the equivalent [ErrorInformation] API presented to users of the Ably
 * Asset Tracking SDKs.
 *
 * The `requestId` field is yet to be implemented by ably-java, however even once it is available then the chances are
 * that it'll still not be exposed through to users of the Ably Asset Tracking SDKs in order to keep things simple.
 */
fun io.ably.lib.types.ErrorInfo.toTracking() =
    ErrorInformation(
        this.code,
        this.statusCode,
        this.message,
        this.href, // may be null
        null // yet to be implemented by ably-java
    )

/**
 * Extension converting Ably error info objects to the equivalent [ConnectionException] API presented to users of the Ably
 * Asset Tracking SDKs.
 *
 * The `requestId` field is yet to be implemented by ably-java, however even once it is available then the chances are
 * that it'll still not be exposed through to users of the Ably Asset Tracking SDKs in order to keep things simple.
 */
fun io.ably.lib.types.ErrorInfo.toTrackingException() =
    ConnectionException(
        ErrorInformation(
            this.code,
            this.statusCode,
            this.message,
            this.href, // may be null
            null // yet to be implemented by ably-java
        )
    )

/**
 * Extension converting Ably Realtime connection state change events to the equivalent [ConnectionStateChange] API
 * presented to users of the Ably Asset Tracking SDKs.
 *
 * We are intentionally not passing on the `event` or `retryIn` fields. Our position on this may change in future, but
 * they are omitted for now to keep things simple.
 */
fun io.ably.lib.realtime.ConnectionStateListener.ConnectionStateChange.toTracking() =
    ConnectionStateChange(
        this.current.toTracking(),
        this.previous.toTracking(),
        this.reason?.toTracking()
    )

/**
 * Extension vending Ably client library ClientOptions from an [Authentication] instance.
 */
val Authentication.clientOptions: ClientOptions
    get() = ClientOptions().apply {
        clientId = this@clientOptions.clientId

        this@clientOptions.tokenRequestCallback?.let { tokenRequestCallback ->
            authCallback = Auth.TokenCallback {
                tokenRequestCallback(it.toTracking()).toAuth()
            }
        }

        this@clientOptions.basicApiKey?.let { basicApiKey ->
            key = basicApiKey
        }
    }

/**
 * Extension converting Ably Realtime auth token params to the equivalent [TokenParams] API
 * presented to users of the Ably Asset Tracking SDKs.
 */
fun Auth.TokenParams.toTracking(): TokenParams =
    object : TokenParams {
        override val ttl: Long = this@toTracking.ttl
        override val capability: String = this@toTracking.capability
        override val clientId: String = this@toTracking.clientId
        override val timestamp: Long = this@toTracking.timestamp
    }

/**
 * Extension converting Asset Tracking SDK [TokenRequest] to the equivalent [Auth.TokenRequest] from Ably.
 */
fun TokenRequest.toAuth(): Auth.TokenRequest =
    Auth.TokenRequest().apply {
        ttl = this@toAuth.ttl
        capability = this@toAuth.capability
        clientId = this@toAuth.clientId
        timestamp = this@toAuth.timestamp
        keyName = this@toAuth.keyName
        nonce = this@toAuth.nonce
        mac = this@toAuth.mac
    }

/**
 * Extension converting Ably Realtime presence message to the equivalent [PresenceMessage] API
 * presented to users of the Ably Asset Tracking SDKs.
 */
fun io.ably.lib.types.PresenceMessage.toTracking(gson: Gson) =
    PresenceMessage(
        this.action.toTracking(),
        this.getPresenceData(gson),
        this.clientId
    )

fun io.ably.lib.types.PresenceMessage.Action.toTracking(): PresenceAction = when (this) {
    io.ably.lib.types.PresenceMessage.Action.present -> PresenceAction.PRESENT_OR_ENTER
    io.ably.lib.types.PresenceMessage.Action.enter -> PresenceAction.PRESENT_OR_ENTER
    io.ably.lib.types.PresenceMessage.Action.update -> PresenceAction.UPDATE
    io.ably.lib.types.PresenceMessage.Action.leave -> PresenceAction.LEAVE_OR_ABSENT
    io.ably.lib.types.PresenceMessage.Action.absent -> PresenceAction.LEAVE_OR_ABSENT
}

/**
 * Extension converting Ably Realtime channel state to the equivalent [ConnectionState] API presented to users of
 * the Ably Asset Tracking SDKs.
 */
fun ChannelState.toTracking() = when (this) {
    ChannelState.initialized -> ConnectionState.OFFLINE
    ChannelState.attaching -> ConnectionState.OFFLINE
    ChannelState.attached -> ConnectionState.ONLINE
    ChannelState.detaching -> ConnectionState.OFFLINE
    ChannelState.detached -> ConnectionState.OFFLINE
    ChannelState.failed -> ConnectionState.FAILED
    ChannelState.suspended -> ConnectionState.OFFLINE
}

/**
 * Extension converting Ably Realtime channel state change events to the equivalent [ConnectionStateChange] API
 * presented to users of the Ably Asset Tracking SDKs.
 */
fun io.ably.lib.realtime.ChannelStateListener.ChannelStateChange.toTracking() =
    ConnectionStateChange(
        this.current.toTracking(),
        this.previous.toTracking(),
        this.reason.toTracking()
    )
