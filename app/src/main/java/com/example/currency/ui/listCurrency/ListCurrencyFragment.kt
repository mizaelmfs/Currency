package com.example.currency.ui.listCurrency

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.currency.R
import com.example.currency.databinding.FragmentListCurrencyBinding
import com.example.network.model.SelectCurrency
import com.example.currency.ui.MainActivity
import com.example.currency.ui.conversion.ConversionFragmentArgs
import com.example.currency.ui.listCurrency.adapter.CurrencyAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListCurrencyFragment : Fragment() {

    private val viewModel : ListCurrencyViewModel by viewModel()
    private lateinit var binding : FragmentListCurrencyBinding
    private lateinit var adapter: CurrencyAdapter

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_currency, container, false)

        iniViews()
        observables()
        viewModel.getSearchData()

        return binding.root
    }

    private fun iniViews() {

        setHasOptionsMenu(true)
        (activity as MainActivity).setSupportActionBar(binding.layoutToolbar.toolbar)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)

        adapter = CurrencyAdapter()
        binding.rcCurrencies.adapter = adapter

        adapter.setOnClickListener { currency, _ ->
            arguments?.let {
                val safeArgs = ConversionFragmentArgs.fromBundle(it)
                if (safeArgs.value == SelectCurrency.FROM) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("fromCurrency", currency)
                } else {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("toCurrency", currency)
                }
            }
            findNavController().popBackStack()
        }
    }

    private fun observables() {
        viewModel.responseCurrency.observe(viewLifecycleOwner, Observer {
            adapter.setData(it.currencies)
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, Observer {
           Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.progress.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        })

        viewModel.listEmpty.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.rcCurrencies.visibility = View.GONE
                binding.txtEmpty.visibility = View.VISIBLE
            } else {
                binding.rcCurrencies.visibility = View.VISIBLE
                binding.txtEmpty.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_currency, menu)
        val searchItem = menu.findItem(R.id.search)

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.getSearchData(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.getSearchData(query)
                    return true
                }
            }
            searchView?.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search ->
                return false
            else -> {
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }
}