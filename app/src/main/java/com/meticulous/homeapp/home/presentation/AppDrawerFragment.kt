package com.meticulous.homeapp.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.meticulous.homeapp.databinding.FragmentAppDrawerBinding
import com.meticulous.homeapp.home.domain.App
import com.meticulous.homeapp.util.AnalyticLogger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppDrawerFragment : Fragment() {

    private val viewModel: AppDrawerViewModel by viewModels()

    private var _binding: FragmentAppDrawerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var logger: AnalyticLogger

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawer = binding.rvAppDrawer
        drawer.layoutManager = GridLayoutManager(requireContext(), 4)
        viewModel.filteredApps.observe(viewLifecycleOwner) {
            drawer.adapter = AppDrawerRecyclerAdapter(it) { app ->
                logger.logDebug(message = "AppDrawerFragment clicked app = ${app.appName}")
                launchApp(app)
            }
        }

        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.onSearchQueryChanged(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText.orEmpty())
                return true
            }
        })
    }

    private fun launchApp(app: App) {
        logger.logInfo(message = "AppDrawerFragment launchApp called app: ${app.appName}")
        val context = requireContext()
        context.packageManager.getLaunchIntentForPackage(app.packageName)?.also { intent ->
            context.startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}