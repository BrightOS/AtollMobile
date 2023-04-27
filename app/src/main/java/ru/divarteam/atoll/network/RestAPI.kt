package ru.myrosmol.conductor.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import ru.divarteam.atoll.network.request.AuthRequest
import ru.divarteam.atoll.network.request.UpdateMeRequest
import ru.divarteam.atoll.network.response.EventResponse
import ru.divarteam.atoll.network.response.MailExistsResponse
import ru.divarteam.atoll.network.response.SuccessfulResponse
import ru.divarteam.atoll.network.response.UserResponse

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

    // User methods

    @GET("user.mail_exists")
    fun mailExists(@Query("mail") mail: String): Call<MailExistsResponse>

    // Event methods

    @GET("event.all")
    fun getAllEvents(@Header("Authorization") token: String): Call<List<EventResponse>>

    @GET("event.get_by_int_id")
    fun getEventById(
        @Header("Authoriation") token: String,
        @Query("int_id") eventId: Int
    ): Call<EventResponse>
}