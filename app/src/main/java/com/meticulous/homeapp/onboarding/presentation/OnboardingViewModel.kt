package com.meticulous.homeapp.onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meticulous.homeapp.onboarding.domain.CheckIfAppIsDefaultUseCase
import com.meticulous.homeapp.onboarding.domain.GetWaitingUserActionUseCase
import com.meticulous.homeapp.onboarding.domain.MarkOnboardingCompleteUseCase
import com.meticulous.homeapp.onboarding.domain.SetAppAsDefaultUseCase
import com.meticulous.homeapp.onboarding.domain.SetWaitingUserActionUseCase
import com.meticulous.homeapp.util.AnalyticLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val logger: AnalyticLogger,
    private val checkIfAppIsDefaultUseCase: CheckIfAppIsDefaultUseCase,
    private val setAppAsDefaultUseCase: SetAppAsDefaultUseCase,
    private val setWaitingUserActionUseCase: SetWaitingUserActionUseCase,
    private val getWaitingUserActionUseCase: GetWaitingUserActionUseCase,
    private val markOnboardingCompleteUseCase: MarkOnboardingCompleteUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.FirstStep)
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    companion object {
        const val ONBOARDING_FIRST_STEP = 0
        const val ONBOARDING_SECOND_STEP = 1
        const val ONBOARDING_LAST_STEP = 2
        private const val ONBOARDING_COMPLETED = -1
        private const val SAVED_CURRENT_STATE = "appCurrentState"
    }

    init {
        val savedState = savedStateHandle.get<Int>(SAVED_CURRENT_STATE)
        logger.logInfo(message = "Last saved state is : $savedState")
        handleSavedState(savedState)
    }

    fun onFirstStepNextClicked() {
        _uiState.value = OnboardingUiState.SecondStep
        savedStateHandle[SAVED_CURRENT_STATE] = ONBOARDING_SECOND_STEP
    }

    fun onSecondStepNextClicked() {
        _uiState.value = OnboardingUiState.AwaitingUserAction
    }

    fun onThirdStepNextClicked() {
        viewModelScope.launch {
            setAppAsDefaultUseCase(true)
            markOnboardingCompleteUseCase()
        }
        _uiState.value = OnboardingUiState.HomeActivity
        savedStateHandle[SAVED_CURRENT_STATE] = ONBOARDING_COMPLETED
    }

    fun updateWaitingUserAction() {
        viewModelScope.launch {
            setWaitingUserActionUseCase(true)
        }
    }

    fun onWaitingUserActionCancelled() {
        viewModelScope.launch {
            setWaitingUserActionUseCase(false)
        }
        _uiState.value = OnboardingUiState.SecondStep
    }

    fun onViewResumed() {
        if (checkIfAppIsDefaultUseCase()) {
            logger.logInfo(message = "App is the default home app")
            if (getWaitingUserActionUseCase()) {
                logger.logInfo(message = "getNextDestination Destination.ONBOARDING_THIRD_STEP")
                _uiState.value = OnboardingUiState.ThirdStep
            } else {
                logger.logInfo(message = "getNextDestination Destination.HOME_ACTIVITY")
                _uiState.value = OnboardingUiState.HomeActivity
            }
        }
    }

    private fun handleSavedState(savedState: Int?) {
        savedState?.let { previousState ->
            logger.logInfo("Restoring the UI state to the previous state : $previousState")
            when (previousState) {
                ONBOARDING_FIRST_STEP -> {
                    _uiState.value = OnboardingUiState.FirstStep
                }

                ONBOARDING_SECOND_STEP -> {
                    _uiState.value = OnboardingUiState.SecondStep
                }

                ONBOARDING_LAST_STEP -> {
                    _uiState.value = OnboardingUiState.ThirdStep
                }

                else -> {}
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.logDebug("OnboardingViewModel onCleared called")
    }
}

sealed class OnboardingUiState {
    object FirstStep : OnboardingUiState()
    object SecondStep : OnboardingUiState()
    object ThirdStep : OnboardingUiState()
    object AwaitingUserAction : OnboardingUiState()
    object HomeActivity : OnboardingUiState()
    object Unknown : OnboardingUiState()
}