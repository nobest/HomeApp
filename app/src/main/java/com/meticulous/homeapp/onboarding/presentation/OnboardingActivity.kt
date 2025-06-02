package com.meticulous.homeapp.onboarding.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.meticulous.homeapp.databinding.ActivityOnboardingBinding
import com.meticulous.homeapp.home.presentation.HomeActivity
import com.meticulous.homeapp.util.AnalyticLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val viewModel: OnboardingActivityViewModel by viewModels()

    @Inject
    lateinit var logger: AnalyticLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger.logInfo(message = "OnboardingActivity onCreate called")
        collectStartPoint()

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add callback to listen for the back button press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
        logger.logInfo(message = "OnboardingActivity onCreate complete")
    }

    private fun collectStartPoint() {
        lifecycleScope.launch {
            viewModel.startPoint.collect { state ->
                when (state) {
                    StartPoint.HomeActivity -> {
                        logger.logDebug(message = "OnboardingActivity onCreate HomeApp set default, opening HomeActivity")
                        openHomeActivity()
                        finish()
                    }

                    else -> {
                        logger.logDebug(message = "OnboardingActivity onCreate received start point: $state")
                    }
                }
            }
        }
    }

    private fun openHomeActivity() {
        logger.logInfo(message = "OnboardingActivity.openHomeActivity called")
        val home = Intent(this, HomeActivity::class.java)
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(home)
    }
}