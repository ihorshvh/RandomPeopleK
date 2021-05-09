package com.paint.randompeoplek.ui.randompeoplelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paint.randompeoplek.R
import com.paint.randompeoplek.databinding.RandomPeopleListItemBinding
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.randompeopleprofile.RandomPeopleProfileFragment

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
        mapViewHolder(holder, user)
    }

    private fun mapViewHolder(holder: ViewHolder, user: User) {
        holder.binding.tvUserName.text = user.name.shortName
        holder.binding.tvAddress.text = user.location

        mapPicture(holder.binding.ivUserImage, user.picture.thumbnail)
    }

    private fun mapPicture(imageView : ImageView, url : String) {
        val imageWidth: Int =
            imageView.context.resources.getDimensionPixelSize(R.dimen.row_user_default_image_width)
        val imageHeight: Int =
            imageView.context.resources.getDimensionPixelSize(R.dimen.row_user_default_image_height)

        Glide.with(imageView.context)
            .load(url)
            .override(imageWidth, imageHeight)
            .circleCrop()
            .error(R.drawable.ic_user_default_picture)
            .placeholder(R.drawable.ic_user_default_picture)
            .into(imageView);
    }

    override fun getItemCount(): Int = users.size

    fun updateUsersList(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)

        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RandomPeopleListItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val bundle = bundleOf(RandomPeopleProfileFragment.ARG_USER to users[absoluteAdapterPosition])

            Navigation.findNavController(binding.root)
                .navigate(
                    R.id.action_randomPeopleListFragment_to_randomPeopleProfileFragment,
                    bundle
                )
        }

    }
}