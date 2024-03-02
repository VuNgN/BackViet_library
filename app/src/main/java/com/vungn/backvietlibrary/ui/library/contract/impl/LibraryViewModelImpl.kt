package com.vungn.backvietlibrary.ui.library.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.model.repo.GetCategoriesRepo
import com.vungn.backvietlibrary.ui.library.contract.LibraryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModelImpl @Inject constructor(private val categoriesRepo: GetCategoriesRepo) :
    ViewModel(), LibraryViewModel {
    private val _categories: MutableStateFlow<List<CategoryEntity>> = MutableStateFlow(emptyList())
    override val categories: StateFlow<List<CategoryEntity>>
        get() = _categories

    init {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesRepo.getFromDatabase().stateIn(viewModelScope).collect {
                _categories.value = it
            }
        }
    }
}