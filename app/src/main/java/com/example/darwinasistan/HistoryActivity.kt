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
        // History Activity'de Action Bar'ı gizle (Tasarım bütünlüğü için)
        supportActionBar?.hide()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHistory)

        // Log yöneticisindeki kayıt listesini alıyoruz
        val historyList = if (AppLogManager.historyLog.isEmpty()) {
            // Liste boşsa, bir başlangıç/hoşgeldin mesajı göster
            listOf(
                HistoryItem(
                    "Hoş Geldiniz",
                    "Henüz hiçbir işlem kaydı yok. Darwin'i yemlemeye başlayın!",
                    "Şimdi",
                    R.drawable.ic_home
                )
            )
        } else {
            // Liste doluysa, canlı log verisini kullan
            AppLogManager.historyLog.toList()
        }


        // Adapter ve RecyclerView'ı bağla
        val adapter = HistoryAdapter(historyList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Geri Dönüş Butonunun İşlevselliği
        val backButton = findViewById<TextView>(R.id.tvHistoryTitle)
        backButton.setOnClickListener {
            // Ana sayfaya dön
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}