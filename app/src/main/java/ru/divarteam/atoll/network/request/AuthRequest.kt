package ru.divarteam.atoll.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AuthRequest (
    @SerializedName("mail") val mail: String?,
    @SerializedName("code") val code: Int?
)