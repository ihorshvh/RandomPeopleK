package com.paint.randompeoplek.ui.randompeoplelist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.paint.randompeoplek.R
import com.paint.randompeoplek.databinding.RandomPeopleListFragmentBinding
import com.paint.randompeoplek.mediator.model.User
import com.paint.randompeoplek.model.LiveDataResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomPeopleListFragment : Fragment() {

    private val userQuantity = "10"

    private val binding get() = randomPeopleListFragmentBinding!!
    private val randomPeopleListRecyclerViewAdapter :
            RandomPeopleListRecyclerViewAdapter = RandomPeopleListRecyclerViewAdapter()


    private var randomPeopleListFragmentBinding : RandomPeopleListFragmentBinding? = null

    private lateinit var viewModel: RandomPeopleListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

//        arguments?.let {
//            //columnCount = it.getInt(ARG_COLUMN_COUNT)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        randomPeopleListFragmentBinding = RandomPeopleListFragmentBinding.inflate(inflater, container, false)

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

        viewModel = ViewModelProvider(this).get(RandomPeopleListViewModel::class.java)

        viewModel.usersResponse.observe(viewLifecycleOwner, { usersResponse ->
            handleResponse(usersResponse)

            binding.swipeRefreshLayout.isRefreshing = false
        })

        getUsers()
    }

    private fun handleResponse(response : LiveDataResponse<List<User>>) {
        if(response.error != null){
            Toast.makeText(activity, response.error.toString(), Toast.LENGTH_LONG).show()
        }

        if(response.response?.isNotEmpty() == true){
            randomPeopleListRecyclerViewAdapter.updateUsersList(response.response!!)
        }

        checkLayoutVisibilityConditions()
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
        inflater.inflate(R.menu.toolbar_menu, menu)
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

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() = RandomPeopleListFragment()

//        @JvmStatic
//        fun newInstance(columnCount: Int) =
//            RandomPeopleListFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_COLUMN_COUNT, columnCount)
//                }
//            }
    }
}