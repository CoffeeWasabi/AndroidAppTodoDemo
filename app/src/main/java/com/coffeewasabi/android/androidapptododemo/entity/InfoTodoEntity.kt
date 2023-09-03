package com.coffeewasabi.android.androidapptododemo.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * InfoTable and TodoTable link entity.
 * parent: InfoTable
 * child: TodoTable
 * type: 1:n
 */
data class InfoTodoEntity(
    @Embedded
    var info: InfoEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "infoId"
    )
    var todoList: List<TodoEntity>
)
