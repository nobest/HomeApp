package com.meticulous.homeapp.onboarding.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.meticulous.homeapp.util.PreferencesHelper
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
    private val appContext: Context
) : OnboardingRepository {

    override fun isOnboardingComplete(): Boolean {
        return preferencesHelper.isOnboardingComplete()
    }

    override suspend fun setOnboardingComplete(isComplete: Boolean) {
        preferencesHelper.setOnboardingComplete(isComplete)
    }

    override fun isAppSetAsDefaultLauncher(): Boolean {
        return preferencesHelper.isAppSetAsDefaultLauncher()
    }

    override fun setAppAsDefaultLauncher(isDefault: Boolean) {
        preferencesHelper.setAppAsDefaultLauncher(isDefault)
    }

    override fun checkSystemDefaultLauncher(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = appContext.packageManager.resolveActivity(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        val isDefault = resolveInfo?.activityInfo?.packageName == appContext.packageName
        // Also update our internal tracking if it's different
        if (isAppSetAsDefaultLauncher() != isDefault) {
            setAppAsDefaultLauncher(isDefault)
        }
        return isDefault
    }

    override suspend fun setAwaitingUserAction(awaitingUserAction: Boolean) {
        preferencesHelper.setAwaitingUserAction(awaitingUserAction)
    }

    override fun getAwaitingUserAction(): Boolean {
        return preferencesHelper.getAwaitingUserAction()
    }
}