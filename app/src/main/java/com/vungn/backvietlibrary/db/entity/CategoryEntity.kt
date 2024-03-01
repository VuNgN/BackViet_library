package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vungn.backvietlibrary.util.converter.TypeConverter

@Entity(tableName = "category")
@TypeConverters(TypeConverter::class)
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val bookIds: List<String>
)