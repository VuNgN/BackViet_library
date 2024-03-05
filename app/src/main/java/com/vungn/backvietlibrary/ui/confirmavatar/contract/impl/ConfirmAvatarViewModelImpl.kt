package com.vungn.backvietlibrary.ui.confirmavatar.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.data.UserValue
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.UploadAvatarRepo
import com.vungn.backvietlibrary.ui.confirmavatar.contract.ConfirmAvatarViewModel
import com.vungn.backvietlibrary.util.enums.CallApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConfirmAvatarViewModelImpl @Inject constructor(
    private val uploadAvatarRepo: UploadAvatarRepo
) : ConfirmAvatarViewModel, ViewModel() {
    private val _callApiState: MutableStateFlow<CallApiState> = MutableStateFlow(CallApiState.NONE)
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState

    override fun changeAvatar(avatarPath: String) {
        _callApiState.value = CallApiState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(avatarPath)
            uploadAvatarRepo.setAvatar(file, object : BaseRepo.Callback<Response<UserValue>> {
                override fun onSuccess(data: Response<UserValue>) {
                    _callApiState.value = CallApiState.SUCCESS
                }

                override fun onError(error: Throwable) {
                    _callApiState.value = CallApiState.ERROR
                }

                override fun onRelease() {}
            })
        }
    }
}