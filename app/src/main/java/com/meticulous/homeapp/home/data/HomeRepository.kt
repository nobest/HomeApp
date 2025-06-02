package com.meticulous.homeapp.home.data

import com.meticulous.homeapp.home.domain.App

interface HomeRepository {
    suspend fun getInstalledApps(): List<App>
}