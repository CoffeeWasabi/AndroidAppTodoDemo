package com.coffeewasabi.android.androidapptododemo

import android.app.Application
import com.coffeewasabi.android.androidapptododemo.database.ToDoListDatabase

class MainApplication: Application() {

    companion object{
        lateinit var database: ToDoListDatabase
    }

    override fun onCreate() {
        super.onCreate()

        // Room Databaseのセットアップ
        database = ToDoListDatabase.getInstance(applicationContext)
    }
}