package com.meticulous.homeapp.onboarding.presentation

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.meticulous.homeapp.R
import com.meticulous.homeapp.databinding.FragmentOnboardingBinding
import com.meticulous.homeapp.home.presentation.HomeActivity
import com.meticulous.homeapp.util.AnalyticLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val viewModel: OnboardingViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var changeSetting: ActivityResultLauncher<Intent>

    @Inject
    lateinit var logger: AnalyticLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changeSetting = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    logger.logDebug("OnboardingFragment registerForActivityResult result : $result")
                    // If user cancel change home app process, we update the process cancelled
                    if (result.resultCode == Activity.RESULT_CANCELED) {
                        viewModel.onWaitingUserActionCancelled()
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        val onboardingFragments = listOf(
            OnboardingStepOneFragment.newInstance(),
            OnboardingStepTwoFragment.newInstance(),
            OnboardingStepThreeFragment.newInstance()
        )

        val adapter = OnboardingViewPagerAdapter(
            onboardingFragments, childFragmentManager, lifecycle
        )
        binding.onboardingViewPager.adapter = adapter
        TabLayoutMediator(
            binding.onboardingTabIndicator, binding.onboardingViewPager
        ) { tab, position -> }.attach()

        // Prevent uses from swiping forward and backward so that the user can only navigate using
        // the Next button and this prevent backward navigation
        binding.onboardingViewPager.isUserInputEnabled = false

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("current_page", binding.onboardingViewPager.currentItem)
    }

    private fun handleUiState(state: OnboardingUiState) {
        logger.logInfo(message = "OnboardingFragment handleUiState called with state : $state")
        when (state) {
            OnboardingUiState.FirstStep -> {
                if (binding.onboardingViewPager.currentItem != OnboardingViewModel.ONBOARDING_FIRST_STEP) {
                    navigateToFirstOnboardingPage()
                }
            }

            OnboardingUiState.SecondStep -> {
                navigateToSecondOnboardingPage()
            }

            OnboardingUiState.ThirdStep -> {
                navigateToThirdOnboardingPage()
            }

            OnboardingUiState.AwaitingUserAction -> {
                openHomeScreenSettings()
                viewModel.updateWaitingUserAction()
            }

            OnboardingUiState.HomeActivity -> {
                openHomeActivity()
            }

            OnboardingUiState.Unknown -> {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                handleUiState(state)
            }
        }

        setupContinueButton()

        binding.onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == OnboardingViewModel.ONBOARDING_LAST_STEP) {
                    binding.btnNext.text = getString(R.string.finish)
                } else {
                    binding.btnNext.text = getString(R.string.next)
                }
            }
        })
    }

    private fun setupContinueButton() {
        binding.btnNext.setOnClickListener {
            val currentValue = binding.onboardingViewPager.currentItem
            when (currentValue) {
                OnboardingViewModel.ONBOARDING_FIRST_STEP -> {
                    viewModel.onFirstStepNextClicked()
                }

                OnboardingViewModel.ONBOARDING_SECOND_STEP -> {
                    logger.logInfo(message = "OnboardingFragment ONBOARDING_SECOND_STEP continue button clicked")
                    viewModel.onSecondStepNextClicked()
                }

                OnboardingViewModel.ONBOARDING_LAST_STEP -> {
                    viewModel.onThirdStepNextClicked()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        logger.logInfo(message = "OnboardingFragment onResume called")
        viewModel.onViewResumed()
    }

    private fun navigateToFirstOnboardingPage() {
        logger.logInfo(message = "OnboardingFragment.navigateToFirstOnboardingPage called")
        binding.onboardingViewPager.currentItem = OnboardingViewModel.ONBOARDING_FIRST_STEP
    }

    private fun navigateToSecondOnboardingPage() {
        logger.logInfo(message = "OnboardingFragment.navigateToSecondOnboardingPage called")
        binding.onboardingViewPager.currentItem = OnboardingViewModel.ONBOARDING_SECOND_STEP
    }

    private fun navigateToThirdOnboardingPage() {
        logger.logInfo(message = "OnboardingFragment.navigateToThirdOnboardingPage called")
        binding.onboardingViewPager.currentItem = OnboardingViewModel.ONBOARDING_LAST_STEP
    }

    private fun openHomeActivity() {
        logger.logInfo(message = "OnboardingFragment.openHomeActivity called")
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun openHomeScreenSettings(): Boolean {
        logger.logInfo(message = "OnboardingFragment.openHomeScreenSettings called")
        return try {
            val intent = Intent().also {
                it.action = Settings.ACTION_HOME_SETTINGS
            }
            changeSetting.launch(intent)
            true
        } catch (e: Exception) {
            logger.logError(message = e.localizedMessage.orEmpty(), e)
            e.printStackTrace()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}