package com.example.kode.feature.details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.load
import com.example.kode.R
import com.example.kode.databinding.FragmentWorkerDetailsBinding
import com.example.kode.data.model.Worker
import com.example.kode.feature.details.common.CalculateAge
import com.example.kode.feature.details.common.FormatDateAndPhone
import java.time.LocalDate

class WorkerDetailsFragment : Fragment() {

    private lateinit var binding : FragmentWorkerDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getSerializable("WORKER") as Worker
        bindUI(data)

        binding.backButtonView.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    private fun bindUI(data: Worker) {

        val fullName = "${data.firstName} ${data.lastName}"
        val dataFormatter = FormatDateAndPhone()

        with(binding) {
            workerAvatar.load(data.imageUrl) {
                error(R.drawable.goose_plug)
            }
            workerName.text = fullName
            workerPost.text = data.position
            dateBirthday.text = dataFormatter.formatDate(LocalDate.parse(data.birthday))
            phoneNumber.text = dataFormatter.formatPhone(data.phone)
            userTag.text = data.userTag
            textViewAge.text = CalculateAge().calculate(LocalDate.parse(data.birthday))
        }
    }
}