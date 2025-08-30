package com.paint.randompeoplek.ui.randompeopleprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paint.randompeoplek.domain.RandomPeopleProfileUseCase
import com.paint.randompeoplek.ui.model.toUiParcelableUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPeopleProfileViewModel @Inject constructor(
    private val randomPeopleProfileUseCase: RandomPeopleProfileUseCase
) : ViewModel(), PeopleProfileViewModel {

    private val _randomPeopleProfileStateFlow: MutableStateFlow<RandomPeopleProfileState> = MutableStateFlow(RandomPeopleProfileState.Initial)
    val randomPeopleProfileStateFlow: StateFlow<RandomPeopleProfileState> = _randomPeopleProfileStateFlow.asStateFlow()



    override fun getUserById(userId: String) {
        viewModelScope.launch {
            val result = runCatching { randomPeopleProfileUseCase.getUserById(userId) }
            result.onSuccess {
                _randomPeopleProfileStateFlow.value = RandomPeopleProfileState.Success(it.toUiParcelableUser())
            }
            result.onFailure {
                _randomPeopleProfileStateFlow.value = RandomPeopleProfileState.Error()
            }
        }
    }
}