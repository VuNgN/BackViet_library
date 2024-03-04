package com.vungn.backvietlibrary.ui.account.contract.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vungn.backvietlibrary.db.entity.UserEntity
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.data.UserValue
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.GetUserRepo
import com.vungn.backvietlibrary.ui.account.contract.AccountViewModel
import com.vungn.backvietlibrary.util.enums.CallApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModelImpl @Inject constructor(
    private val userRepo: GetUserRepo,
    private val gson: Gson
) : ViewModel(),
    AccountViewModel {
    private val _user: MutableStateFlow<UserEntity?> = MutableStateFlow(null)
    private val _callApiState: MutableStateFlow<CallApiState> =
        MutableStateFlow(CallApiState.LOADING)
    override val user: StateFlow<UserEntity?>
        get() = _user
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState

    override fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.execute(object : BaseRepo.Callback<Response<UserValue>> {
                override fun onSuccess(data: Response<UserValue>) {
                    Log.d(TAG, "onSuccess: ${gson.toJson(data)}")
//                    _user.value = data.value
                    _callApiState.value = CallApiState.SUCCESS
                }

                override fun onError(error: Throwable) {
                    error.printStackTrace()
                    _callApiState.value = CallApiState.ERROR
                }

                override fun onRelease() {}
            }).launchIn(viewModelScope)
        }
        viewModelScope.launch {
            userRepo.getFromDatabase().stateIn(viewModelScope).collect {
                Log.d(TAG, "getUser: ${gson.toJson(it)}")
                _user.emit(it)
            }
        }
    }

    companion object {
        private const val TAG = "AccountViewModelImpl"
    }
}