package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.UserDao
import com.vungn.backvietlibrary.db.entity.UserEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.Request
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.data.UserValue
import com.vungn.backvietlibrary.model.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.Calendar
import javax.inject.Inject

class EditProfileRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val userService: UserService,
    private val userDao: UserDao
) : BaseRepo<Response<UserValue>, UserEntity?>() {
    private lateinit var _userRequest: UserValue
    override val call: Call<Response<UserValue>>
        get() = userService.updateUser(Request(_userRequest))

    suspend fun updateUser(user: UserValue, callback: Callback<Response<UserValue>>) {
        _userRequest = user
        execute(callback).launchIn(coroutineScopeIO)
    }

    override fun getFromDatabase(): Flow<UserEntity> = userDao.getUserById(_userRequest.id)

    override fun Response<UserValue>.toEntity(): UserEntity? {
        val calendar = Calendar.getInstance()
        this.data?.run {
            return UserEntity(
                calendar.timeInMillis, id, gender, displayName, address, avatar, identityNo, isLock
            )
        } ?: return null
    }

    override suspend fun saveToDatabase(data: Response<UserValue>) {
        coroutineScopeIO.launch(Dispatchers.IO) {
            val entity = data.toEntity()
            if (entity != null) {
                userDao.updateUsers(entity)
            }
        }
    }
}