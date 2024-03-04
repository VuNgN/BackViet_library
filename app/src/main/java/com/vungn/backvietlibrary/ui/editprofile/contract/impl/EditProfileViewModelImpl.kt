package com.vungn.backvietlibrary.ui.editprofile.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.UserEntity
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.data.UserValue
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.EditProfileRepo
import com.vungn.backvietlibrary.model.repo.GetUserRepo
import com.vungn.backvietlibrary.ui.editprofile.contract.EditProfileViewModel
import com.vungn.backvietlibrary.util.enums.CallApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModelImpl @Inject constructor(
    private val editProfileRepo: EditProfileRepo,
    private val getUserRepo: GetUserRepo
) : EditProfileViewModel, ViewModel() {
    private val _user: MutableStateFlow<UserEntity?> = MutableStateFlow(null)
    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    private val _gender: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _address: MutableStateFlow<String> = MutableStateFlow("")
    private val _identityCard: MutableStateFlow<String> = MutableStateFlow("")
    private val _callApiState: MutableStateFlow<CallApiState> = MutableStateFlow(CallApiState.NONE)
    override val user: MutableStateFlow<UserEntity?>
        get() = _user
    override val name: MutableStateFlow<String>
        get() = _name
    override val gender: MutableStateFlow<Boolean>
        get() = _gender
    override val address: MutableStateFlow<String>
        get() = _address
    override val identityCard: MutableStateFlow<String>
        get() = _identityCard
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getUserRepo.getFromDatabase().collect { user ->
                _user.value = user
            }
        }
    }

    override fun updateProfile() {
        _callApiState.value = CallApiState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            val newUser = _user.value?.let {
                UserValue(
                    id = it.id,
                    displayName = _name.value,
                    gender = _gender.value,
                    address = _address.value,
                    identityNo = _identityCard.value,
                    avatar = it.avatar,
                    isLock = it.isLock
                )
            }
            if (newUser == null) {
                _callApiState.value = CallApiState.ERROR
                return@launch
            }
            editProfileRepo.updateUser(
                newUser,
                object : BaseRepo.Callback<Response<UserValue>> {
                    override fun onSuccess(data: Response<UserValue>) {
                        _callApiState.value = CallApiState.SUCCESS
                    }

                    override fun onError(error: Throwable) {
                        _callApiState.value = CallApiState.ERROR
                        error.printStackTrace()
                    }

                    override fun onRelease() {}
                })
        }
    }

    override fun release() {
        viewModelScope.launch {
            _callApiState.value = CallApiState.NONE
        }
    }
}