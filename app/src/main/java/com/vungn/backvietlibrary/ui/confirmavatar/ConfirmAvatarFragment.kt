package com.vungn.backvietlibrary.ui.confirmavatar

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vungn.backvietlibrary.databinding.FragmentConfirmAvatarBinding
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.confirmavatar.contract.impl.ConfirmAvatarViewModelImpl
import com.vungn.backvietlibrary.util.enums.CallApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfirmAvatarFragment :
    FragmentBase<FragmentConfirmAvatarBinding, ConfirmAvatarViewModelImpl>() {
    private val args: ConfirmAvatarFragmentArgs by navArgs()
    override fun getViewBinding(): FragmentConfirmAvatarBinding {
        return FragmentConfirmAvatarBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<ConfirmAvatarViewModelImpl> {
        return ConfirmAvatarViewModelImpl::class.java
    }

    override fun setupListener() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.btnConfirm.setOnClickListener {
            viewModel.changeAvatar(args.avatarPath)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.callApiState.collect {
                    when (it) {
                        CallApiState.LOADING -> {
                            binding.btnConfirm.isEnabled = false
                            binding.flProgressBar.visibility = View.VISIBLE
                            binding.llConfirmAvatar.visibility = View.GONE
                        }

                        CallApiState.SUCCESS -> {
                            Toast.makeText(
                                requireContext(),
                                "Thay đổi ảnh thành công.",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        }

                        CallApiState.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                "Có lỗi trong quá trình thay đổi ảnh.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.btnConfirm.isEnabled = true
                            binding.flProgressBar.visibility = View.GONE
                            binding.llConfirmAvatar.visibility = View.VISIBLE
                        }

                        CallApiState.NONE -> {}
                    }
                }
            }
        }
    }

    override fun setupViews() {
        val avatarPath = args.avatarPath
        lifecycleScope.launch {
            Glide.with(this@ConfirmAvatarFragment)
                .load(avatarPath)
                .into(binding.ivAvatar)
        }
    }
}