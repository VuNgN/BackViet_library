package com.vungn.backvietlibrary.ui.account.contract.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vungn.backvietlibrary.model.data.UserResponse
import com.vungn.backvietlibrary.model.data.UserValue
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.GetUserRepo
import com.vungn.backvietlibrary.ui.account.contract.AccountViewModel
import com.vungn.backvietlibrary.util.enums.CallApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModelImpl @Inject constructor(
    private val userRepo: GetUserRepo,
    private val gson: Gson
) : ViewModel(),
    AccountViewModel {
    private val _user: MutableStateFlow<UserValue?> = MutableStateFlow(null)
    private val _callApiState: MutableStateFlow<CallApiState> =
        MutableStateFlow(CallApiState.LOADING)
    override val user: StateFlow<UserValue?>
        get() = _user
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState

    init {
        getUser()
    }

    override fun getUser() {
        viewModelScope.launch {
            userRepo.execute(object : BaseRepo.Callback<UserResponse> {
                override fun onSuccess(data: UserResponse) {
                    Log.d(TAG, "onSuccess: ${gson.toJson(data)}")
                    _user.value = data.value
                    _callApiState.value = CallApiState.SUCCESS
                }

                override fun onError(error: Throwable) {
                    error.printStackTrace()
                    _callApiState.value = CallApiState.ERROR
                }

                override fun onRelease() {}
            }).launchIn(viewModelScope)
        }
    }

    companion object {
        private const val TAG = "AccountViewModelImpl"
    }
}