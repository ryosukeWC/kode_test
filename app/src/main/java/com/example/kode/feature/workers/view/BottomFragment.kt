package com.example.kode.feature.workers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kode.databinding.FragmentBottomBinding
import com.example.kode.feature.workers.common.OnRadioButtonClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomBinding

    private var listener: OnRadioButtonClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.radioButtonAlphabet.setOnCheckedChangeListener { _, _ ->
            listener?.onClickAlphabet()
            dismiss()
        }

        binding.radioButtonBirthday.setOnCheckedChangeListener { _, _ ->
            listener?.onClickBirthday()
            dismiss()
        }
    }

    fun setListener(listener: OnRadioButtonClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}