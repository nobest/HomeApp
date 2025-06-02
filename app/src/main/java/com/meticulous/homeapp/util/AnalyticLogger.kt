package com.meticulous.homeapp.util

interface AnalyticLogger {
    fun logDebug(message: String)
    fun logInfo(message: String)
    fun logError(message: String, exception: Exception? = null)
}