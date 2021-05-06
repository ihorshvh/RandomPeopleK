package com.paint.randompeoplek.ui.randompeoplelist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paint.randompeoplek.R
import com.paint.randompeoplek.ui.randompeoplelist.dummy.DummyContent
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class RandomPeopleListFragment : Fragment() {

    private var columnCount = 1

    private lateinit var viewModel: RandomPeopleListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.random_people_list_fragment, container, false)

        val toolBar : Toolbar = view.findViewById(R.id.toolbar);
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolBar)

        // Set the adapter
        val recycleView : RecyclerView = view.findViewById(R.id.list)
        with(recycleView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = RandomPeopleListRecyclerViewAdapter(DummyContent.ITEMS)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("myTag", "onViewCreated")
        viewModel = ViewModelProvider(this).get(RandomPeopleListViewModel::class.java)

        viewModel.usersResponse.observe(viewLifecycleOwner, { usersResponse ->

            if(usersResponse.error != null){
                Log.d("myTag", usersResponse.error.toString())
            }

            if(usersResponse.response != null){
                Log.d("myTag", usersResponse.response?.size.toString())

                if(usersResponse.response?.isNotEmpty() == true){
                    Log.d("myTag", usersResponse.response!![0].toString())
                }
            }

        })

        viewModel.getRandomPeopleList("5")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_update -> {
            Toast.makeText(activity, "Test", Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

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