package com.paint.randompeoplek.ui.randompeoplelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paint.randompeoplek.model.User
import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.repository.RandomPeopleListRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RandomPeopleListViewModel : ViewModel() {

    val users : MutableLiveData<List<User>> by lazy {
        MutableLiveData()
    }

    private val gson : Gson by lazy {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.create()
    }

    private val randomPeopleListRepository : RandomPeopleListRepository by lazy {

        val randomPeopleService = Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/1.3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RandomPeopleService::class.java)

        RandomPeopleListRepository(randomPeopleService)
    }


    fun getRandomPeopleList() {
        viewModelScope.launch {
            val result = runCatching { randomPeopleListRepository.getUserList("5") }

            result.onSuccess {
                users.value = it.users
                Log.d("myTags", "SUCSESS")
            }

            result.onFailure {
                Log.d("myTags", "ERROR " + it.message.toString())
            }
        }
    }

}