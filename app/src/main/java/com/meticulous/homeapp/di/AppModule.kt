package com.meticulous.homeapp.di

import android.content.Context
import com.meticulous.homeapp.home.data.HomeRepository
import com.meticulous.homeapp.home.data.HomeRepositoryImpl
import com.meticulous.homeapp.onboarding.data.OnboardingRepository
import com.meticulous.homeapp.onboarding.data.OnboardingRepositoryImpl
import com.meticulous.homeapp.util.AnalyticLogger
import com.meticulous.homeapp.util.AnalyticLoggerImpl
import com.meticulous.homeapp.util.PreferencesHelper
import com.meticulous.homeapp.util.PreferencesHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesHelper(@ApplicationContext context: Context): PreferencesHelper {
        return PreferencesHelperImpl(context)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(@ApplicationContext context: Context): HomeRepository {
        return HomeRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        preferencesHelper: PreferencesHelper,
        @ApplicationContext context: Context
    ): OnboardingRepository {
        return OnboardingRepositoryImpl(preferencesHelper, context)
    }

    @Provides
    @Singleton
    fun provideAnalyticLogger(): AnalyticLogger {
        return AnalyticLoggerImpl()
    }
}