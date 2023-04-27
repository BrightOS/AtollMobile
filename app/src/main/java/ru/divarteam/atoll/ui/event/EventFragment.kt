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
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.atoll.data.repository.PreferenceRepository
import ru.divarteam.atoll.databinding.FragmentEventBinding
import ru.divarteam.atoll.epoxy.EventTimelineEpoxyModel
import ru.divarteam.atoll.epoxy.eventHeader
import ru.divarteam.atoll.epoxy.eventTimeline
import ru.divarteam.atoll.network.response.EventResponse
import ru.myrosmol.conductor.network.RetrofitService
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var retrofitService: RetrofitService

    private lateinit var binding: FragmentEventBinding
    private val onParticipateClickListener = object : OnParticipateClickListener {
        override fun onParticipateClick() {
            // TODO: Принимаем участие
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

            binding.eventsRecycler.withModels {

                eventHeader {
                    id(currentEpoxyIndex++)
                    title(it.title)
                    description(it.description)
                    onParticipateClickListener(onParticipateClickListener)
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
            }
        }

        loadEvent(9)

        binding.appbar.startButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun loadEvent(eventId: Int) {
        retrofitService.getEventById(preferenceRepository.userToken, eventId) { response, code ->
            if (code == 200 && response != null)
                _currentEvent.postValue(response)
            else
                somethingWentWrong()
        }
    }

    private fun somethingWentWrong() {
        Toast.makeText(
            context,
            "Что-то пошло не так...",
            Toast.LENGTH_SHORT
        ).show()
    }
}