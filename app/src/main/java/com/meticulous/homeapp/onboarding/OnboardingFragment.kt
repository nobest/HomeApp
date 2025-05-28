package com.meticulous.homeapp.onboarding

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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.meticulous.homeapp.R
import com.meticulous.homeapp.databinding.FragmentOnboardingBinding
import com.meticulous.homeapp.home.HomeActivity
import com.meticulous.homeapp.util.logD
import com.meticulous.homeapp.util.logE
import com.meticulous.homeapp.util.logI

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val viewModel: OnboardingViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var changeSetting: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changeSetting = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    logD("OnboardingFragment registerForActivityResult result : $result")
                    // If user cancel change home app process, we update the process cancelled
                    if (result.resultCode == Activity.RESULT_CANCELED) {
                        viewModel.onWaitingUserActionCancelled(requireContext().applicationContext)
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

        observeNextDestination()

        return binding.root
    }

    // Observe the next destination to navigate to the appropriate page on the app
    private fun observeNextDestination() {
        viewModel.currentDestination.observe(viewLifecycleOwner) {
            when (it) {
                Destination.ONBOARDING_FIRST_STEP -> {
                    logI(message = "OnboardingFragment.observeNextDestination Destination.ONBOARDING_FIRST_STEP")
                    if (binding.onboardingViewPager.currentItem != OnboardingViewModel.ONBOARDING_FIRST_STEP) {
                        navigateToFirstOnboardingPage()
                    }
                }

                Destination.ONBOARDING_SECOND_STEP -> {
                    logI(message = "OnboardingFragment.observeNextDestination Destination.ONBOARDING_SECOND_STEP")
                    navigateToSecondOnboardingPage()
                }

                Destination.ONBOARDING_THIRD_STEP -> {
                    logI(message = "OnboardingFragment.observeNextDestination Destination.ONBOARDING_THIRD_STEP")
                    navigateToThirdOnboardingPage()
                }

                Destination.HOME_ACTIVITY -> {
                    logI(message = "OnboardingFragment.observeNextDestination Destination.HOME_ACTIVITY")
                    openHomeActivity()
                }

                Destination.ONBOARDING_AWAITING_USER_ACTION -> {
                    logI(message = "OnboardingFragment.observeNextDestination Destination.ONBOARDING_AWAITING_USER_ACTION")
                    openHomeScreenSettings()
                }

                Destination.UNKNOWN -> {
                    logI(message = "OnboardingFragment.observeNextDestination Destination.UNKNOWN")
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            val currentValue = binding.onboardingViewPager.currentItem
            when (currentValue) {
                OnboardingViewModel.ONBOARDING_FIRST_STEP -> {
                    viewModel.onFirstStepNextClicked()
                }

                OnboardingViewModel.ONBOARDING_SECOND_STEP -> {
                    viewModel.onSecondStepNextClicked()
                    viewModel.updateWaitingUserAction(requireContext().applicationContext)
                }

                OnboardingViewModel.ONBOARDING_LAST_STEP -> {
                    viewModel.onThirdStepNextClicked()
                }
            }

        }

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

    override fun onResume() {
        super.onResume()
        logI(message = "OnboardingFragment onResume called")
        viewModel.onViewResumed(requireContext())
    }

    private fun navigateToFirstOnboardingPage() {
        logI(message = "OnboardingFragment.navigateToFirstOnboardingPage called")
        binding.onboardingViewPager.currentItem = OnboardingViewModel.ONBOARDING_FIRST_STEP
    }

    private fun navigateToSecondOnboardingPage() {
        logI(message = "OnboardingFragment.navigateToSecondOnboardingPage called")
        binding.onboardingViewPager.currentItem = OnboardingViewModel.ONBOARDING_SECOND_STEP
    }

    private fun navigateToThirdOnboardingPage() {
        logI(message = "OnboardingFragment.navigateToThirdOnboardingPage called")
        binding.onboardingViewPager.currentItem = OnboardingViewModel.ONBOARDING_LAST_STEP
    }

    private fun openHomeActivity() {
        logI(message = "OnboardingFragment.openHomeActivity called")
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun openHomeScreenSettings(): Boolean {
        logI(message = "OnboardingFragment.openHomeScreenSettings called")
        return try {
            val intent = Intent().also {
                it.action = Settings.ACTION_HOME_SETTINGS
            }
            changeSetting.launch(intent)
            true
        } catch (e: Exception) {
            logE(message = e.localizedMessage.orEmpty(), e)
            e.printStackTrace()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}