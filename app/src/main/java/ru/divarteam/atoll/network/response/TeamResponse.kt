package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TeamResponse(
    @SerializedName("int_id") val teamId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("users") val users: List<UserResponse>?
)