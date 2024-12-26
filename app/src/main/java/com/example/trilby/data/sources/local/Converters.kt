package com.example.trilby.data.sources.local


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    // String to List<String>
    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    // List<String> to String
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Gson().toJson(list)
    }
}