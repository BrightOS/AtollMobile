package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class InviteResponse(
    @SerializedName("int_id") val invitationId: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("from_team") val team: TeamResponse?
)