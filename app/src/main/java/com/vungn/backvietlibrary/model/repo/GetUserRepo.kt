package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.UserDao
import com.vungn.backvietlibrary.db.entity.UserEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.UserResponse
import com.vungn.backvietlibrary.model.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.Calendar
import javax.inject.Inject

class GetUserRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val userService: UserService,
    private val userDao: UserDao
) : BaseRepo<UserResponse, UserEntity>() {
    override val call: Call<UserResponse>
        get() = userService.getUser()

    override fun getFromDatabase(): Flow<UserEntity> = callbackFlow {
        coroutineScopeIO.launch(Dispatchers.IO) {
            userDao.getAllUsers().collect {
                val user = it.firstOrNull()
                if (user != null) {
                    launch {
                        this@callbackFlow.send(user)
                    }
                }
            }
        }
        awaitClose {}
    }

    override fun UserResponse.toEntity(): UserEntity {
        val calendar = Calendar.getInstance()
        this.data.run {
            return UserEntity(
                calendar.timeInMillis,
                id,
                gender,
                displayName,
                address,
                avatar,
                identityNo,
                isLock
            )
        }
    }

    override suspend fun saveToDatabase(data: UserResponse) {
        coroutineScopeIO.launch(Dispatchers.IO) {
            val user = userDao.getAllUsers().stateIn(coroutineScopeIO).firstOrNull()
            if (user?.isEmpty() == true) {
                userDao.insertUsers(data.toEntity())
            } else {
                userDao.updateUsers(data.toEntity())
            }
        }
    }
}