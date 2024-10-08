package com.example.kode.feature.workers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.kode.R
import com.example.kode.databinding.ItemWorkerBinding
import com.example.kode.data.model.Worker

class WorkersAdapter : ListAdapter<Worker, WorkersVH>(WorkersDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkersVH {
        val binding = ItemWorkerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WorkersVH(binding)
    }

    override fun onBindViewHolder(holder: WorkersVH, position: Int) {

        val item = getItem(position)
        holder.bindItem(item)

        val bundle = bundleOf("WORKER" to item)

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_workersFragment_to_workerDetailsFragment,bundle)
        }
    }
}

class WorkersVH(private val binding: ItemWorkerBinding) : ViewHolder(binding.root) {

    fun bindItem(data: Worker) {

        val imageView = binding.avatar
        val fullName = "${data.firstName} ${data.lastName}"

        imageView.load(data.imageUrl) {
            error(R.drawable.goose_plug)
        }

        with(binding) {
            workerName.text = fullName
            workerPost.text = data.position
            workerTag.text = data.userTag
        }
    }
}

class WorkersDiffUtil : DiffUtil.ItemCallback<Worker>() {
    override fun areItemsTheSame(oldItem: Worker, newItem: Worker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Worker, newItem: Worker): Boolean {
        return oldItem == newItem
    }
}

