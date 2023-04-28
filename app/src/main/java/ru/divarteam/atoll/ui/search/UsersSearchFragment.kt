package ru.divarteam.atoll.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import ru.divarteam.atoll.epoxy.userPicker
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.divarteam.atoll.data.repository.PreferenceRepository
import ru.divarteam.atoll.databinding.FragmentUsersSearchBinding
import ru.divarteam.atoll.network.response.UserResponse
import ru.myrosmol.conductor.network.RetrofitService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class UsersSearchFragment : Fragment() {

    @Inject
    lateinit var retrofitService: RetrofitService

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var binding: FragmentUsersSearchBinding
    private val args by navArgs<UsersSearchFragmentArgs>()
    private val onUserSelectedListener = object : OnUserSelectedListener {
        override fun onUserSelect(userResponse: UserResponse) {
            showLoading()
            retrofitService.sendTeamInvite(
                preferenceRepository.userToken,
                args.teamId,
                userResponse.intId!!
            ) { response, code ->
                if (code == 200 && response != null) {
                    Toast.makeText(
                        context,
                        "${userResponse.fullname} был приглашён в команду",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                } else {
                    somethingWentWrong()
                    hideLoading()
                }
            }
        }
    }

    private val _queriedList = MutableLiveData(listOf<UserResponse>())
    val queriedList: LiveData<List<UserResponse>>
        get() = _queriedList

    private val _fullList = MutableLiveData(listOf<UserResponse>())
    val fullList: LiveData<List<UserResponse>>
        get() = _fullList

    private lateinit var compositeDisposable: CompositeDisposable
    private val searchSubject = PublishSubject.create<String>()
    var searchSubjectInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentUsersSearchBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fullList.observe(viewLifecycleOwner) {
            if (!searchSubjectInitialized)
                setupSearchObject()
        }

        queriedList.observe(viewLifecycleOwner) {
            binding.usersRecycler.withModels {
                it.forEach {
                    userPicker {
                        id(it.intId)
                        userResponse(it)
                        onUserSelectedListener(onUserSelectedListener)
                    }
                }
            }
        }

        binding.searchBar.editText?.doAfterTextChanged {
            searchSubject.onNext(it.toString())
        }

        binding.appbar.startButton.setOnClickListener {
            findNavController().popBackStack()
        }

        loadUsersList()
    }

    fun loadUsersList() {
        showLoading()
        retrofitService.searchUsersForInvitation(
            preferenceRepository.userToken,
            args.eventId
        ) { response, code ->
            if (code == 200 && response != null) {
                _fullList.postValue(response)
                _queriedList.postValue(response)
            } else
                somethingWentWrong()
            hideLoading()
        }
    }

    fun setupSearchObject() {
        searchSubject
            .map { query -> query.trim() }
            .distinctUntilChanged()
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.computation())
            .switchMapSingle { query ->
                if (query.isNotBlank()) {
                    Single.just(fullList.value?.filter {
                        (it.fullname ?: "").uppercase().replace("-", "").replace(" ", "")
                            .contains(query.uppercase().replace("-", "").replace(" ", ""))
                    })
                } else
                    Single.just(fullList.value)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _queriedList.postValue(it)
            }
            .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
    }

    private fun somethingWentWrong() {
        Toast.makeText(
            context,
            "Что-то пошло не так...",
            Toast.LENGTH_SHORT
        ).show()
    }

}