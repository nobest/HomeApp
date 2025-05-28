package com.meticulous.homeapp.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager


enum class OnboardingState(val state: String) {
    NOT_STARTED("Not_started"),
    STARTED_STAGE_1("Started_stage_1"),
    STARTED_STAGE_2("Started_stage_2"),
    STARTED_AWAITING_USER_ACTION("Awaiting_user_action"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed")
}

object OnboardingManager {

    /***
     * Checks if the current app is set as the default home app
     */
    fun isHomeAppSetAsDefault(context: Context): Boolean {
        logD(message = "Utils.isHomeAppSetAsDefault called")
        val intent = Intent(Intent.ACTION_MAIN).also {
            it.addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfo =
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val defaultHomeApp = resolveInfo?.activityInfo?.packageName
        logI(message = "Utils.defaultHomeApp: $defaultHomeApp")
        logI(message = "Utils My HomeApp packageName: ${context.packageName}")
        val isSetAsDefault = resolveInfo?.activityInfo?.packageName == context.packageName
        logI(message = "Utils.defaultHomeApp: $defaultHomeApp")
        return isSetAsDefault
    }

    fun shouldShowOnboarding(context: Context): Boolean {
        val onboardingState = getOnboardingStateFromSharedPreference(context)
        val appSetAsDefault = isHomeAppSetAsDefault(context)
        return onboardingState != OnboardingState.COMPLETED.state || !appSetAsDefault
    }

    fun setOnboardingStartedStage1(context: Context) {
        saveOnboardingStateToSharedPreference(context, OnboardingState.STARTED_STAGE_1.state)
    }

    fun setOnboardingStartedStage2(context: Context) {
        saveOnboardingStateToSharedPreference(context, OnboardingState.STARTED_STAGE_2.state)
    }

    fun setOnboardingStateAwaitingUserAction(context: Context) {
        saveOnboardingStateToSharedPreference(
            context,
            OnboardingState.STARTED_AWAITING_USER_ACTION.state
        )
    }

    fun setOnboardingCancelled(context: Context) {
        saveOnboardingStateToSharedPreference(context, OnboardingState.CANCELLED.state)
    }

    fun markOnboardingComplete(context: Context) {
        saveOnboardingStateToSharedPreference(context, OnboardingState.COMPLETED.state)
    }

    fun isWaitingForUserAction(context: Context): Boolean {
        return getOnboardingStateFromSharedPreference(context) == OnboardingState.STARTED_AWAITING_USER_ACTION.state

    }

}