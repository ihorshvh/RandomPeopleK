package com.paint.randompeoplek.ui.randompeoplelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.usecase.RandomPeopleListUseCase
import com.paint.randompeoplek.usecase.model.Name
import com.paint.randompeoplek.usecase.model.Picture
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(private val randomPeopleListUseCase : RandomPeopleListUseCase) : ViewModel() {

    private val oneTimeErrorMessage : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    private val usersResponse : MutableLiveData<LoadResult<LiveDataResponse<List<User>>>> by lazy {
        MutableLiveData()
    }

    val oneTimeErrorMessageLiveData : LiveData<String> = oneTimeErrorMessage

    val usersResponseLiveData : LiveData<LoadResult<LiveDataResponse<List<User>>>> = usersResponse

    init {
        getRandomPeopleList(USER_QUANTITY)
    }

    fun getRandomPeopleList(userQuantity : String) {
        viewModelScope.launch {
            usersResponse.value = LoadResult.Loading(usersResponse.value?.data)

            val result = runCatching { randomPeopleListUseCase.getUserList(userQuantity) }

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

    fun clearOneTimeErrorMessage() {
        oneTimeErrorMessage.value = ""
    }

    companion object {

        const val USER_QUANTITY = "10"

    }

}

private fun Name.toUiParcelableName() =
    com.paint.randompeoplek.ui.model.Name(this.shortName, this.fullName)

private fun Picture.toUiParcelablePicture() =
    com.paint.randompeoplek.ui.model.Picture(this.medium, this.thumbnail)

private fun com.paint.randompeoplek.usecase.model.User.toUiParcelableUser() =
    com.paint.randompeoplek.ui.model.User(this.name.toUiParcelableName(),
        this.location,
        this.email,
        this.phone,
        this.picture.toUiParcelablePicture())

private fun List<com.paint.randompeoplek.usecase.model.User>.toUiParcelableUsers() = this.map { it.toUiParcelableUser() }