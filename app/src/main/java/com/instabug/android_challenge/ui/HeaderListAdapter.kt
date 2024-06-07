package com.instabug.khaledtask.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.instabug.android_challenge.databinding.ListItemHeaderBinding
import com.instabug.android_challenge.model.Header

class HeaderListAdapter(var currentList: MutableList<Header>, private val removeHeader: (position: Int) -> Unit): RecyclerView.Adapter<HeaderListAdapter.HeaderViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }


    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(currentList[position], position, removeHeader)
    }

    class HeaderViewHolder(private val binding: ListItemHeaderBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup): HeaderViewHolder{
                val bindings  = ListItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HeaderViewHolder(bindings)
            }
        }
        fun bind(item: Header, position: Int, removeHeader: (position: Int) -> Unit){
            binding.key = item.key
            binding.value = item.value
            binding.keyTv.addTextChangedListener{
                item.key = binding.key?:""
            }
            binding.valueTv.addTextChangedListener{
                item.value = binding.value?:""
            }
            binding.deleteBtn.setOnClickListener {
                removeHeader(position)
            }

        }
    }

}