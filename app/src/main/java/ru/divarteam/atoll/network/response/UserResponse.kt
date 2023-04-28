package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("mail") val mail: String?,
    @SerializedName("birth_dt") val birthDate: String?,
    @SerializedName("tg_username") val telegramUsername: String?,
    @SerializedName("tg_id") val telegramId: String?,
    @SerializedName("vk_id") val vkId: String?,
    @SerializedName("roles") val roles: List<String>?,
    @SerializedName("tokens") val tokens: List<String>?,
    @SerializedName("current_token") val token: String?,
    @SerializedName("is_captain") val isCaptain: Boolean
)