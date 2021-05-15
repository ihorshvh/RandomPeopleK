package com.paint.randompeoplek.ui.randompeoplelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.mediator.RandomPeopleListMediator
import com.paint.randompeoplek.mediator.toUiParcelableUsers
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(private val randomPeopleListMediator : RandomPeopleListMediator) : ViewModel() {

    val oneTimeErrorMessage : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val usersResponse : MutableLiveData<LoadResult<LiveDataResponse<List<User>>>> by lazy {
        MutableLiveData()
    }

    fun getRandomPeopleList(userQuantity : String) {
        viewModelScope.launch {
            usersResponse.value = LoadResult.Loading(usersResponse.value?.data)

            val result = runCatching { randomPeopleListMediator.getUserList(userQuantity) }

            result.onSuccess {
                if (it.throwable != null) {
                    val errorMessage = it.throwable!!.message.toString()

                    oneTimeErrorMessage.value = errorMessage
                    usersResponse.value =
                        LoadResult.Error(errorMessage, LiveDataResponse(it.users.toUiParcelableUsers()))
                } else {
                    usersResponse.value =
                        LoadResult.Success(LiveDataResponse(it.users.toUiParcelableUsers()))
                }
            }

            result.onFailure {
                oneTimeErrorMessage.value = it.message.toString()
                usersResponse.value = LoadResult.Error(it.message.toString())
            }
        }
    }

}