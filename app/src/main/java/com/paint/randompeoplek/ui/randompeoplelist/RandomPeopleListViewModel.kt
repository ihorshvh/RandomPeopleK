package com.paint.randompeoplek.ui.randompeoplelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.model.User
import com.paint.randompeoplek.repository.RandomPeopleListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var randomPeopleListRepository : RandomPeopleListRepository

    val users : MutableLiveData<List<User>> by lazy {
        MutableLiveData()
    }

    fun getRandomPeopleList() {
        viewModelScope.launch {
            val result = runCatching { randomPeopleListRepository.getUserList("5") }

            result.onSuccess {
                Log.d("myTag", "SUCCESS")
                users.value = it.users
            }

            result.onFailure {
                Log.d("myTag", "ERROR " + it.message.toString())
            }
        }
    }

}