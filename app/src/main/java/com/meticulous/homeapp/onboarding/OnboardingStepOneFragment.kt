package com.meticulous.homeapp.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meticulous.homeapp.R
import com.meticulous.homeapp.util.OnboardingManager

class OnboardingStepOneFragment : Fragment() {

    companion object {
        fun newInstance() = OnboardingStepOneFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding_step_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        OnboardingManager.setOnboardingStartedStage1(requireContext().applicationContext)
    }
}