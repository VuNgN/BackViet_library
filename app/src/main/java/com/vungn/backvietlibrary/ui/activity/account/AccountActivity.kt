package com.vungn.backvietlibrary.ui.activity.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.account.contract.AccountActivityViewModel
import com.vungn.backvietlibrary.ui.activity.account.contract.impl.AccountActivityViewModelImpl
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity.Companion.RESULT_BUNDLE_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {
    private val vm: AccountActivityViewModel by viewModels<AccountActivityViewModelImpl>()
    private val startLoginActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        activityResultCallback()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.networkState.collect {
                    if (it is NetworkState.UNAUTHORIZED) {
                        val intent = Intent(this@AccountActivity, AuthActivity::class.java)
                        startLoginActivity.launch(intent)
                    }
                }
            }
        }
    }

    private fun activityResultCallback() = ActivityResultCallback<ActivityResult> {
        if (it.resultCode == RESULT_OK) {
            val bundle = it.data?.extras
            val isLoginSuccess = bundle?.getBoolean(RESULT_BUNDLE_KEY, false)
            if (isLoginSuccess != true) {
                finish()
            }
        }
    }
}