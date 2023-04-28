package ru.divarteam.atoll.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.atoll.data.repository.PreferenceRepository
import ru.divarteam.atoll.databinding.FragmentEventBinding
import ru.divarteam.atoll.epoxy.EventTimelineEpoxyModel
import ru.divarteam.atoll.epoxy.eventHeader
import ru.divarteam.atoll.epoxy.eventMyTeam
import ru.divarteam.atoll.epoxy.eventRatingTopTeam
import ru.divarteam.atoll.epoxy.eventRatingCommonTeam
import ru.divarteam.atoll.epoxy.eventTimeline
import ru.divarteam.atoll.network.response.EventResponse
import ru.divarteam.atoll.utils.fromDefaultStringToDate
import ru.myrosmol.conductor.network.RetrofitService
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var retrofitService: RetrofitService

    private val eventArgs by navArgs<EventFragmentArgs>()

    private lateinit var binding: FragmentEventBinding
    private val onEventHeaderClickListener = object : OnEventHeaderClickListener {
        override fun onParticipateClick() {
            showLoading()
            retrofitService.joinEvent(
                preferenceRepository.userToken,
                currentEvent.value?.id!!
            ) { response, code ->
                if (code == 200 && response != null) {
                    retrofitService.getEventById(
                        preferenceRepository.userToken,
                        currentEvent.value?.id!!
                    ) { innerResponse, innerCode ->
                        if (innerCode == 200 && innerResponse != null) {
                            _currentEvent.postValue(innerResponse)
                        } else
                            somethingWentWrong()
                        hideLoading()
                    }
                } else {
                    somethingWentWrong()
                    hideLoading()
                }
            }
        }

        override fun onSendFeedbackClick() {
            findNavController().navigate(
                EventFragmentDirections.actionEventFragmentToSendFeedbackBottomSheet().apply {
                    eventId = currentEvent.value?.id!!
                })
        }
    }
    private val onInviteClickListener = object : OnInviteClickListener {
        override fun onInviteClick(eventId: Int, teamId: Int) {
            findNavController().navigate(
                EventFragmentDirections.actionEventFragmentToUsersSearchFragment().apply {
                    this.eventId = eventId
                    this.teamId = teamId
                }
            )
        }
    }
    private val _currentEvent = MutableLiveData<EventResponse>()
    val currentEvent: LiveData<EventResponse>
        get() = _currentEvent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEventBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentEpoxyIndex = 0

        currentEvent.observe(viewLifecycleOwner) {

            val myTeam = it.teams?.find {
                it.users?.map { it.intId }
                    ?.contains(preferenceRepository.userId) == true
            }

            binding.eventsRecycler.withModels {

                eventHeader {
                    id(currentEpoxyIndex++)
                    title(it.title)
                    description(it.description)
                    canParticipate(myTeam == null && preferenceRepository.userRole == "sportsman")
                    canLeaveFeedback(myTeam != null && it.endDateTime!!.fromDefaultStringToDate()!!.time < Calendar.getInstance().timeInMillis)
                    onEventHeaderClickListener(onEventHeaderClickListener)
                }

                it.timeline?.forEachIndexed { i, e ->
                    eventTimeline {
                        id(currentEpoxyIndex++)
                        time(e.dateTime)
                        text(e.text)
                        state(
                            when (i) {
                                0 -> EventTimelineEpoxyModel.TimelineItemState.FIRST
                                it.timeline.size - 1 -> EventTimelineEpoxyModel.TimelineItemState.LAST
                                else -> EventTimelineEpoxyModel.TimelineItemState.COMMON
                            }
                        )
                    }
                }

                if (myTeam != null)
                    eventMyTeam {
                        id(currentEpoxyIndex++)
                        teamResponse(myTeam)
                        eventId(it.id!!)
                        onInviteClickListener(onInviteClickListener)
                    }

                it.rating?.forEachIndexed { i, e ->
                    when (i) {
                        in 0..2 ->
                            eventRatingTopTeam {
                                id(currentEpoxyIndex++)
                                ratingResponse(e)
                                it.teams?.first { it.teamId == e.teamId }.let {
                                    if (it != null)
                                        teamResponse(it)
                                }
                            }

                        in 3..8 ->
                            eventRatingCommonTeam {
                                id(currentEpoxyIndex++)
                                ratingResponse(e)
                                teamResponse(it.teams?.first { it.teamId == e.teamId })
                            }
                    }
                }
            }
        }

        loadEvent(eventArgs.eventId)

        binding.appbar.startButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun loadEvent(eventId: Int) {
        showLoading()
        retrofitService.getEventById(preferenceRepository.userToken, eventId) { response, code ->
            if (code == 200 && response != null)
                _currentEvent.postValue(response)
            else
                somethingWentWrong()
            hideLoading()
        }
    }

    private fun showLoading() {
        binding.loading.root.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.root.visibility = View.GONE
    }

    private fun somethingWentWrong() {
        Toast.makeText(
            context,
            "Что-то пошло не так...",
            Toast.LENGTH_SHORT
        ).show()
    }
}