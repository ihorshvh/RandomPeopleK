package com.paint.randompeoplek.ui.randompeoplelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.model.Name
import com.paint.randompeoplek.domain.model.Picture
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(private val randomPeopleListUseCase : RandomPeopleListUseCase,
                                                    private val errorHandlerUseCase: ErrorHandlerUseCase) : ViewModel() {

    private val _oneTimeErrorFlow : MutableSharedFlow<ErrorEntity> = MutableSharedFlow(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val _usersResponseFlow : MutableStateFlow<LoadResult<LiveDataResponse<List<User>>>> = MutableStateFlow(LoadResult.Loading())
    val usersResponseFlow = _usersResponseFlow.asStateFlow()

    private var lastLoadingResult : LoadResult<LiveDataResponse<List<User>>> = LoadResult.Loading()

    val oneTimeErrorFlow : SharedFlow<ErrorEntity> = _oneTimeErrorFlow.asSharedFlow()



    init {
        getRandomPeopleList(USER_QUANTITY)
    }

    fun getRandomPeopleList(userQuantity : String) {
        viewModelScope.launch {
            _usersResponseFlow.value = LoadResult.Loading(lastLoadingResult.data)
            val result = runCatching { randomPeopleListUseCase.getUserList(userQuantity) }

            result.onSuccess {
                if (it.throwable != null) {
                    val errorEntity = errorHandlerUseCase.getErrorEntity(it.throwable!!)

                    _oneTimeErrorFlow.emit(errorEntity)
                    setResult(LoadResult.Error(errorEntity, LiveDataResponse(it.users.toUiParcelableUsers())))
                } else {
                    setResult(LoadResult.Success(LiveDataResponse(it.users.toUiParcelableUsers())))
                }
            }

            result.onFailure {
                // TODO consider adding data even if unknown error
                _oneTimeErrorFlow.emit(errorHandlerUseCase.getErrorEntity(it))
                _usersResponseFlow.value = LoadResult.Error(errorHandlerUseCase.getErrorEntity(it))
            }
        }
    }

    private fun setResult(result: LoadResult<LiveDataResponse<List<User>>>) {
        lastLoadingResult = result
        _usersResponseFlow.value = lastLoadingResult
    }

    suspend fun getUserByFullName(userFullName: String): User? {
        return randomPeopleListUseCase.getUserByUserName(userFullName).toUiParcelableUser()
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