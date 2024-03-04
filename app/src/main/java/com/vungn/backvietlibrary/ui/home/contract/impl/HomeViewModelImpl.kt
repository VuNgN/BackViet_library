package com.vungn.backvietlibrary.ui.home.contract.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.model.repo.Get10EarliestPublishedBooks
import com.vungn.backvietlibrary.ui.home.contract.HomeViewModel
import com.vungn.backvietlibrary.util.key.PreferenceKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    dataStore: DataStore<Preferences>,
    private val get10EarliestPublishedBooks: Get10EarliestPublishedBooks
) : HomeViewModel, ViewModel() {
    private val _avatar: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _books: MutableStateFlow<List<BookEntity>> = MutableStateFlow(emptyList())
    override val avatar: StateFlow<String?>
        get() = _avatar
    override val books: StateFlow<List<BookEntity>>
        get() = _books

    init {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                val avatar = preferences[PreferenceKey.AVATAR_URL]
                _avatar.emit(avatar)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            get10EarliestPublishedBooks.getFromDatabase().stateIn(viewModelScope).collect {
                _books.value = it
            }
        }
    }
}