package com.vungn.backvietlibrary.util.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

class TypeConverter {
    @TypeConverter
    fun fromListString(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toListString(value: String?): List<String> {
        return Gson().fromJson(value, Array<String>::class.java).toList()
    }
}