package com.paint.randompeoplek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paint.randompeoplek.ui.randompeoplelist.RandomPeopleListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomPeopleMainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_people_main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RandomPeopleListFragment.newInstance())
                .commitNow()
        }
    }
}