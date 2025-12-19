package com.example.darwinasistan

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
// 👇 Modern Geri Tuşu İçin Gerekli Kütüphane
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLogManager.loadData(this)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.dark_gray)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val btnFeed = findViewById<Button>(R.id.btnFeed)
        val btnWater = findViewById<Button>(R.id.btnWater)
        val imgDarwin = findViewById<ImageView>(R.id.imgDarwinFish)


        var isHungry = true
        var isWaterDirty = true


        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        // ✅ MODERN GERİ TUŞU YÖNETİMİ (Buraya Ekledik)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Eğer Menü Kapalıysa -> Standart geri işlemini yap (Çıkış vb.)
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home_drawer -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_alarm -> {
                    startActivity(Intent(this, AlarmActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_history_drawer -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_settings_drawer -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        fun showThemedSnackbar(message: String) {
            val parentView = findViewById<DrawerLayout>(R.id.drawerLayout)
            val snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.deep_water))
            val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(Color.WHITE)
            snackbar.show()
        }

        btnFeed.setOnClickListener {
            // Animasyon
            btnFeed.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150)
                .withEndAction { btnFeed.animate().scaleX(1f).scaleY(1f).setDuration(150) }
                .start()

            isHungry = false
            val timestamp = AppLogManager.getCurrentTimestamp()
            val feedItem = HistoryItem("Yemleme Yapıldı", "Granül yem verildi.", timestamp, R.drawable.ic_feed)


            AppLogManager.historyLog.add(0, feedItem)


            AppLogManager.saveData(this)

            showThemedSnackbar("Karnım doydu! 🥫 Kayıt: $timestamp")
        }

        // Suyu değiştirme
        btnWater.setOnClickListener {
            // Animasyon
            btnWater.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150)
                .withEndAction { btnWater.animate().scaleX(1f).scaleY(1f).setDuration(150) }
                .start()

            if (isWaterDirty) {
                isWaterDirty = false
                val timestamp = AppLogManager.getCurrentTimestamp()
                val waterItem = HistoryItem("Su Değiştirildi", "Tam su değişimi yapıldı.", timestamp, R.drawable.ic_water)


                AppLogManager.historyLog.add(0, waterItem)
                AppLogManager.saveData(this)

                showThemedSnackbar("Ohh! Su tertemiz oldu ✨ Kayıt: $timestamp")
            } else {
                showThemedSnackbar("Su zaten temiz, israf etmeyelim! 🌊")
            }
        }


        imgDarwin.setOnClickListener {
            val stateMessage = when {
                isWaterDirty -> "Öhö öhö! Su çok pis! 🤢"
                isHungry -> "Karnım aç! 🐟"
                else -> "Keyfim yerinde 🫧"
            }
            showThemedSnackbar(stateMessage)
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}