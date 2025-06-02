package com.meticulous.homeapp.home.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.meticulous.homeapp.home.data.HomeRepository
import com.meticulous.homeapp.home.domain.App
import com.meticulous.homeapp.util.AnalyticLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppDrawerViewModel @Inject constructor(
    app: Application,
    private val repository: HomeRepository,
    private val logger: AnalyticLogger,
) :
    AndroidViewModel(app) {
    // Bind this to the serachView for the apps and it should work seamlessly
    // The filtering process has been implemented already
    private val _searchQuery: MutableLiveData<String> = MutableLiveData("")
    private val searchQuery: LiveData<String> = _searchQuery

    private val _installedApps: MutableLiveData<List<App>> = MutableLiveData()
    val installedApps: LiveData<List<App>> = _installedApps

    private val _filteredApps = searchQuery.switchMap { query ->
        if (query.isEmpty()) {
            logger.logInfo(message = "query is empty returning ${installedApps.value}")
            return@switchMap installedApps
        }
        logger.logInfo(message = "query is NOT empty query: $query")
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
        viewModelScope.launch {
            val apps = repository.getInstalledApps()
            logger.logDebug(message = "getInstalledApps return: ${apps.size} apps")
            _installedApps.value = apps
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}