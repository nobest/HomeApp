package com.meticulous.homeapp.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit


private const val onBoardingSharedPrefFile = "onBoardingSharedPrefFile"
private const val onBoardingSharedPrefKey = "onBoardingSharedPrefKey"
private const val changeHomeUserInitiationPrefKey = "changeHomeUserInitiationPrefKey"

/**
 * Saves the onboarding state to the shared preferences
 * This flag tracks whether the user has completed the onboarding process or not
 *
 * @param context The context of the application
 * @param onboardingState The state of the onboarding
 * @return Unit
 * */
fun saveOnboardingStateToSharedPreference(context: Context, onboardingState: Boolean) {
    logI(message = "Utils.saveOnboardingStateToSharedPreference onboardingState: $onboardingState")
    val sharedPreferences = context.getSharedPreferences(onBoardingSharedPrefFile, MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean(onBoardingSharedPrefKey, onboardingState)
    }
}

/**
 * Reads the onboarding state from the shared preferences
 *
 * @param context The context of the application
 * @return Boolean True if the user had completed the onboarding process, false otherwise
 * */
fun getOnboardingStateFromSharedPref(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(onBoardingSharedPrefFile, MODE_PRIVATE)
    val onboardingState = sharedPreferences.getBoolean(onBoardingSharedPrefKey, false)
    logI(message = "Utils.readOnboardingStateFromSharedPref onboardingState: $onboardingState")
    return onboardingState
}

fun saveUserInitiatedChangeHomePreference(context: Context, userInitiated: Boolean) {
    logI(message = "Utils.saveUserInitiatedChangeHomePreference onboardingState: $userInitiated")
    val sharedPreferences = context.getSharedPreferences(onBoardingSharedPrefFile, MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean(changeHomeUserInitiationPrefKey, userInitiated)
    }
}

fun getUserInitiatedChangeHomePreference(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(onBoardingSharedPrefFile, MODE_PRIVATE)
    val onboardingState = sharedPreferences.getBoolean(changeHomeUserInitiationPrefKey, false)
    logI(message = "Utils.getUserInitiatedChangeHomePreference onboardingState: $onboardingState")
    return onboardingState
}