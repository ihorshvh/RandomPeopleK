package com.paint.randompeoplek.ui.randompeoplelist

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.domain.RandomPeopleListUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.model.toUiParcelableUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
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

    private val _randomPeopleList: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())

    private val _randomPeopleListScreenStateFlow: MutableStateFlow<RandomPeopleListScreenState>
        = MutableStateFlow(RandomPeopleListScreenState(randomPeopleListState = RandomPeopleListState.Initial))
    val randomPeopleListScreenStateFlow: StateFlow<RandomPeopleListScreenState> = _randomPeopleListScreenStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            snackbarMessageSharedFlow.asSharedFlow().collectLatest { snackbarMessage ->
                snackbarHostState.showSnackbar(snackbarMessage)
            }
        }
        getRandomPeopleList(USER_QUANTITY)
    }

    override fun getRandomPeopleList(userQuantity: String) {
        if (_randomPeopleListScreenStateFlow.value.randomPeopleListState !is RandomPeopleListState.Initial) {
            _isRefreshing.value = true
        }

        viewModelScope.launch {
            val result = runCatching { randomPeopleListUseCase.getUserList(userQuantity) }
            result.onSuccess {
                _randomPeopleList.value = it.users.toUiParcelableUsers()

                val searchQuery = _randomPeopleListScreenStateFlow.value.searchText

                if (it.networkError != null) {
                    val errorMessage = errorHandlerUseCase.getErrorMessage(it.networkError)
                    snackbarMessageSharedFlow.emit(errorMessage)

                    _randomPeopleListScreenStateFlow.value = RandomPeopleListScreenState(
                        randomPeopleListState = RandomPeopleListState.Error(getFilteredUsers(searchQuery))
                    )
                } else {
                    _randomPeopleListScreenStateFlow.value = RandomPeopleListScreenState(
                        randomPeopleListState = RandomPeopleListState.Success(getFilteredUsers(searchQuery))
                    )
                }

                // Workaround to fix the material pull refresh bug. When the data is taken from cache it happens to fast.
                // As the result the refresh indicator of PullToRefreshBox remains on the screen as it's state corrupts
                delay(REFRESHING_STATE_DELAY)
                _isRefreshing.value = false
            }

            result.onFailure {
                val errorMessage = errorHandlerUseCase.getErrorMessage(it)
                snackbarMessageSharedFlow.emit(errorMessage)

                // Workaround to fix the material pull refresh bug. When the data is taken from cache it happens to fast.
                // As the result the refresh indicator of PullToRefreshBox remains on the screen as it's state corrupts
                delay(REFRESHING_STATE_DELAY)
                _isRefreshing.value = false
            }
        }
    }

    private fun getFilteredUsers(searchQuery: String): List<User> {
        return _randomPeopleList.value.filter { it.name.shortName.contains(searchQuery, ignoreCase = true) }
    }

    fun onAction(action: RandomPeopleListAction) {
        when (action) {
            is RandomPeopleListAction.OnSearchTextChange -> {
                _randomPeopleListScreenStateFlow.update {
                    val searchQuery = action.newSearchText
                    it.copy(
                        searchText = searchQuery,
                        randomPeopleListState = RandomPeopleListState.Success(getFilteredUsers(searchQuery))
                    )
                }
            }
            is RandomPeopleListAction.OnSearchButtonClick -> {
                _randomPeopleListScreenStateFlow.update {
                    it.copy( isSearchVisible = true )
                }
            }
            is RandomPeopleListAction.OnCloseSearchButtonClick -> {
                _randomPeopleListScreenStateFlow.update {
                    it.copy( isSearchVisible = false, searchText = "" )
                }
            }
            is RandomPeopleListAction.OnRefreshClick -> {
                getRandomPeopleList(USER_QUANTITY)
            }
        }
    }

    companion object {
        const val USER_QUANTITY = "10"
        private const val ONE_TIME_ERROR_REPLAY = 0
        private const val ONE_TIME_ERROR_EXTRA_BUFFER_CAPACITY = 1
        private const val REFRESHING_STATE_DELAY = 100L
    }
}