package com.vungn.backvietlibrary.ui.account.contract

import com.vungn.backvietlibrary.db.entity.UserEntity
import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.flow.StateFlow

interface AccountViewModel {
    val user: StateFlow<UserEntity?>
    val callApiState: StateFlow<CallApiState>
    fun getUser()
}