package ru.divarteam.atoll.ui.profile

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
import ru.divarteam.atoll.databinding.FragmentProfileBinding
import ru.divarteam.atoll.network.response.UserResponse
import ru.myrosmol.conductor.network.RetrofitService
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var retrofitService: RetrofitService

    private lateinit var binding: FragmentProfileBinding

    private val _currentUser = MutableLiveData<UserResponse>()
    val currentUser: LiveData<UserResponse>
        get() = _currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentUser.observe(viewLifecycleOwner) {
            if (it == null)
                binding.loading.root.visibility = View.VISIBLE
            else {
                binding.fullname.editText?.setText(it.fullname)
                binding.birthDate.editText?.setText(it.birthDate)
                binding.telegram.editText?.setText(it.telegramUsername)
                binding.vk.editText?.setText(it.vkId)
                binding.description.editText?.setText(it.description)
                binding.roleTv.setText(when (it.roles!![0]) {
                    "admin" -> "Администратор"
                    "representative" -> "Представитель"
                    "partner" -> "Партнёр"
                    else -> "Спортсмен"
                })

                binding.loading.root.visibility = View.GONE
            }
        }

        binding.confirmChanges.setOnClickListener {
            updateUser()
        }

        loadUser()
    }

    fun updateUser() {
        binding.loading.root.visibility = View.VISIBLE

        retrofitService.updateMe(
            token = preferenceRepository.userToken,
            fullname = binding.fullname.editText?.text.toString(),
            description = binding.description.editText?.text.toString(),
            birthDate = null,
            telegramUsername = binding.telegram.editText?.text.toString(),
            vk = binding.vk.editText?.text.toString(),
        ) { response, code ->
            if (code == 200 && response != null) {
                _currentUser.postValue(response)
                Toast.makeText(
                    context,
                    "Изменения успешно применены!",
                    Toast.LENGTH_SHORT
                ).show()
            } else
                somethingWentWrong()
            binding.loading.root.visibility = View.GONE
        }
    }

    fun loadUser(userId: Int = preferenceRepository.userId) {
        binding.loading.root.visibility = View.VISIBLE
        if (userId == preferenceRepository.userId)
            retrofitService.getMe(preferenceRepository.userToken) { response, code ->
                if (code == 200 && response != null)
                    _currentUser.postValue(response)
                else
                    somethingWentWrong()
            }
        else
            Toast.makeText(
                context,
                "Что......",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun somethingWentWrong() {
        Toast.makeText(
            context,
            "Что-то пошло не так...",
            Toast.LENGTH_SHORT
        ).show()
    }
}