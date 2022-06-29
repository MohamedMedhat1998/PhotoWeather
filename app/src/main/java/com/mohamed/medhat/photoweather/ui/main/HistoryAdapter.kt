package com.mohamed.medhat.photoweather.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamed.medhat.photoweather.R
import com.mohamed.medhat.photoweather.databinding.ItemHistoryBinding
import com.mohamed.medhat.photoweather.model.HistoryItem
import java.io.File
import javax.inject.Inject

/**
 * Binds a list of [HistoryItem] to the UI.
 */
class HistoryAdapter @Inject constructor() :
    ListAdapter<HistoryItem, HistoryAdapter.HistoryItemHolder>(HistoryItemsDiffUtilsCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryItemHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemHolder, position: Int) {
        holder.bind(currentList[position])
    }

    /**
     * Represents a single [HistoryItem] in the recycler view.
     */
    class HistoryItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHistoryBinding.bind(itemView)

        /**
         * Binds a single [HistoryItem] to the UI.
         */
        fun bind(historyItem: HistoryItem) {
            binding.tvItemHistoryName.text = historyItem.getImageName()
            Glide.with(itemView.context)
                .load(File(historyItem.imagePath))
                .into(binding.ivItemHistoryPreview)
        }
    }

    /**
     * Decides whether two [HistoryItem]s are the same or not.
     */
    object HistoryItemsDiffUtilsCallback : DiffUtil.ItemCallback<HistoryItem>() {
        override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem.imagePath == newItem.imagePath
        }

        override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem.imagePath == newItem.imagePath
        }

    }
}