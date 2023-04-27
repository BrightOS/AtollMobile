package ru.divarteam.atoll.network.response

import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("oid") val objectId: String?,
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("event_oid") val eventObjectId: String?,
    @SerializedName("place") val place: Int,
    @SerializedName("team_oid") val teamObjectId: String?
)