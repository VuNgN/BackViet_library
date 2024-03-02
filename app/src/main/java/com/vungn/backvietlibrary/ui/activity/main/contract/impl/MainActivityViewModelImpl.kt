package com.vungn.backvietlibrary.ui.activity.main.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.model.data.CategoryResponse
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.GetCategoriesRepo
import com.vungn.backvietlibrary.model.repo.GetUserRepo
import com.vungn.backvietlibrary.network.NetworkEvent
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.main.contract.MainActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModelImpl @Inject constructor(
    networkEvent: NetworkEvent,
    private val categoryRepo: GetCategoriesRepo,
    private val userRepo: GetUserRepo
) : ViewModel(),
    MainActivityViewModel {
    private val _networkState: StateFlow<NetworkState> = networkEvent.observableNetworkState
    override val networkState: StateFlow<NetworkState>
        get() = _networkState
    private val _avatar: MutableStateFlow<String?> = MutableStateFlow(null)
    override val avatar: StateFlow<String?>
        get() = _avatar
    private val _categories: MutableStateFlow<List<CategoryEntity>> = MutableStateFlow(emptyList())
    override val categories: StateFlow<List<CategoryEntity>>
        get() = _categories

    init {
        viewModelScope.launch {
            userRepo.getFromDatabase().stateIn(viewModelScope).collect {
                _avatar.value = it.avatar
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepo.getFromDatabase().stateIn(viewModelScope).collect {
                _categories.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepo.execute(object : BaseRepo.Callback<CategoryResponse> {
                override fun onSuccess(data: CategoryResponse) {}

                override fun onError(error: Throwable) {
                    error.printStackTrace()
                }

                override fun onRelease() {}
            }).launchIn(viewModelScope)
        }
    }
}