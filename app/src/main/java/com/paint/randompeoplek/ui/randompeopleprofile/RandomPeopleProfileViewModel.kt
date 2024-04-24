package com.paint.randompeoplek.ui.randompeopleprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.domain.RandomPeopleProfileUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.model.Response
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.model.toUiParcelableUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleProfileViewModel @Inject constructor(
    private val randomPeopleProfileUseCase: RandomPeopleProfileUseCase,
    private val errorHandlerUseCase: ErrorHandlerUseCase
) : ViewModel() {

    private val _userResponseFlow: MutableStateFlow<Response<User>> = MutableStateFlow(Response.Initial())
    val userResponseFlow: StateFlow<Response<User>> = _userResponseFlow.asStateFlow()

    fun getUserById(userId: String) {
        viewModelScope.launch {
            val result = runCatching { randomPeopleProfileUseCase.getUserById(userId) }
            result.onSuccess {
                _userResponseFlow.value = Response.Success(it.toUiParcelableUser())
            }
            result.onFailure {
                val errorEntity = errorHandlerUseCase.getErrorEntity(it)
                _userResponseFlow.value = Response.Error(errorEntity)
            }
        }
    }
}