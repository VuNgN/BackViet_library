package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vungn.backvietlibrary.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: String): Flow<UserEntity>

    @Insert
    fun insertUsers(vararg users: UserEntity)

    @Update
    fun updateUsers(vararg users: UserEntity)

    @Query("DELETE FROM user")
    fun deleteAllUsers()
}