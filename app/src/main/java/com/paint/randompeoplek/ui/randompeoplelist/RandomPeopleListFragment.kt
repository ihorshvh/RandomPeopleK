package com.paint.randompeoplek.ui.randompeoplelist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.paint.randompeoplek.R
import com.paint.randompeoplek.databinding.RandomPeopleListFragmentBinding
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.randompeopleprofile.RandomPeopleProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomPeopleListFragment : Fragment() {

    private val binding get() = randomPeopleListFragmentBinding!!
    private val randomPeopleListRecyclerViewAdapter:
            RandomPeopleListRecyclerViewAdapter = RandomPeopleListRecyclerViewAdapter { user ->
        onRandomPeopleListRecyclerViewAdapterClick(user)
    }

    private var randomPeopleListFragmentBinding: RandomPeopleListFragmentBinding? = null

    private lateinit var viewModel: RandomPeopleListViewModel

    private fun onRandomPeopleListRecyclerViewAdapterClick(user: User) {
        val bundle = bundleOf(RandomPeopleProfileFragment.ARG_USER to user)

        Navigation.findNavController(binding.root)
            .navigate(
                R.id.action_randomPeopleListFragment_to_randomPeopleProfileFragment,
                bundle
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        randomPeopleListFragmentBinding = RandomPeopleListFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        initializeViewModel()
        setUpViewModel()
    }

    private fun initializeViews() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)

        binding.swipeRefreshLayout.setOnRefreshListener { getUsers() }
        binding.imgBtnObtainList.setOnClickListener { getUsers() }

        binding.list.adapter = randomPeopleListRecyclerViewAdapter
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this).get(RandomPeopleListViewModel::class.java)
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usersResponseFlow.collect { usersResponseResource ->
                    handleUserListResponse(usersResponseResource)
                }


            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.oneTimeErrorFlow.collect { oneTimeError ->
                    handleOneTimeErrorMessageShowing(oneTimeError)
                }
            }
        }
    }

    private fun handleOneTimeErrorMessageShowing(error: ErrorEntity) {
        val oneTimeErrorMessage = getOneTimeErrorMessage(error)
        Toast.makeText(activity, oneTimeErrorMessage, Toast.LENGTH_LONG).show()
    }

    private fun getOneTimeErrorMessage(error: ErrorEntity) : String {
        return when (error) {
            is ErrorEntity.Network -> getString(R.string.error_outdated_users_loaded)
            is ErrorEntity.ServiceUnavailable -> getString(R.string.error_unknown)
            else -> getString(R.string.error_unknown)
        }
    }

    private fun handleUserListResponse(response: LoadResult<LiveDataResponse<List<User>>>) {
        when (response) {
            is LoadResult.Loading -> handleUserListWhenLoading(response)
            is LoadResult.Success -> handleUserListWhenSuccess(response)
            is LoadResult.Error -> handleUserListWhenError(response)
        }

        if (response !is LoadResult.Loading) {
            binding.swipeRefreshLayout.isRefreshing = false
            checkLayoutVisibilityConditions()
        }
    }

    private fun handleUserListWhenLoading(response: LoadResult<LiveDataResponse<List<User>>>) {
        binding.swipeRefreshLayout.isRefreshing = true
        mapUserList(response.data)
    }

    private fun handleUserListWhenSuccess(response: LoadResult<LiveDataResponse<List<User>>>) {
        mapUserList(response.data)
    }

    private fun handleUserListWhenError(response: LoadResult<LiveDataResponse<List<User>>>) {
        // no need to handle error message at this time
        // as it will be shown as a one time message only.
        // See @RandomPeopleListViewModel for reference.
        mapUserList(response.data)
    }

    private fun mapUserList(response: LiveDataResponse<List<User>>?) {
        if (response?.response?.isNotEmpty() == true) {
            randomPeopleListRecyclerViewAdapter.submitList(response.response!!)
        }
    }

    private fun checkLayoutVisibilityConditions() {
        if (randomPeopleListRecyclerViewAdapter.itemCount == 0) {
            toEmptyListMode()
        } else {
            toNonEmptyListMode()
        }
    }

    private fun toEmptyListMode() {
        binding.llNoUsers.visibility = View.VISIBLE
        binding.list.visibility = View.GONE
    }

    private fun toNonEmptyListMode() {
        binding.llNoUsers.visibility = View.GONE
        binding.list.visibility = View.VISIBLE
    }

    private fun getUsers() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY)
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

}