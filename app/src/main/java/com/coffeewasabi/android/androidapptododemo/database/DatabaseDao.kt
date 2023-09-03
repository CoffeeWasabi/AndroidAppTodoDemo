package com.coffeewasabi.android.androidapptododemo.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.coffeewasabi.android.androidapptododemo.entity.InfoEntity
import com.coffeewasabi.android.androidapptododemo.entity.InfoTodoEntity
import com.coffeewasabi.android.androidapptododemo.entity.TodoEntity

@Dao
interface DatabaseDao {
    @Transaction
    @Query("SELECT * FROM InfoTable")
    fun getInfoTodoList(): LiveData<List<InfoTodoEntity>>

    @Insert
    suspend fun insert(info: InfoEntity): Long

    @Insert
    suspend fun insert(todo: TodoEntity): Long

    @Update
    suspend fun update(info: InfoEntity)

    @Update
    suspend fun update(todo: TodoEntity)

    @Transaction
    suspend fun deleteList(list: List<InfoTodoEntity>){
        list.forEach {
            try{
                delete(it.info)
                // 紐づくTodoリストのデータも削除
                abstractDeleteTodoList(it.info.id)
            } catch(error: Error){
                Log.e("DatabaseDao", "DeleteList Error:$error")
            }
        }
    }

    @Delete
    fun delete(info: InfoEntity)

    @Query("DELETE from TodoTable WHERE infoId = :infoId")
    fun abstractDeleteTodoList(infoId: Long)

    @Query("DELETE from TodoTable WHERE infoId = :infoId")
    suspend fun deleteTodoList(infoId: Long)
}