package com.meticulous.homeapp.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.meticulous.homeapp.home.model.App
import timber.log.Timber

fun logD(message: String) {
    Timber.d(message)
}

fun logI(message: String) {
    Timber.i(message)
}

fun logE(message: String, e: Exception? = null) {
    Timber.e(e, message)
}

fun queryInstalledApps(context: Context): List<App> {
    val packageManager = context.packageManager

    val apps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        queryInstalledAppsPostTIRAMISU(context, packageManager)
    } else {
        queryInstalledAppsPreTIRAMISU(packageManager)
    }
    logI(message = "Utils SEMIU installed apps: $apps")
    return apps

}

private fun queryInstalledAppsPreTIRAMISU(packageManager: PackageManager): List<App> {
    return packageManager.getInstalledApplications(0)
        .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }.map {
            App(
                appName = packageManager.getApplicationLabel(it).toString(),
                appIcon = packageManager.getApplicationIcon(it),
                packageName = it.packageName
            )
        }.sortedBy { it.appName }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun queryInstalledAppsPostTIRAMISU(
    context: Context, packageManager: PackageManager
): List<App> {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)

    val flags = PackageManager.ResolveInfoFlags.of(
        PackageManager.MATCH_ALL.toLong()
    )
    return context.packageManager.queryIntentActivities(intent, flags).map { resolveInfo ->
        App(
            appName = resolveInfo.loadLabel(packageManager).toString(),
            packageName = resolveInfo.activityInfo.packageName,
            appIcon = resolveInfo.loadIcon(packageManager)
        )
    }.sortedBy { it.appName }
}
