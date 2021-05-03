package com.paint.randompeoplek.ui.randompeoplelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.User
import com.paint.randompeoplek.repository.RandomPeopleListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(private val randomPeopleListRepository : RandomPeopleListRepository) : ViewModel() {

    val usersResponse : MutableLiveData<LiveDataResponse<List<User>>> by lazy {
        MutableLiveData()
    }

    fun getRandomPeopleList() {
        viewModelScope.launch {
            val result = runCatching { randomPeopleListRepository.getUserList("5") }

            result.onSuccess {
                Log.d("myTag", "SUCCESS")
                usersResponse.value = LiveDataResponse(it.users)
            }

            result.onFailure {
                Log.d("myTag", "ERROR " + it.message.toString())
                usersResponse.value = LiveDataResponse(it.message.toString())
            }
        }
    }

}