package com.example.darwinasistan

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        supportActionBar?.hide()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHistory)


        val historyList = if (AppLogManager.historyLog.isEmpty()) {
            listOf(
                HistoryItem(
                    "Hoş Geldiniz",
                    "Henüz hiçbir işlem kaydı yok. Darwin'i yemlemeye başlayın!",
                    "Şimdi",
                    R.drawable.ic_home
                )
            )
        } else {
            AppLogManager.historyLog.toList()
        }

        val adapter = HistoryAdapter(historyList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val backButton = findViewById<TextView>(R.id.tvHistoryTitle)
        backButton.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}