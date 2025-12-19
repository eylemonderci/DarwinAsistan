package com.example.darwinasistan

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class AlarmActivity : AppCompatActivity() {

    private lateinit var tvTimeSelector: TextView
    private var selectedHour = 9
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // Tasarım bütünlüğü için üst barı gizle
        supportActionBar?.hide()

        // UI Elemanlarını Tanımla
        tvTimeSelector = findViewById(R.id.tvTimeSelector)
        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        val tvBack = findViewById<TextView>(R.id.tvAlarmTitle)

        // Geri Dönüş Butonu
        tvBack.setOnClickListener { onBackPressed() }

        // --- SAAT SEÇME (TimePicker) ---
        tvTimeSelector.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hour, minute ->
                    selectedHour = hour
                    selectedMinute = minute
                    // Saati 09:05 formatında yazdır
                    val formattedTime = String.format("%02d:%02d", hour, minute)
                    tvTimeSelector.text = formattedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24 saat formatı
            )
            timePickerDialog.show()
        }

        // --- ALARMI KUR BUTONU ---
        btnSetAlarm.setOnClickListener {
            scheduleAlarm(selectedHour, selectedMinute)
        }
    }

    // Gerçek Alarm Kurma Fonksiyonu
    private fun scheduleAlarm(hour: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        // PendingIntent: Sistemin bizim adımıza çalıştıracağı emir
        // FLAG_IMMUTABLE ve UPDATE_CURRENT güvenlik ve güncelleme için şarttır
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Zamanı Ayarla
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // Eğer seçilen saat şu andan önceyse (örn: şu an 14:00, sen 10:00 seçtin),
        // alarmı YARIN sabah 10:00'a kur.
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Alarmı Sisteme Kaydet
        try {
            // Android 12 ve üzeri için tam zamanlı alarm izni gereklidir
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    // İzin yoksa ayarları açtır
                    val intentSettings = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(intentSettings)
                    return
                }
            }

            // Alarmı Kur (Doze modunda bile çalışır)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            // Kullanıcıya Bilgi Ver (Şık Snackbar)
            val message = "Alarm kuruldu: ${String.format("%02d:%02d", hour, minute)} ⏰"
            val snackbar = Snackbar.make(tvTimeSelector, message, Snackbar.LENGTH_LONG)

            // Arka plan rengi (Deep Water)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.deep_water))
            // Yazı rengi (Beyaz)
            val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(Color.WHITE)

            snackbar.show()

        } catch (e: SecurityException) {
            // İzin hatası olursa kullanıcıyı uyar
            val snackbar = Snackbar.make(tvTimeSelector, "Lütfen ayarlardan alarm izni verin.", Snackbar.LENGTH_LONG)
            snackbar.setAction("AYARLAR") {
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
            snackbar.show()
        }
    }
}