package com.vungn.backvietlibrary.ui.newandhot.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.model.data.BookData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.BaseRepo.Callback
import com.vungn.backvietlibrary.model.repo.Get10EarliestPublishedBooks
import com.vungn.backvietlibrary.ui.newandhot.contract.NewAndHotViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewAndHotViewModelImpl @Inject constructor(
    private val get10EarliestPublishedBooks: Get10EarliestPublishedBooks
) : NewAndHotViewModel, ViewModel() {
    private val _books: MutableStateFlow<List<BookEntity>> = MutableStateFlow(emptyList())
    override val books: StateFlow<List<BookEntity>>
        get() = _books

    init {
        viewModelScope.launch(Dispatchers.IO) {
            get10EarliestPublishedBooks.execute(object : Callback<Response<BookData>> {
                override fun onSuccess(data: Response<BookData>) {}

                override fun onError(error: Throwable) {
                    error.printStackTrace()
                }

                override fun onRelease() {}
            })
        }
        viewModelScope.launch {
            get10EarliestPublishedBooks.getFromDatabase().collect {
                _books.value = it
            }
        }
    }
}