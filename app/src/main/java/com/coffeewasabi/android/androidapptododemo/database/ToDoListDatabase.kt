package com.coffeewasabi.android.androidapptododemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coffeewasabi.android.androidapptododemo.entity.InfoEntity
import com.coffeewasabi.android.androidapptododemo.entity.TodoEntity

@Database(entities = [InfoEntity::class, TodoEntity::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
abstract class ToDoListDatabase: RoomDatabase() {
    abstract fun DatabaseDao(): DatabaseDao

    companion object{
        private const val dbName = "info_todo_dev.db"
        private var instance: ToDoListDatabase? = null

        fun getInstance(context: Context): ToDoListDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(
                    context,
                    ToDoListDatabase::class.java,
                    dbName
                )
                .fallbackToDestructiveMigration()
                .build()
            }
            return requireNotNull(instance)
        }
    }
}