package com.paint.randompeoplek.ui.randompeoplelist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.paint.randompeoplek.databinding.RandomPeopleListItemBinding
import com.paint.randompeoplek.mediator.model.User

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class RandomPeopleListRecyclerViewAdapter(
    private val users: List<User>
) : RecyclerView.Adapter<RandomPeopleListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RandomPeopleListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.binding.tvUserName.text = user.name.shortName
        holder.binding.tvAddress.text = user.location
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(val binding : RandomPeopleListItemBinding)
        : RecyclerView.ViewHolder(binding.root)
}