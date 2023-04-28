package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RatingResponse(
    @SerializedName("oid") val objectId: String?,
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("place") val place: Int,
    @SerializedName("team_int_id") val teamId: Int?
)