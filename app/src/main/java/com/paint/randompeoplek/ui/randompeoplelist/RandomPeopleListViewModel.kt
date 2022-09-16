package com.paint.randompeoplek.ui.randompeoplelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.errorhandler.ErrorEntity
import com.paint.randompeoplek.errorhandler.ErrorHandler
import com.paint.randompeoplek.livedata.OnTimeLiveData
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.model.Name
import com.paint.randompeoplek.domain.model.Picture
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(private val randomPeopleListUseCase : RandomPeopleListUseCase,
                                                    private val errorHandler: ErrorHandler) : ViewModel() {

    private val oneTimeError : MutableLiveData<ErrorEntity> by lazy {
        OnTimeLiveData()
    }

    private val usersResponseFlow : MutableStateFlow<LoadResult<LiveDataResponse<List<User>>>> = MutableStateFlow(LoadResult.Loading())

    val oneTimeErrorLiveData : LiveData<ErrorEntity> = oneTimeError

    val usersResponseFlowData : StateFlow<LoadResult<LiveDataResponse<List<User>>>> = usersResponseFlow.asStateFlow()

    init {
        getRandomPeopleList(USER_QUANTITY)
    }

    fun getRandomPeopleList(userQuantity : String) {
        viewModelScope.launch {
            usersResponseFlow.value = LoadResult.Loading()

            val result = runCatching { randomPeopleListUseCase.getUserList(userQuantity) }

            result.onSuccess {
                if (it.throwable != null) {
                    val errorEntity = errorHandler.getErrorEntity(it.throwable!!)

                    oneTimeError.value = errorEntity
                    usersResponseFlow.value = LoadResult.Error(errorEntity, LiveDataResponse(it.users.toUiParcelableUsers()))
                } else {
                    usersResponseFlow.value = LoadResult.Success(LiveDataResponse(it.users.toUiParcelableUsers()))
                }
            }

            result.onFailure {
                // TODO consider adding data even if unknown error
                oneTimeError.value = errorHandler.getErrorEntity(it)
                usersResponseFlow.value = LoadResult.Error(errorHandler.getErrorEntity(it))
            }
        }
    }

    companion object {

        const val USER_QUANTITY = "10"

    }

}

private fun Name.toUiParcelableName() =
    com.paint.randompeoplek.ui.model.Name(this.shortName, this.fullName)

private fun Picture.toUiParcelablePicture() =
    com.paint.randompeoplek.ui.model.Picture(this.medium, this.thumbnail)

private fun com.paint.randompeoplek.domain.model.User.toUiParcelableUser() =
    com.paint.randompeoplek.ui.model.User(this.name.toUiParcelableName(),
        this.location,
        this.email,
        this.phone,
        this.picture.toUiParcelablePicture())

private fun List<com.paint.randompeoplek.domain.model.User>.toUiParcelableUsers() = this.map { it.toUiParcelableUser() }