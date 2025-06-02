package com.meticulous.homeapp.util

interface PreferencesHelper {
    fun isOnboardingComplete(): Boolean
    fun setOnboardingComplete(isComplete: Boolean)
    fun isAppSetAsDefaultLauncher(): Boolean
    fun setAppAsDefaultLauncher(isDefault: Boolean)
    fun setAwaitingUserAction(isAwaiting: Boolean)
    fun getAwaitingUserAction(): Boolean
}