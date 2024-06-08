package com.instabug.android_challenge.ui.cachedRequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.instabug.android_challenge.databinding.ListItemCachedRequestBinding
import com.instabug.android_challenge.model.Request

class CachedRequestsAdapter: ListAdapter<Request, CachedRequestsAdapter.RequestItemViewHolder>(RequestDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestItemViewHolder {
        return RequestItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RequestItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RequestItemViewHolder(private val binding: ListItemCachedRequestBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup): RequestItemViewHolder{
                val bindings  = ListItemCachedRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RequestItemViewHolder(bindings)
            }
        }
        fun bind(item: Request){
            binding.request = item
        }
    }

    class RequestDiffCallback: DiffUtil.ItemCallback<Request>(){
        override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem.url == newItem.url

        }

    }
}