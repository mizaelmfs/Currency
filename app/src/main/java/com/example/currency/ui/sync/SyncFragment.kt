package com.example.currency.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.currency.R
import com.example.currency.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SyncFragment : Fragment() {

    private val viewModel: SyncViewModel by viewModel {
        parametersOf(findNavController())
    }
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        viewModel.start()

        viewModel.errorMsg.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.notNetwork.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), getString(R.string.not_network), Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }
}