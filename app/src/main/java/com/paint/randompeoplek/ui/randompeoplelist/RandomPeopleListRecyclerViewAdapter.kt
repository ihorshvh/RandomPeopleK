package com.paint.randompeoplek.ui.randompeoplelist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.paint.randompeoplek.R
import com.paint.randompeoplek.databinding.RandomPeopleListItemBinding
import com.paint.randompeoplek.ui.model.User

/**
 * [RecyclerView.Adapter] that can display a [User].
 */
class RandomPeopleListRecyclerViewAdapter(
    private val users: MutableList<User> = arrayListOf()
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

        // TODO add image
    }

    override fun getItemCount(): Int = users.size

    fun updateUsersList(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)

        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding : RandomPeopleListItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val bundle = bundleOf("test" to users[absoluteAdapterPosition])

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_randomPeopleListFragment_to_randomPeopleProfileFragment, bundle)
        }

    }
}