package com.example.trilby.data.sources.local


import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // String to List<String>
    @TypeConverter
    fun fromStringToList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, listType)
        } catch (e: Exception) {
            Log.e("Converters", "Error parsing String list: $value", e)
            emptyList()
        }
    }

    // List<String> to String
    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromWordPrsListToString(list: List<LocalWordPrs>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToWordPrsList(value: String?): List<LocalWordPrs>? {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            val listType = object : TypeToken<List<LocalWordPrs>>() {}.type
            gson.fromJson(value, listType)
        } catch (e: Exception) {
            Log.e("Converters", "Error parsing WordPrs list: $value", e)
            emptyList()
        }
    }
}