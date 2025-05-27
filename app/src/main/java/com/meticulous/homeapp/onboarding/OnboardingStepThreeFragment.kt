package com.meticulous.homeapp.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meticulous.homeapp.R

class OnboardingStepThreeFragment : Fragment() {

    companion object {
        fun newInstance() = OnboardingStepThreeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding_step_three, container, false)
    }
}