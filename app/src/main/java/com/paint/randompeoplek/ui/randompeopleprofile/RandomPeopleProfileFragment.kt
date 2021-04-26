package com.paint.randompeoplek.ui.randompeopleprofile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paint.randompeoplek.R

class RandomPeopleProfileFragment : Fragment() {

    companion object {
        fun newInstance() = RandomPeopleProfileFragment()
    }

    private lateinit var viewModel: RandomPeopleProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.random_people_profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RandomPeopleProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}