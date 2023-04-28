package ru.divarteam.atoll.ui.events

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import ru.divarteam.atoll.databinding.FragmentEventsBinding
import ru.divarteam.atoll.epoxy.eventPicker
import ru.divarteam.atoll.network.response.EventResponse
import ru.divarteam.atoll.ui.main.MainActivity
import ru.divarteam.atoll.utils.fromDefaultFormatToRuFormatString
import ru.myrosmol.conductor.network.RetrofitService
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : Fragment() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var retrofitService: RetrofitService

    private lateinit var binding: FragmentEventsBinding
    private val onEventClickListener = object : OnEventClickListener {
        override fun onEventClick(eventId: Int) {
            findNavController().navigate(
                EventsFragmentDirections.actionEventsFragmentToEventFragment().apply {
                    this.eventId = eventId
                }
            )
        }
    }

    private val _eventsList: MutableLiveData<List<EventResponse>> = MutableLiveData(arrayListOf())
    val eventsList: LiveData<List<EventResponse>>
        get() = _eventsList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEventsBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        eventsList.observe(viewLifecycleOwner) {
            binding.eventsRecycler.withModels {
                it.forEach {
                    eventPicker {
                        id(it.id)
                        title(it.title)
                        subtitle(it.description)
                        eventId(it.id ?: 0)
                        onEventClickListener(onEventClickListener)
                        dateTime(
                            "${it.startDateTime?.fromDefaultFormatToRuFormatString()} - " +
                                    "${it.endDateTime?.fromDefaultFormatToRuFormatString()}"
                        )
                    }
                }
            }
        }

        loadEvents()
    }

    fun loadEvents() {
        showLoading()
        retrofitService.getAllEvents(preferenceRepository.userToken) { response, code ->
            if (code == 200 && response != null) {
                _eventsList.postValue(response)
            } else if (code == 403) {
                preferenceRepository.userToken = ""
                startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            } else
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