package com.meticulous.homeapp.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.meticulous.homeapp.onboarding.domain.IsOnboardingRequiredUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingActivityViewModel @Inject constructor(
    isOnboardingRequiredUseCase: IsOnboardingRequiredUseCase,
) : ViewModel() {

    private val _startPoint = MutableStateFlow<StartPoint>(StartPoint.Unknown)
    val startPoint: StateFlow<StartPoint> = _startPoint.asStateFlow()

    init {
        if (isOnboardingRequiredUseCase()) {
            _startPoint.value = StartPoint.OnboardingActivity
        } else {
            _startPoint.value = StartPoint.HomeActivity
        }
    }
}

sealed class StartPoint {
    object HomeActivity : StartPoint()
    object OnboardingActivity : StartPoint()
    object Unknown : StartPoint()
}