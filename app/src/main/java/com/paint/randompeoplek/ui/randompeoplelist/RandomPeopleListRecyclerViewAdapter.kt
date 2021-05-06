package com.paint.randompeoplek.ui.randompeoplelist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.paint.randompeoplek.databinding.RandomPeopleListItemBinding

import com.paint.randompeoplek.ui.randompeoplelist.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class RandomPeopleListRecyclerViewAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<RandomPeopleListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RandomPeopleListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.binding.tvUserName.text = item.id
        holder.binding.tvAddress.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding : RandomPeopleListItemBinding)
        : RecyclerView.ViewHolder(binding.root)
}