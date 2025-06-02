package com.meticulous.homeapp.home.domain

import android.graphics.drawable.Drawable

data class App(
    val appName: String,
    val appIcon: Drawable,
    val packageName: String
)