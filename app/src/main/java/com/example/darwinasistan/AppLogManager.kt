package com.example.darwinasistan

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppLogManager {
    var historyLog = mutableListOf<HistoryItem>()

    // Zaman Damgası Al
    fun getCurrentTimestamp(): String {
        val formatter = SimpleDateFormat("HH:mm | dd MMM", Locale.getDefault())
        return formatter.format(Date())
    }

    fun saveData(context: Context) {
        val sharedPrefs = context.getSharedPreferences("DarwinData", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val gson = Gson()
        val jsonString = gson.toJson(historyLog)

        editor.putString("history_list", jsonString)
        editor.apply()
    }

    fun loadData(context: Context) {
        val sharedPrefs = context.getSharedPreferences("DarwinData", Context.MODE_PRIVATE)
        val jsonString = sharedPrefs.getString("history_list", null)

        if (jsonString != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
            historyLog = gson.fromJson(jsonString, type)
        }
    }

    fun clearHistory(context: Context) {
        historyLog.clear()
        saveData(context) // Boş halini kaydet
    }
}