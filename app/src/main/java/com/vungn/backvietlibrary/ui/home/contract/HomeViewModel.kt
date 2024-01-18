package com.vungn.backvietlibrary.ui.home.contract

import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val avatar: StateFlow<String>
}