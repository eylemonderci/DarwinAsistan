package com.example.darwinasistan

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        // UI Elemanları
        val tvBack = findViewById<TextView>(R.id.tvSettingsTitle)
        val switchSound = findViewById<MaterialSwitch>(R.id.switchSound)
        val cardClearData = findViewById<MaterialCardView>(R.id.cardClearData)

        // 1. GERİ DÖNÜŞ (Modern Yöntem)
        tvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 2. BİLDİRİM SESİ AYARI
        val sharedPrefs = getSharedPreferences("DarwinPrefs", Context.MODE_PRIVATE)
        switchSound.isChecked = sharedPrefs.getBoolean("sound_enabled", true)

        switchSound.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("sound_enabled", isChecked).apply()

            val message = if (isChecked) "Bildirim sesi açıldı 🔔" else "Bildirim sesi kapatıldı 🔕"
            showThemedSnackbar(message)
        }

        // 3. GEÇMİŞİ TEMİZLEME
        cardClearData.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    // --- TEMAYA UYGUN SNACKBAR FONKSİYONU ---
    private fun showThemedSnackbar(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)

        // Arka plan rengini deep_water (Koyu Lacivert) yap
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.deep_water))

        // Yazı rengini Beyaz yap
        val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)

        snackbar.show()
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Geçmişi Temizle")
        builder.setMessage("Tüm yemleme ve bakım kayıtları silinecek. Emin misin?")
        builder.setPositiveButton("Evet, Sil") { _, _ ->
            AppLogManager.clearHistory(this)
            showThemedSnackbar("Tüm veriler silindi! 🗑️")
        }

        builder.setNegativeButton("İptal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}