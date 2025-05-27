package com.meticulous.homeapp.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.meticulous.homeapp.home.model.App
import com.meticulous.homeapp.util.logD
import com.meticulous.homeapp.util.logI
import com.meticulous.homeapp.util.queryInstalledApps


class AppDrawerViewModel(val app: Application) : AndroidViewModel(app) {
    // Bind this to the serachView for the apps and it should work seamlessly
    // The filtering process has been implemented already
    private val _searchQuery: LiveData<String> = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _installedApps: MutableLiveData<List<App>> = MutableLiveData()
    val installedApps: LiveData<List<App>> = _installedApps

    private val _filteredApps = searchQuery.switchMap { query ->
        if (query.isEmpty()) {
            logI(message = "query is empty returning ${installedApps.value}")
            return@switchMap installedApps
        }
        logI(message = "query is NOT empty query: $query")
        val filtered = installedApps.value?.filter { app ->
            app.appName.contains(query, ignoreCase = true)
        }.orEmpty()
        MutableLiveData(filtered)
    }

    val filteredApps: LiveData<List<App>> = _filteredApps

    init {
        getInstalledApps()
    }


    private fun getInstalledApps() {
        val apps = queryInstalledApps(app.baseContext)
        val withoutHomeApp = apps.filter { it.packageName != app.baseContext.packageName }
        logD(message = "getInstalledApps return: ${apps.size} apps")
        _installedApps.value = withoutHomeApp
    }
}