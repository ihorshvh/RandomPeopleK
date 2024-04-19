package com.paint.randompeoplek.ui.randompeoplelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.domain.model.Name
import com.paint.randompeoplek.domain.model.Picture
import com.paint.randompeoplek.model.LiveDataResponse
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(
    private val randomPeopleListUseCase: RandomPeopleListUseCase,
    private val errorHandlerUseCase: ErrorHandlerUseCase
) : ViewModel() {

    private val _oneTimeErrorFlow: MutableSharedFlow<ErrorEntity> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val oneTimeErrorFlow: SharedFlow<ErrorEntity> = _oneTimeErrorFlow.asSharedFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _usersResponseFlow: MutableStateFlow<LoadResult<LiveDataResponse<List<User>>>> = MutableStateFlow(LoadResult.Initial())
    val usersResponseFlow: StateFlow<LoadResult<LiveDataResponse<List<User>>>> = _usersResponseFlow.asStateFlow()

    init {
        getRandomPeopleList(USER_QUANTITY)
    }

    fun getRandomPeopleList(userQuantity: String) {
        viewModelScope.launch {
            if (_usersResponseFlow.value != LoadResult.Initial<LiveDataResponse<List<User>>>()) {
                _isRefreshing.emit(true)
            }

            val result = runCatching { randomPeopleListUseCase.getUserList(userQuantity) }
            result.onSuccess {
                if (it.throwable != null) {
                    val errorEntity = errorHandlerUseCase.getErrorEntity(it.throwable!!)

                    _oneTimeErrorFlow.emit(errorEntity)
                    _usersResponseFlow.emit(LoadResult.Error(errorEntity, LiveDataResponse(it.users.toUiParcelableUsers())))
                } else {
                    _usersResponseFlow.emit(LoadResult.Success(LiveDataResponse(it.users.toUiParcelableUsers())))
                }

                _isRefreshing.emit(false)
            }

            result.onFailure {
                // TODO consider adding data even if unknown error
                _oneTimeErrorFlow.emit(errorHandlerUseCase.getErrorEntity(it))
                _usersResponseFlow.emit(LoadResult.Error(errorHandlerUseCase.getErrorEntity(it)))
                _isRefreshing.emit(false)
            }
        }
    }

    suspend fun getUserByFullName(userFullName: String): User {
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
    User(
        this.id,
        this.name.toUiParcelableName(),
        this.location,
        this.email,
        this.phone,
        this.picture.toUiParcelablePicture()
    )

private fun List<com.paint.randompeoplek.domain.model.User>.toUiParcelableUsers() = this.map { it.toUiParcelableUser() }