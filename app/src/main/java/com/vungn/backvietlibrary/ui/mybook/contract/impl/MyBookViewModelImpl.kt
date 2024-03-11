package com.vungn.backvietlibrary.ui.mybook.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.model.data.BorrowData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.GetActivatedBooksRepo
import com.vungn.backvietlibrary.ui.mybook.contract.MyBookViewModel
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
class MyBookViewModelImpl @Inject constructor(
    private val getActivatedBooksRepo: GetActivatedBooksRepo
) : MyBookViewModel, ViewModel() {
    private val _books: MutableStateFlow<List<BookEntity>> = MutableStateFlow(emptyList())
    private val _callApiState: MutableStateFlow<CallApiState> = MutableStateFlow(CallApiState.NONE)
    override val books: StateFlow<List<BookEntity>>
        get() = _books
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getActivatedBooksRepo.getActivatedBooks().stateIn(viewModelScope).collect {
                _books.value = it
            }
        }
    }

    override fun getBooks() {
        _callApiState.value = CallApiState.LOADING
        viewModelScope.launch {
            getActivatedBooksRepo.execute(object : BaseRepo.Callback<Response<BorrowData>> {
                override fun onSuccess(data: Response<BorrowData>) {
                    _callApiState.value = CallApiState.SUCCESS
                }

                override fun onError(error: Throwable) {
                    _callApiState.value = CallApiState.ERROR
                }

                override fun onRelease() {}
            }).launchIn(viewModelScope)
        }
    }
}