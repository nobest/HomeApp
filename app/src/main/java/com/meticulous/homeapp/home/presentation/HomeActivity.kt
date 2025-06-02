package com.meticulous.homeapp.home.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.meticulous.homeapp.R
import com.meticulous.homeapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_home)

        ViewCompat.setOnApplyWindowInsetsListener(binding.home) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Add callback to listen for the back button press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed(navController)
            }
        })
    }

    private fun handleBackPressed(navController: NavController) {
        val currentView = navController.currentBackStackEntry?.destination?.id
        // If the user is on the App Drawer page and click the back button we close the app drawer
        // to go back to the Home fragment which is the device dashboard
        if (currentView == R.id.AppDrawerFragment) {
            navController.navigateUp()
        }
    }
}