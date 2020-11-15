package com.example.currency.ui.conversion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.currency.R
import com.example.currency.databinding.FragmentConversionBinding
import com.example.currency.ui.MainActivity
import com.example.currency.util.getDate
import com.example.network.model.Currency
import com.example.network.model.SelectCurrency
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversionFragment : Fragment() {

    private val viewModel: ConversionViewModel by viewModel()

    private lateinit var binding: FragmentConversionBinding
    private var valueTo: Currency? = null
    private var valueFrom: Currency? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversion, container, false)

        initViews()
        observables()
        viewModel.getData()
        return binding.root
    }

    private fun initViews() {
        (activity as MainActivity).setSupportActionBar(binding.layoutToolbar.toolbar)

        binding.btnFrom.setOnClickListener {
            val bundle = bundleOf("value" to SelectCurrency.FROM)
            findNavController().navigate(R.id.action_navigation_conversion_to_navigation_list, bundle)
        }

        binding.btnTo.setOnClickListener {
            val bundle = bundleOf("value" to SelectCurrency.TO)
            findNavController().navigate(R.id.action_navigation_conversion_to_navigation_list, bundle)
        }

        binding.edtValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().trim().replace(".", "")
                if (value.isNotEmpty()) {
                    viewModel.calcQuote(valueFrom?.key, valueTo?.key, value.toDouble())
                } else {
                    viewModel.calcQuote(valueFrom?.key, valueTo?.key, null)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun observables() {
        viewModel.responseQuotes.observe(viewLifecycleOwner, {
            val dateFormat = getString(R.string.last_update).format(getDate(it.timestamp))
            (activity as MainActivity).supportActionBar?.subtitle = dateFormat
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.txtResult.observe(viewLifecycleOwner, {
            binding.txtResult.text = it
        })

        viewModel.txtCalc.observe(viewLifecycleOwner, {
            binding.txtResult.text = getString(R.string.result).format(String.format("%.2f", it.toFloat()))
        })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Currency>("fromCurrency")
            ?.observe(viewLifecycleOwner, {
                valueFrom = it
                val value = "${it.value} ${it.key}"
                binding.btnFrom.text = value
            })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Currency>("toCurrency")
            ?.observe(viewLifecycleOwner, {
                valueTo = it
                val value = "${it.value} ${it.key}"
                binding.btnTo.text = value
            })
    }
}