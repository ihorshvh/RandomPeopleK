package com.paint.randompeoplek.ui.randompeopleprofile

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.paint.randompeoplek.R
import com.paint.randompeoplek.databinding.RandomPeopleProfileFragmentBinding
import com.paint.randompeoplek.ui.model.User

class RandomPeopleProfileFragment : Fragment() {

    private val binding get() = randomPeopleProfileFragmentBinding!!
    private var randomPeopleProfileFragmentBinding : RandomPeopleProfileFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        randomPeopleProfileFragmentBinding = RandomPeopleProfileFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        mapData()
    }

    private fun initializeViews() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_img)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun mapData() {
        val user = arguments?.getParcelable<User>(ARG_USER)

        binding.tvProfileName.text = user?.name?.fullName
        binding.tvProfileLocation.text = user?.location
        binding.tvProfileEmail.text = user?.email
        binding.tvProfilePhone.text = user?.phone

        mapPicture(binding.ivProfileImage, user?.picture?.medium)
    }

    private fun mapPicture(imageView : ImageView, url : String?) {
        val imageWidth: Int =
            imageView.context.resources.getDimensionPixelSize(R.dimen.profile_image_width)
        val imageHeight: Int =
            imageView.context.resources.getDimensionPixelSize(R.dimen.profile_image_height)

        Glide.with(imageView.context)
            .load(url)
            .override(imageWidth, imageHeight)
            .circleCrop()
            .error(R.drawable.ic_user_default_picture)
            .placeholder(R.drawable.ic_user_default_picture)
            .into(imageView);
    }

    override fun onDestroyView() {
        super.onDestroyView()

        randomPeopleProfileFragmentBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.people_profile_fragment_toolbar_menu, menu)
    }

    companion object {

        const val ARG_USER = "ARG_USER"

    }

}