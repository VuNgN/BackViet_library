package com.vungn.backvietlibrary.ui.editprofile.contract

import com.vungn.backvietlibrary.db.entity.UserEntity
import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface EditProfileViewModel {
    val user: MutableStateFlow<UserEntity?>
    val name: MutableStateFlow<String>
    val gender: MutableStateFlow<Boolean>
    val address: MutableStateFlow<String>
    val identityCard: MutableStateFlow<String>
    val callApiState: StateFlow<CallApiState>

    fun updateProfile()
    fun release()
}