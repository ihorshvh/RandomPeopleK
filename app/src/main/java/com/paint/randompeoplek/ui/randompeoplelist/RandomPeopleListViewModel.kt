package com.paint.randompeoplek.ui.randompeoplelist

import androidx.compose.material.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.model.Response
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.model.toUiParcelableUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleListViewModel @Inject constructor(
    private val randomPeopleListUseCase: RandomPeopleListUseCase,
    private val errorHandlerUseCase: ErrorHandlerUseCase
) : ViewModel(), PeopleListViewModel {

    private val snackbarMessageSharedFlow: MutableSharedFlow<String> =
        MutableSharedFlow(
            replay = ONE_TIME_ERROR_REPLAY,
            extraBufferCapacity = ONE_TIME_ERROR_EXTRA_BUFFER_CAPACITY,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    val snackbarHostState = SnackbarHostState()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _usersResponseFlow: MutableStateFlow<Response<List<User>>> = MutableStateFlow(Response.Initial())
    val usersResponseFlow: StateFlow<Response<List<User>>> = _usersResponseFlow.asStateFlow()

    init {
        viewModelScope.launch {
            snackbarMessageSharedFlow.asSharedFlow().collectLatest { snackbarMessage ->
                snackbarHostState.showSnackbar(snackbarMessage)
            }
        }
        getRandomPeopleList(USER_QUANTITY)
    }

    override fun getRandomPeopleList(userQuantity: String) {
        viewModelScope.launch {
            if (_usersResponseFlow.value !is Response.Initial) {
                _isRefreshing.value = true
            }

            val result = runCatching { randomPeopleListUseCase.getUserList(userQuantity) }
            result.onSuccess {
                if (it.networkError != null) {
                    val errorMessage = errorHandlerUseCase.getErrorMessage(it.networkError)
                    snackbarMessageSharedFlow.emit(errorMessage)

                    _usersResponseFlow.value = Response.Error(errorMessage, it.users.toUiParcelableUsers())
                } else {
                    _usersResponseFlow.value = Response.Success(it.users.toUiParcelableUsers())
                }

                _isRefreshing.value = false
            }

            result.onFailure {
                val errorMessage = errorHandlerUseCase.getErrorMessage(it)
                snackbarMessageSharedFlow.emit(errorMessage)
            }
        }
    }

    companion object {
        const val USER_QUANTITY = "10"
        private const val ONE_TIME_ERROR_REPLAY = 0
        private const val ONE_TIME_ERROR_EXTRA_BUFFER_CAPACITY = 1
    }
}