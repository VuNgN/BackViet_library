package com.vungn.backvietlibrary.model.repo

import android.content.Context
import androidx.core.net.toUri
import com.vungn.backvietlibrary.db.dao.UserDao
import com.vungn.backvietlibrary.db.entity.UserEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.data.UserValue
import com.vungn.backvietlibrary.model.service.UserService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.File
import java.util.Calendar
import javax.inject.Inject

class UploadAvatarRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    @ApplicationContext private val context: Context,
    private val userService: UserService,
    private val userDao: UserDao
) : BaseRepo<Response<UserValue>, UserEntity?>() {
    private lateinit var _avatar: File

    override val call: Call<Response<UserValue>>
        get() {
            val requestBody = _avatar.asRequestBody(
                context.contentResolver.getType(
                    _avatar.toUri()
                )?.toMediaTypeOrNull()
            )
            val part = MultipartBody.Part.createFormData("file", _avatar.name, requestBody)
            return userService.uploadAvatar(part)
        }

    suspend fun setAvatar(avatar: File, callback: Callback<Response<UserValue>>) {
        this._avatar = avatar
        execute(callback).launchIn(coroutineScopeIO)
    }

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