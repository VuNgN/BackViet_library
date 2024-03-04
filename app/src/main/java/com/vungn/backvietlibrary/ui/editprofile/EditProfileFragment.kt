package com.vungn.backvietlibrary.ui.editprofile

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vungn.backvietlibrary.databinding.FragmentEditProfileBinding
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.editprofile.contract.impl.EditProfileViewModelImpl
import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.launch


class EditProfileFragment : FragmentBase<FragmentEditProfileBinding, EditProfileViewModelImpl>() {
    override fun getViewBinding(): FragmentEditProfileBinding =
        FragmentEditProfileBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<EditProfileViewModelImpl> =
        EditProfileViewModelImpl::class.java

    override fun setupListener() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                lifecycleScope.launch {
                    when (position) {
                        0 -> {
                            viewModel.gender.emit(true)
                        }

                        1 -> {
                            viewModel.gender.emit(false)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.etFullName.addTextChangedListener {
            lifecycleScope.launch {
                viewModel.name.emit(it?.trim().toString())
            }
        }
        binding.etAddress.addTextChangedListener {
            lifecycleScope.launch {
                viewModel.address.emit(it?.trim().toString())
            }
        }
        binding.etIdenrity.addTextChangedListener {
            lifecycleScope.launch {
                viewModel.identityCard.emit(it?.trim().toString())
            }
        }
        binding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateProfile()
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    it?.run {
                        binding.etFullName.setText(displayName)
                        binding.etAddress.setText(address)
                        binding.etIdenrity.setText(identityNo)
                        binding.spinner.setSelection(if (gender == true) 0 else 1)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.callApiState.collect {
                    when (it) {
                        CallApiState.LOADING -> {
                            binding.btnSend.isEnabled = false
                            binding.llEditProfile.visibility = View.GONE
                            binding.flProgressBar.visibility = View.VISIBLE
                        }

                        CallApiState.SUCCESS -> {
                            navController.popBackStack()
                            Toast.makeText(
                                requireContext(),
                                "Cập nhật thành công",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        CallApiState.ERROR -> {
                            binding.btnSend.isEnabled = true
                            binding.llEditProfile.visibility = View.VISIBLE
                            binding.flProgressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT)
                                .show()
                        }

                        CallApiState.NONE -> {}
                    }
                }
            }
        }
    }

    override fun setupViews() {
        val items = arrayOf("Nam", "Nữ")
        val dropdown: Spinner = binding.spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.setAdapter(adapter)
    }

    override fun onStop() {
        super.onStop()
        viewModel.release()
    }
}