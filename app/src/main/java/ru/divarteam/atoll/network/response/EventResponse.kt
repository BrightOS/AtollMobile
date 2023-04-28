package ru.divarteam.atoll.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class EventResponse(
    @SerializedName("int_id") val id: Int?,
    @SerializedName("created") val created: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("team_ods") val teamOds: List<String>?,
    @SerializedName("author_oid") val authorOId: String?,
    @SerializedName("start_dt") val startDateTime: String?,
    @SerializedName("end_dt") val endDateTime: String?,
    @SerializedName("timeline") val timeline: List<TimelineResponse>?,
    @SerializedName("ratings") val rating: List<RatingResponse>?,
    @SerializedName("teams") val teams: List<TeamResponse>?
)