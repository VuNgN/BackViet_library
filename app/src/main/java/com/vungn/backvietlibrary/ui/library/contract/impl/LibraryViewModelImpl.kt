package com.vungn.backvietlibrary.ui.library.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.model.data.BookItem
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.GetBookByIdRepo
import com.vungn.backvietlibrary.model.repo.GetCategoriesRepo
import com.vungn.backvietlibrary.ui.library.contract.LibraryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModelImpl @Inject constructor(
    private val getCategoriesRepo: GetCategoriesRepo, private val getBookByIdRepo: GetBookByIdRepo
) : ViewModel(), LibraryViewModel {
    private val _categories: MutableStateFlow<List<CategoryEntity>> = MutableStateFlow(emptyList())
    override val categories: StateFlow<List<CategoryEntity>>
        get() = _categories

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesRepo.getFromDatabase().stateIn(viewModelScope).collect {
                _categories.value = it
            }
        }
    }

    override fun getBooksByCategory(category: CategoryEntity): Flow<List<BookEntity>> =
        callbackFlow {
            val ids = category.bookIds
            ids.forEach { id ->
                getBookByIdRepo.getBookById(id, object : BaseRepo.Callback<Response<BookItem>> {
                    override fun onSuccess(data: Response<BookItem>) {}

                    override fun onError(error: Throwable) {
                        error.printStackTrace()
                    }

                    override fun onRelease() {}
                })
            }
            val books = ids.mapNotNull { id ->
                getBookByIdRepo.getBookByIdFromDatabase(id).firstOrNull()
            }
            launch { send(books) }
            awaitClose { }
        }
}