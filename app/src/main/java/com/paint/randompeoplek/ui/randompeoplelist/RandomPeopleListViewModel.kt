package com.paint.randompeoplek.ui.randompeoplelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.mediator.RandomPeopleListMediator
import com.paint.randompeoplek.mediator.toUiParcelableUsers
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.Resource
import com.paint.randompeoplek.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(private val randomPeopleListMediator : RandomPeopleListMediator) : ViewModel() {

    val usersResponse : MutableLiveData<Resource<LiveDataResponse<List<User>>>> by lazy {
        MutableLiveData()
    }

    fun getRandomPeopleList(userQuantity : String) {
        viewModelScope.launch {
            usersResponse.value = Resource.Loading(usersResponse.value?.data)

            val result = runCatching { randomPeopleListMediator.getUserList(userQuantity) }

            result.onSuccess {
                Log.d("myTag", "SUCCESS")

                if(it.throwable != null) {
                    usersResponse.value = Resource.Success(LiveDataResponse(
                        it.users.toUiParcelableUsers(),
                        it.throwable!!.message.toString()))
                } else {
                    usersResponse.value =
                        Resource.Success(LiveDataResponse(it.users.toUiParcelableUsers()))
                }
            }

            result.onFailure {
                Log.d("myTag", "ERROR " + it.message.toString())
                usersResponse.value = Resource.Error(it.message.toString())
            }
        }
    }

}