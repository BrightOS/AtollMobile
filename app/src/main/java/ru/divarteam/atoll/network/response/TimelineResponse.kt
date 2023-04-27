package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TimelineResponse(
    @SerializedName("dt") val dateTime: String?,
    @SerializedName("text") val text: String?
)