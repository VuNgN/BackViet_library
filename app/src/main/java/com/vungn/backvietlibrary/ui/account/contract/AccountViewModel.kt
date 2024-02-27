package com.vungn.backvietlibrary.ui.account.contract

import com.vungn.backvietlibrary.model.data.UserValue
import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.flow.StateFlow

interface AccountViewModel {
    val user: StateFlow<UserValue?>
    val callApiState: StateFlow<CallApiState>
    fun getUser()
}