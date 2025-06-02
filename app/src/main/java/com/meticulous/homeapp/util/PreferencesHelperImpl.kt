package com.meticulous.homeapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesHelperImpl(context: Context) : PreferencesHelper {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("homeAppPrefFile", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_COMPLETE = "onboardingComplete"
        private const val KEY_APP_DEFAULT_LAUNCHER = "appDefaultLauncher"
        private const val KEY_AWAITING_USER_ACTION = "onboardAwaitingUserAction"
    }

    override fun isOnboardingComplete(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETE, false)
    }

    override fun setOnboardingComplete(isComplete: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_ONBOARDING_COMPLETE, isComplete) }
    }

    override fun isAppSetAsDefaultLauncher(): Boolean {
        return sharedPreferences.getBoolean(KEY_APP_DEFAULT_LAUNCHER, false)
    }

    override fun setAppAsDefaultLauncher(isDefault: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_APP_DEFAULT_LAUNCHER, isDefault) }
    }

    override fun setAwaitingUserAction(isAwaiting: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_AWAITING_USER_ACTION, isAwaiting)
        }
    }

    override fun getAwaitingUserAction(): Boolean {
        return sharedPreferences.getBoolean(KEY_AWAITING_USER_ACTION, false)
    }
}