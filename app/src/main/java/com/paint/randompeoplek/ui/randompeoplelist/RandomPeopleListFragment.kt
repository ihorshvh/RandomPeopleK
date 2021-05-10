package com.paint.randompeoplek.ui.randompeoplelist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.paint.randompeoplek.R
import com.paint.randompeoplek.databinding.RandomPeopleListFragmentBinding
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.Resource
import com.paint.randompeoplek.ui.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomPeopleListFragment : Fragment() {

    private val userQuantity = "10"

    private val binding get() = randomPeopleListFragmentBinding!!
    private val randomPeopleListRecyclerViewAdapter :
            RandomPeopleListRecyclerViewAdapter = RandomPeopleListRecyclerViewAdapter()

    private var isFragmentInitiallyCreated = true
    private var randomPeopleListFragmentBinding : RandomPeopleListFragmentBinding? = null

    private lateinit var viewModel: RandomPeopleListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        savedInstanceState?.let {
            isFragmentInitiallyCreated = savedInstanceState.getBoolean(ARG_FRAGMENT_LAUNCHED, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("myTag", "onCreateView")
        randomPeopleListFragmentBinding = RandomPeopleListFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        return initializeViews()
    }

    private fun initializeViews() : View {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)

        binding.swipeRefreshLayout.setOnRefreshListener { getUsers() }
        binding.imgBtnObtainList.setOnClickListener { getUsers() }

        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = randomPeopleListRecyclerViewAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("myTag", "onViewCreated")
        viewModel = ViewModelProvider(this).get(RandomPeopleListViewModel::class.java)

        viewModel.usersResponse.observe(viewLifecycleOwner, { usersResponseResource ->
            handleResponse(usersResponseResource)
        })

        retrieveUsersIfFragmentCreated()
    }

    // Trigger the users retrieve in the fragment was just created
    private fun retrieveUsersIfFragmentCreated() {
        if(isFragmentInitiallyCreated) {
            getUsers()
            isFragmentInitiallyCreated = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.apply {
            putBoolean(ARG_FRAGMENT_LAUNCHED, isFragmentInitiallyCreated)
        }
    }

    private fun handleResponse(response: Resource<LiveDataResponse<List<User>>>) {
        when(response) {
            is Resource.Loading -> handleUserListWhenLoading(response.data)
            is Resource.Success -> handleUserListWhenSuccess(response.data)
            is Resource.Error -> Toast.makeText(activity, response.message.toString(), Toast.LENGTH_LONG).show()
        }

        if (response !is Resource.Loading) {
            binding.swipeRefreshLayout.isRefreshing = false
            checkLayoutVisibilityConditions()
        }
    }

    private fun handleUserListWhenLoading(response: LiveDataResponse<List<User>>?) {
        binding.swipeRefreshLayout.isRefreshing = true
        mapUserList(response)
    }

    private fun handleUserListWhenSuccess(response: LiveDataResponse<List<User>>?) {
        handleWarning(response?.warning)
        mapUserList(response)
    }
    
    private fun handleWarning(warning : String?) {
        if(warning != null) {
            Toast.makeText(activity, warning, Toast.LENGTH_LONG).show()
        }
    }
    
    private fun mapUserList(response: LiveDataResponse<List<User>>?) {
        if(response?.response?.isNotEmpty() == true){
            randomPeopleListRecyclerViewAdapter.updateUsersList(response.response!!)
        }
    }

    private fun checkLayoutVisibilityConditions() {
        if(randomPeopleListRecyclerViewAdapter.itemCount == 0) {
            toEmptyListMode()
        } else {
            toNonEmptyListMode()
        }
    }

    private fun toEmptyListMode() {
        binding.llNoUsers.visibility = View.VISIBLE
        binding.swipeRefreshLayout.visibility = View.GONE
    }

    private fun toNonEmptyListMode(){
        binding.llNoUsers.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.VISIBLE
    }

    private fun getUsers() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getRandomPeopleList(userQuantity)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        randomPeopleListFragmentBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.people_list_fragment_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_update -> {
            getUsers()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {

        const val ARG_FRAGMENT_LAUNCHED = "ARG_FRAGMENT_LAUNCHED"

    }
}