package com.meticulous.homeapp.onboarding

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meticulous.homeapp.util.OnboardingManager
import com.meticulous.homeapp.util.OnboardingManager.isHomeAppSetAsDefault
import com.meticulous.homeapp.util.OnboardingManager.isWaitingForUserAction
import com.meticulous.homeapp.util.logI

class OnboardingViewModel : ViewModel() {
    private val _currentDestination =
        MutableLiveData<Destination>(Destination.ONBOARDING_FIRST_STEP)
    val currentDestination: LiveData<Destination>
        get() = _currentDestination

    companion object {
        val ONBOARDING_FIRST_STEP = 0
        val ONBOARDING_SECOND_STEP = 1
        val ONBOARDING_LAST_STEP = 2
    }

    fun onFirstStepNextClicked() {
        _currentDestination.value = Destination.ONBOARDING_SECOND_STEP
    }

    fun onSecondStepNextClicked() {
        _currentDestination.value = Destination.ONBOARDING_AWAITING_USER_ACTION
    }

    fun onThirdStepNextClicked() {
        _currentDestination.value = Destination.HOME_ACTIVITY
    }

    fun updateWaitingUserAction(context: Context) {
        OnboardingManager.setOnboardingStateAwaitingUserAction(context)
    }

    fun onWaitingUserActionCancelled(context: Context) {
        OnboardingManager.setOnboardingCancelled(context)
    }

    fun onViewResumed(context: Context) {
        if (isHomeAppSetAsDefault(context)) {
            logI(message = "App is the default home app")
            if (isWaitingForUserAction(context)) {
                logI(message = "getNextDestination Destination.ONBOARDING_THIRD_STEP")
                _currentDestination.value = Destination.ONBOARDING_THIRD_STEP
            } else {
                logI(message = "getNextDestination Destination.HOME_ACTIVITY")
                _currentDestination.value = Destination.HOME_ACTIVITY
            }
        }
    }
}

enum class Destination {
    ONBOARDING_FIRST_STEP,
    ONBOARDING_SECOND_STEP,
    ONBOARDING_THIRD_STEP,
    ONBOARDING_AWAITING_USER_ACTION,
    HOME_ACTIVITY,
    UNKNOWN
}