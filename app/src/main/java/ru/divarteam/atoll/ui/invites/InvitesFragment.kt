package ru.divarteam.atoll.ui.invites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.atoll.data.repository.PreferenceRepository
import ru.divarteam.atoll.databinding.FragmentInvitesBinding
import ru.divarteam.atoll.epoxy.invitation
import ru.divarteam.atoll.epoxy.noInvites
import ru.divarteam.atoll.network.response.EventResponse
import ru.divarteam.atoll.network.response.InviteResponse
import ru.myrosmol.conductor.network.RetrofitService
import javax.inject.Inject

@AndroidEntryPoint
class InvitesFragment : Fragment() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var retrofitService: RetrofitService

    private lateinit var binding: FragmentInvitesBinding
    private val onInvitationClickListener = object : OnInvitationClickListener {
        override fun onAcceptClicked(teamId: Int) {
            showLoading()
            retrofitService.acceptInvite(preferenceRepository.userToken, teamId) { response, code ->
                if (code == 200 && response != null) {
                    if (response.isDone == true)
                        Toast.makeText(
                            context,
                            "Приглашение принято",
                            Toast.LENGTH_SHORT
                        ).show()
                    loadInvitations()
                } else {
                    somethingWentWrong()
                    hideLoading()
                }
            }
        }

        override fun onDenyClicked(teamId: Int) {
            // TODO: Если появится метод в API
        }
    }
    private val _invitesList: MutableLiveData<List<InviteResponse>> = MutableLiveData(arrayListOf())
    val invitesList: LiveData<List<InviteResponse>>
        get() = _invitesList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentInvitesBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        invitesList.observe(viewLifecycleOwner) {
            binding.invitesRecycler.withModels {
                if (it.isNotEmpty())
                    it.forEach {
                        invitation {
                            id(it.invitationId)
                            teamResponse(it.team)
                            onInvitationClickListener(onInvitationClickListener)
                        }
                    }
                else
                    noInvites {
                        id(0)
                    }
            }
        }

        loadInvitations()
    }

    private fun loadInvitations() {
        showLoading()
        retrofitService.myInvites(preferenceRepository.userToken) { response, code ->
            if (code == 200 && response != null)
                _invitesList.postValue(response)
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