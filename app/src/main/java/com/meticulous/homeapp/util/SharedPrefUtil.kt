package com.meticulous.homeapp.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit


private const val onBoardingSharedPrefFile = "onBoardingSharedPrefFile"
private const val onBoardingStatePrefKey = "onBoardingStatePrefKey"

/**
 * Saves the onboarding state to the shared preferences
 * This flag tracks whether the user has completed the onboarding process or not
 *
 * @param context The context of the application
 * @param onboardingState The state of the onboarding
 * @return Unit
 * */
fun saveOnboardingStateToSharedPreference(context: Context, onboardingState: String) {
    logI(message = "Utils.saveOnboardingStateToSharedPreference onboardingState: $onboardingState")
    val sharedPreferences = context.getSharedPreferences(onBoardingSharedPrefFile, MODE_PRIVATE)
    sharedPreferences.edit {
        putString(onBoardingStatePrefKey, onboardingState)
    }
}

/**
 * Reads the onboarding state from the shared preferences
 *
 * @param context The context of the application
 * @return String that represent the state of the onboarding process
 * */
fun getOnboardingStateFromSharedPreference(context: Context): String {
    val sharedPreferences = context.getSharedPreferences(onBoardingSharedPrefFile, MODE_PRIVATE)
    val onboardingState =
        sharedPreferences.getString(onBoardingStatePrefKey, OnboardingState.NOT_STARTED.state)
    logI(message = "Utils.getOnboardingStateFromSharedPreference onboardingState: $onboardingState")
    return onboardingState ?: OnboardingState.NOT_STARTED.state
}