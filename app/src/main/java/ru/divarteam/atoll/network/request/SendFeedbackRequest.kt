package ru.divarteam.atoll.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SendFeedbackRequest(
    @SerializedName("event_int_id") val eventId: Int,
    @SerializedName("text") val text: String,
    @SerializedName("rate") val rate: Int
)