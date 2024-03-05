package com.vungn.backvietlibrary.ui.confirmavatar.contract

import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.flow.StateFlow

interface ConfirmAvatarViewModel {
    val callApiState: StateFlow<CallApiState>
    fun changeAvatar(avatarPath: String)
}