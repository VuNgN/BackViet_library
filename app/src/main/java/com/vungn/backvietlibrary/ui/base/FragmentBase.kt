package com.vungn.backvietlibrary.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class FragmentBase<Binding : ViewBinding, ViewModel : androidx.lifecycle.ViewModel>() :
    Fragment() {
    open var useSharedViewModel: Boolean = false
    protected lateinit var binding: Binding
    protected abstract fun getViewBinding(): Binding

    protected lateinit var viewModel: ViewModel
    protected abstract fun getViewModelClass(): Class<ViewModel>

    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        binding = getViewBinding()
        viewModel = ViewModelProvider(requireActivity())[getViewModelClass()]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupViews()
        setupListener()
    }

    abstract fun setupListener()

    abstract fun setupViews()

    override fun onDestroy() {
        super.onDestroy()
    }
}