package com.paint.randompeoplek.ui.randompeopleprofile

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.paint.randompeoplek.R
import com.paint.randompeoplek.databinding.RandomPeopleProfileFragmentBinding
import com.paint.randompeoplek.ui.model.User

class RandomPeopleProfileFragment : Fragment() {

    private val binding get() = randomPeopleProfileFragmentBinding!!
    private var randomPeopleProfileFragmentBinding : RandomPeopleProfileFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        setUpAnimation()
    }

    private fun setUpAnimation() {
        val transition = TransitionInflater.from(activity)
                .inflateTransition(R.transition.changebounds_with_arcmotion)
        activity?.window?.sharedElementEnterTransition = transition
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

        return initializeViews()
    }

    private fun initializeViews() : View {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_img)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapData()
    }

    private fun mapData() {
        val user = arguments?.getParcelable<User>(ARG_USER)
        Log.d("myLogs", user.toString())

        binding.tvProfileName.text = user?.name?.fullName
        binding.tvProfileLocation.text = user?.location
        binding.tvProfileEmail.text = user?.email
        binding.tvProfilePhone.text = user?.phone

        // TODO add image
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