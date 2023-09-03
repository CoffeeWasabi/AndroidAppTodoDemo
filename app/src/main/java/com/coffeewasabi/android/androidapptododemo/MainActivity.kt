package com.coffeewasabi.android.androidapptododemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ナビゲーションドロワーのアイコン設定（TODO:ドロワーの実装）
        drawerLayout = findViewById(R.id.main_drawer)

        // ファーストフラグメント呼び出し
        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_view) as NavHostFragment
        val navController = navHostFragment.navController

        val navView = findViewById<NavigationView>(R.id.main_nav_view)
        NavigationUI.setupWithNavController(navView, navController)
    }

    /**
     * ナビゲーションドロワーを表示するアイコン（ハンバーガーアイコン）のトリガー設定
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.main_fragment_view)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}