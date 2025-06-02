package com.meticulous.homeapp.di

import com.meticulous.homeapp.onboarding.data.OnboardingRepository
import com.meticulous.homeapp.onboarding.domain.CheckIfAppIsDefaultUseCase
import com.meticulous.homeapp.onboarding.domain.GetWaitingUserActionUseCase
import com.meticulous.homeapp.onboarding.domain.IsOnboardingRequiredUseCase
import com.meticulous.homeapp.onboarding.domain.MarkOnboardingCompleteUseCase
import com.meticulous.homeapp.onboarding.domain.SetAppAsDefaultUseCase
import com.meticulous.homeapp.onboarding.domain.SetWaitingUserActionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun provideIsOnboardingRequiredUseCase(repository: OnboardingRepository): IsOnboardingRequiredUseCase {
        return IsOnboardingRequiredUseCase(repository)
    }

    @Provides
    fun provideMarkOnboardingCompleteUseCase(repository: OnboardingRepository): MarkOnboardingCompleteUseCase {
        return MarkOnboardingCompleteUseCase(repository)
    }

    @Provides
    fun provideCheckIfAppIsDefaultUseCase(repository: OnboardingRepository): CheckIfAppIsDefaultUseCase {
        return CheckIfAppIsDefaultUseCase(repository)
    }

    @Provides
    fun provideSetAppAsDefaultUseCase(repository: OnboardingRepository): SetAppAsDefaultUseCase {
        return SetAppAsDefaultUseCase(repository)
    }

    @Provides
    fun provideSetWaitingUserActionUseCase(repository: OnboardingRepository): SetWaitingUserActionUseCase {
        return SetWaitingUserActionUseCase(repository)
    }

    @Provides
    fun provideGetWaitingUserActionUseCase(repository: OnboardingRepository): GetWaitingUserActionUseCase {
        return GetWaitingUserActionUseCase(repository)
    }
}