package com.coffeewasabi.android.androidapptododemo.database

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.room.Transaction
import com.coffeewasabi.android.androidapptododemo.entity.InfoEntity
import com.coffeewasabi.android.androidapptododemo.entity.InfoTodoEntity
import com.coffeewasabi.android.androidapptododemo.entity.TodoEntity

class DatabaseRepository(private val dao: DatabaseDao) {

    val infoTodo = dao.getInfoTodoList()

    @WorkerThread
    suspend fun insert(info: InfoEntity): Long{
        return dao.insert(info)
    }

    @WorkerThread
    suspend fun insert(todo: TodoEntity): Long{
        return dao.insert(todo)
    }

    @WorkerThread
    suspend fun insert(info: InfoEntity, todoList: List<TodoEntity>): Long{
        try{
            var infoId = insert(info)

            for(todo in todoList){
                val insertData = TodoEntity(
                    0,
                    infoId,
                    todo.todoName,
                    todo.checkFlg,
                    todo.sortNo,
                    todo.insDate,
                    todo.updDate
                )
                insert(insertData)
            }
            return infoId
        } catch (e: Exception){
            Log.e("DatabaseRepository", "Insert Failed " + e.message)
            return -1L
        }
    }

    @WorkerThread
    suspend fun update(info: InfoEntity){
        dao.update(info)
    }

    @WorkerThread
    suspend fun update(info: InfoEntity, todoList: List<TodoEntity>): Boolean{
        try{
            update(info)
            deleteTodoList(info.id)

            for(todo in todoList){
                val insertData = TodoEntity(
                    0,
                    info.id,
                    todo.todoName,
                    todo.checkFlg,
                    todo.sortNo,
                    todo.insDate,
                    todo.updDate
                )
                insert(insertData)
            }

            return true
        } catch (e: Exception){
            Log.e("Repository", "Insert Failed " + e.message)
            return false
        }

    }

    @Transaction
    @WorkerThread
    suspend fun deleteInfoTodoList(infoList: List<InfoTodoEntity>): Boolean{
        try{
            dao.deleteList(infoList)
            return true
        } catch (e: Exception){
            return false
        }
    }

    @WorkerThread
    suspend fun deleteTodoList(infoId: Long){
        dao.deleteTodoList(infoId)
    }

}