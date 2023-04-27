package ru.divarteam.atoll.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateMeRequest(
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("birth_dt") val birthDate: String?,
    @SerializedName("tg_username") val telegramUsername: String?,
    @SerializedName("description") val description: String?
)