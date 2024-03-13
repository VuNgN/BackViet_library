package com.vungn.backvietlibrary.model.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.vungn.backvietlibrary.db.dao.UserDao
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>
) {
    fun logout(): Flow<Boolean> = callbackFlow {
        val job = coroutineScopeIO.launch(Dispatchers.IO) {
            try {
                userDao.deleteAllUsers()
                dataStore.edit {
                    it.clear()
                }
                send(true)
            } catch (e: Exception) {
                e.printStackTrace()
                send(false)
            }
        }
        awaitClose {
            job.cancel()
        }
    }
}