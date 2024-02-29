package com.vungn.backvietlibrary.ui.upgrade

import com.vungn.backvietlibrary.databinding.FragmentUpgradeBinding
import com.vungn.backvietlibrary.ui.activity.main.MainActivity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.upgrade.contract.UpgradeViewModelImpl

class UpgradeFragment : FragmentBase<FragmentUpgradeBinding, UpgradeViewModelImpl>() {
    override fun getViewBinding(): FragmentUpgradeBinding {
        return FragmentUpgradeBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<UpgradeViewModelImpl> {
        return UpgradeViewModelImpl::class.java
    }

    override fun setupListener() {}

    override fun setupViews() {
        (requireActivity() as MainActivity).setTopBarTitle("Upgrade")
    }
}