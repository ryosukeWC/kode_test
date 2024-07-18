package com.example.kode.presentation.feature.workers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.kode.R
import com.example.kode.databinding.ItemWorkerBinding
import com.example.kode.model.Worker

class WorkersAdapter() : ListAdapter<Worker, WorkersVH>(WorkersDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkersVH {
        return WorkersVH(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_worker,parent,false))
    }

    override fun onBindViewHolder(holder: WorkersVH, position: Int) {
        val itemView = getItem(position)
        holder.bindItem(itemView)
    }
}

class WorkersVH(itemView : View) : ViewHolder(itemView) {

    private lateinit var binding: ItemWorkerBinding

    fun bindItem(data: Worker) {

        binding = ItemWorkerBinding.bind(itemView)

        with(binding) {
            avatar.load(data.imageUrl)
            workerName.text = data.fullName
            subtitle.text = data.post
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

