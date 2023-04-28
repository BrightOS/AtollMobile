package ru.myrosmol.conductor.network

import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.divarteam.atoll.data.repository.PreferenceRepository
import ru.divarteam.atoll.network.request.AuthRequest
import ru.divarteam.atoll.network.request.SendFeedbackRequest
import ru.divarteam.atoll.network.request.UpdateMeRequest
import ru.divarteam.atoll.network.response.EventResponse
import ru.divarteam.atoll.network.response.FeedbackResponse
import ru.divarteam.atoll.network.response.InviteResponse
import ru.divarteam.atoll.network.response.MailExistsResponse
import ru.divarteam.atoll.network.response.SuccessfulResponse
import ru.divarteam.atoll.network.response.UserResponse
import javax.inject.Inject

class RetrofitService(val restAPI: RestAPI) {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    fun auth(mail: String, code: Int, onResult: (response: UserResponse?, code: Int) -> Unit) =
        restAPI.auth(AuthRequest(mail, code)).enqueue(onResult)

    fun authSendCode(mail: String, onResult: (response: SuccessfulResponse?, code: Int) -> Unit) =
        restAPI.authSendCode(mail).enqueue(onResult)

    fun reg(mail: String, code: Int, onResult: (response: UserResponse?, code: Int) -> Unit) =
        restAPI.reg(AuthRequest(mail, code)).enqueue(onResult)

    fun regSendCode(mail: String, onResult: (response: SuccessfulResponse?, code: Int) -> Unit) =
        restAPI.regSendCode(mail).enqueue(onResult)

    fun getMe(token: String, onResult: (response: UserResponse?, code: Int) -> Unit) =
        restAPI.getMe(token).enqueue(onResult)

    fun myInvites(token: String, onResult: (response: List<InviteResponse>?, code: Int) -> Unit) =
        restAPI.myInvites(token).enqueue(onResult)

    fun acceptInvite(
        token: String,
        fromTeamId: Int,
        onResult: (response: SuccessfulResponse?, code: Int) -> Unit
    ) = restAPI.acceptInvite(token, fromTeamId).enqueue(onResult)

    fun mailExists(mail: String, onResult: (response: MailExistsResponse?, code: Int) -> Unit) =
        restAPI.mailExists(mail).enqueue(onResult)

    fun sendTeamInvite(token: String, fromTeamId: Int, toUserId: Int, onResult: (response: SuccessfulResponse?, code: Int) -> Unit) =
        restAPI.sendTeamInvite(token, fromTeamId, toUserId).enqueue(onResult)

    fun getAllEvents(token: String, onResult: (response: List<EventResponse>?, code: Int) -> Unit) =
        restAPI.getAllEvents(token).enqueue(onResult)

    fun getEventById(
        token: String,
        eventId: Int,
        onResult: (response: EventResponse?, code: Int) -> Unit
    ) = restAPI.getEventById(token, eventId).enqueue(onResult)

    fun joinEvent(
        token: String,
        eventId: Int,
        onResult: (response: EventResponse?, code: Int) -> Unit
    ) = restAPI.joinEvent(token, eventId).enqueue(onResult)

    fun sendFeedback(
        token: String,
        eventId: Int,
        text: String,
        rate: Int,
        onResult: (response: FeedbackResponse?, code: Int) -> Unit
    ) = restAPI.sendFeedback(token, SendFeedbackRequest(eventId, text, rate)).enqueue(onResult)

    fun searchUsersForInvitation(
        token: String,
        eventId: Int,
        onResult: (response: List<UserResponse>?, code: Int) -> Unit
    ) = restAPI.searchUsersForInvitation(token, eventId).enqueue(onResult)

    fun updateMe(
        token: String,
        fullname: String?,
        birthDate: String?,
        telegramUsername: String?,
        description: String?,
        vk: String?,
        onResult: (response: UserResponse?, code: Int) -> Unit
    ) = restAPI.updateMe(
        token,
        UpdateMeRequest(
            fullname, birthDate,
            telegramUsername, description,
            vk
        )
    ).enqueue(onResult)

    fun <T> Call<T>.enqueue(onResult: (response: T?, code: Int) -> Unit) =
        enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                onResult.invoke(response.body(), response.code())
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                t.printStackTrace()
            }
        })

}