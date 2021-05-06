package com.paint.randompeoplek.ui.randompeoplelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.mediator.RandomPeopleListMediator
import com.paint.randompeoplek.mediator.model.User
import com.paint.randompeoplek.model.LiveDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(private val randomPeopleListMediator : RandomPeopleListMediator) : ViewModel() {

    val usersResponse : MutableLiveData<LiveDataResponse<List<User>>> by lazy {
        MutableLiveData()
    }

    fun getRandomPeopleList(userQuantity : String) {
        viewModelScope.launch {
            val result = runCatching { randomPeopleListMediator.getUserList(userQuantity) }

            result.onSuccess {
                Log.d("myTag", "SUCCESS")

                if(it.throwable != null) {
                    usersResponse.value = LiveDataResponse(it.users, it.throwable!!.message.toString())
                } else {
                    usersResponse.value = LiveDataResponse(it.users)
                }
            }

            result.onFailure {
                Log.d("myTag", "ERROR " + it.message.toString())
                usersResponse.value = LiveDataResponse(it.message.toString())
            }
        }
    }

}