package com.meticulous.homeapp.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.meticulous.homeapp.databinding.ActivityOnboardingBinding
import com.meticulous.homeapp.home.HomeActivity
import com.meticulous.homeapp.util.isHomeAppSetAsDefault
import com.meticulous.homeapp.util.logD
import com.meticulous.homeapp.util.logI
import com.meticulous.homeapp.util.getOnboardingStateFromSharedPref

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logI(message = "OnboardingActivity onCreate called")
        val onboardingProcessComplete = getOnboardingStateFromSharedPref(this)
        logI(message = "OnboardingActivity onCreate onboardingProcessComplete: $onboardingProcessComplete")
        if (isHomeAppSetAsDefault(this) && onboardingProcessComplete) {
            logD(message = "OnboardingActivity onCreate HomeApp set default, opening HomeActivity")
            openHomeActivity()
            finish()
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add callback to listen for the back button press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
        logI(message = "OnboardingActivity onCreate complete")
    }

    private fun openHomeActivity() {
        logI(message = "OnboardingActivity.openHomeActivity called")
        val home = Intent(this, HomeActivity::class.java)
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(home)
    }
}