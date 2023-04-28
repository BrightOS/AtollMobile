package ru.divarteam.atoll.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.atoll.data.repository.PreferenceRepository
import ru.divarteam.atoll.databinding.BottomSheetFeedbackBinding
import ru.myrosmol.conductor.network.RetrofitService
import javax.inject.Inject

@AndroidEntryPoint
class SendFeedbackBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var retrofitService: RetrofitService

    private lateinit var binding: BottomSheetFeedbackBinding
    private val args by navArgs<SendFeedbackBottomSheetArgs>()
    private var selectedRate = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = BottomSheetFeedbackBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rate.setOnRatingBarChangeListener { ratingBar, fl, b ->
            selectedRate = fl.toInt()
            println(selectedRate)
        }

        binding.sendFeedback.setOnClickListener {
            sendFeedback()
        }
    }

    fun sendFeedback() {
        showLoading()
        retrofitService.sendFeedback(
            preferenceRepository.userToken,
            args.eventId,
            binding.feedbackField.editText?.text.toString(),
            selectedRate
        ) { response, code ->
            if (code == 200 && response != null) {
                dismiss()
            } else {
                somethingWentWrong()
                hideLoading()
            }
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