package com.paint.randompeoplek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomPeopleMainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_people_main_activity)

        Log.d("myLogs", "-----------------------------------------------------------------------------")
        listOf(1, 2, 3, 4).map { Log.d("myLogs", "map($it) "); it * it }.filter { Log.d("myLogs", "filter($it) "); it % 2 == 0 }
    }
}