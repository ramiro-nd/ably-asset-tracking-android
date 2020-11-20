package com.ably.tracking.publisher

import android.content.Context

internal data class AssetPublisherBuilder(
    val ablyConfiguration: AblyConfiguration? = null,
    val mapConfiguration: MapConfiguration? = null,
    val logConfiguration: LogConfiguration? = null,
    val batteryConfiguration: BatteryConfiguration? = null,
    val assetMetadataJson: String? = null,
    val tripMetadataJson: String? = null,
    val locationUpdatedListener: LocationUpdatedListener? = null,
    val androidContext: Context? = null,
    val trackingId: String? = null,
    val destination: String? = null,
    val vehicleType: String? = null
) : AssetPublisher.Builder {

    override fun ablyConfig(configuration: AblyConfiguration): AssetPublisher.Builder =
        this.copy(ablyConfiguration = configuration)

    override fun mapConfig(configuration: MapConfiguration): AssetPublisher.Builder =
        this.copy(mapConfiguration = configuration)

    override fun logConfig(configuration: LogConfiguration): AssetPublisher.Builder =
        this.copy(logConfiguration = configuration)

    override fun batteryConfig(configuration: BatteryConfiguration): AssetPublisher.Builder =
        this.copy(batteryConfiguration = configuration)

    override fun assetMetadataJson(metadataJsonString: String): AssetPublisher.Builder =
        this.copy(assetMetadataJson = metadataJsonString)

    override fun tripMetadataJson(metadataJsonString: String): AssetPublisher.Builder =
        this.copy(tripMetadataJson = metadataJsonString)

    override fun locationUpdatedListener(listener: LocationUpdatedListener): AssetPublisher.Builder =
        this.copy(locationUpdatedListener = listener)

    override fun androidContext(context: Context): AssetPublisher.Builder =
        this.copy(androidContext = context)

    override fun delivery(
        trackingId: String,
        destination: String,
        vehicleType: String
    ): AssetPublisher.Builder =
        this.copy(trackingId = trackingId, destination = destination, vehicleType = vehicleType)

    override fun start(): AssetPublisher {
        if (isMissingRequiredFields()) {
            throw BuilderConfigurationIncompleteException()
        }
        // All below fields are required and above code checks if they are nulls, so using !! should be safe from NPE
        return DefaultAssetPublisher(
            ablyConfiguration!!,
            mapConfiguration!!,
            trackingId!!,
            locationUpdatedListener!!,
            androidContext!!
        )
    }

    // TODO - define which fields are required and which are optional (for now: all are required)
    private fun isMissingRequiredFields() =
        ablyConfiguration == null ||
            mapConfiguration == null ||
            logConfiguration == null ||
            batteryConfiguration == null ||
            assetMetadataJson == null ||
            tripMetadataJson == null ||
            locationUpdatedListener == null ||
            androidContext == null ||
            trackingId == null ||
            destination == null ||
            vehicleType == null
}
