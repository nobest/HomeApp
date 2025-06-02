package com.meticulous.homeapp.onboarding.domain

import com.meticulous.homeapp.onboarding.data.OnboardingRepository

class IsOnboardingRequiredUseCase(private val onboardingRepository: OnboardingRepository) {
    operator fun invoke(): Boolean {
        // Onboarding required if onboarding isn't complete OR if it's complete
        // but the app is no longer the default launcher (user changed it outside)
        val isComplete = onboardingRepository.isOnboardingComplete()
        val isDefault = onboardingRepository.checkSystemDefaultLauncher()
        return !isComplete || !isDefault
    }
}

class MarkOnboardingCompleteUseCase(private val onboardingRepository: OnboardingRepository) {
    suspend operator fun invoke() {
        onboardingRepository.setOnboardingComplete(true)
        // Optionally ensure our tracked default status is also updated
        onboardingRepository.setAppAsDefaultLauncher(true)
    }
}

class CheckIfAppIsDefaultUseCase(private val onboardingRepository: OnboardingRepository) {
    operator fun invoke(): Boolean {
        return onboardingRepository.isAppSetAsDefaultLauncher()
    }
}

class SetAppAsDefaultUseCase(private val onboardingRepository: OnboardingRepository) {
    operator fun invoke(isDefault: Boolean) {
        onboardingRepository.setAppAsDefaultLauncher(isDefault)
    }
}

class SetWaitingUserActionUseCase(private val onboardingRepository: OnboardingRepository) {
    suspend operator fun invoke(awaitingUserAction: Boolean) {
        onboardingRepository.setAwaitingUserAction(awaitingUserAction)
    }
}

class GetWaitingUserActionUseCase(private val onboardingRepository: OnboardingRepository) {
    operator fun invoke(): Boolean {
        return onboardingRepository.getAwaitingUserAction()
    }
}