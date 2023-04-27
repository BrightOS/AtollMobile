package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SuccessfulResponse(
    @SerializedName("is_done") val isDone: Boolean?
)
