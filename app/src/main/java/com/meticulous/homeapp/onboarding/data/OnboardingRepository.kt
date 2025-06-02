package com.meticulous.homeapp.onboarding.data

interface OnboardingRepository {
    fun isOnboardingComplete(): Boolean
    suspend fun setOnboardingComplete(isComplete: Boolean)
    fun isAppSetAsDefaultLauncher(): Boolean
    fun setAppAsDefaultLauncher(isDefault: Boolean)
    fun checkSystemDefaultLauncher(): Boolean
    suspend fun setAwaitingUserAction(awaitingUserAction: Boolean)
    fun getAwaitingUserAction(): Boolean
}