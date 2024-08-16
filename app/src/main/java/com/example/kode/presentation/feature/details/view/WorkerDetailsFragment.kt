package com.example.kode.presentation.feature.details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.load
import com.example.kode.R
import com.example.kode.databinding.FragmentWorkerDetailsBinding
import com.example.kode.model.Worker
import com.example.kode.presentation.feature.workers.viewmodel.WorkersViewModel

class WorkerDetailsFragment : Fragment() {

    private lateinit var binding : FragmentWorkerDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkerDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getSerializable("WORKER") as Worker
        bindUI(binding,data)

        binding.backButtonView.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    private fun bindUI(binding: FragmentWorkerDetailsBinding, data: Worker) {

        val fullName = "${data.firstName} ${data.lastName}"

        with(binding) {
            workerAvatar.load(data.imageUrl)
            workerName.text = fullName
            workerPost.text = data.position
            dateBirthday.text = data.birthday
            phoneNumber.text = data.phone
            userTag.text = data.userTag
        }
    }
}