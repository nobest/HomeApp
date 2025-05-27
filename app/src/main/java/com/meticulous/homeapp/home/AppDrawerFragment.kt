package com.meticulous.homeapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.meticulous.homeapp.databinding.FragmentAppDrawerBinding
import com.meticulous.homeapp.home.model.App
import com.meticulous.homeapp.util.logD
import com.meticulous.homeapp.util.logI

class AppDrawerFragment : Fragment() {

    private val viewModel: AppDrawerViewModel by viewModels()

    private var _binding: FragmentAppDrawerBinding? = null
    private val binding get() = _binding!!

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
                logD(message = "AppDrawerFragment clicked app = ${app.appName}")
                launchApp(app)
            }
        }
    }

    private fun launchApp(app: App) {
        logI(message = "AppDrawerFragment launchApp called app: ${app.appName}")
        val context = requireContext()
        context.packageManager.getLaunchIntentForPackage(app.packageName)?.also { intent ->
            context.startActivity(intent)
        }
//        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}