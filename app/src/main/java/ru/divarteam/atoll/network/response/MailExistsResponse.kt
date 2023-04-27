package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MailExistsResponse(
    @SerializedName("is_exists") val exists: Boolean?
)