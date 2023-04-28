package ru.myrosmol.conductor.network

import androidx.annotation.Keep
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import ru.divarteam.atoll.network.request.AuthRequest
import ru.divarteam.atoll.network.request.SendFeedbackRequest
import ru.divarteam.atoll.network.request.UpdateMeRequest
import ru.divarteam.atoll.network.response.EventResponse
import ru.divarteam.atoll.network.response.FeedbackResponse
import ru.divarteam.atoll.network.response.InviteResponse
import ru.divarteam.atoll.network.response.MailExistsResponse
import ru.divarteam.atoll.network.response.SuccessfulResponse
import ru.divarteam.atoll.network.response.UserResponse

@Keep
interface RestAPI {

    // Auth methods

    @POST("auth")
    fun auth(@Body authRequest: AuthRequest): Call<UserResponse>

    @GET("auth.send_code")
    fun authSendCode(@Query("to_mail") toMail: String): Call<SuccessfulResponse>

    // Reg methods

    @POST("reg")
    fun reg(@Body authRequest: AuthRequest): Call<UserResponse>

    @GET("reg.send_code")
    fun regSendCode(@Query("to_mail") toMail: String): Call<SuccessfulResponse>

    // Current user methods

    @GET("me")
    fun getMe(@Header("Authorization") token: String): Call<UserResponse>

    @POST("me.update")
    fun updateMe(
        @Header("Authorization") token: String,
        @Body updateMeRequest: UpdateMeRequest
    ): Call<UserResponse>

    @GET("me.my_invites")
    fun myInvites(
        @Header("Authorization") token: String
    ): Call<List<InviteResponse>>

    @GET("me.accept_invite")
    fun acceptInvite(
        @Header("Authorization") token: String,
        @Query("from_team_int_id") fromTeamId: Int
    ): Call<SuccessfulResponse>

    // User methods

    @GET("user.mail_exists")
    fun mailExists(@Query("mail") mail: String): Call<MailExistsResponse>

    @GET("user.send_team_invite")
    fun sendTeamInvite(
        @Header("Authorization") token: String,
        @Query("from_team_int_id") fromTeamId: Int,
        @Query("to_user_int_id") toUserId: Int
    ): Call<SuccessfulResponse>

    // Event methods

    @GET("event.all")
    fun getAllEvents(@Header("Authorization") token: String): Call<List<EventResponse>>

    @GET("event.get_by_int_id")
    fun getEventById(
        @Header("Authorization") token: String,
        @Query("int_id") eventId: Int
    ): Call<EventResponse>

    @GET("event.join")
    fun joinEvent(
        @Header("Authorization") token: String,
        @Query("event_int_id") eventId: Int
    ): Call<EventResponse>

    @GET("event.users_for_inventation")
    fun searchUsersForInvitation(
        @Header("Authorization") token: String,
        @Query("event_int_id") eventId: Int
    ): Call<List<UserResponse>>

    @POST("event.send_feedback")
    fun sendFeedback(
        @Header("Authorization") token: String,
        @Body body: SendFeedbackRequest
    ): Call<FeedbackResponse>
}