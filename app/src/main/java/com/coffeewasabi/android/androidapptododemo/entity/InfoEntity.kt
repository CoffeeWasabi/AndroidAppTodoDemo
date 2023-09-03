package com.coffeewasabi.android.androidapptododemo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "InfoTable")
data class InfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    var id: Long = 0,

    @ColumnInfo(name = "infoName")
    var infoName: String = "",

    @ColumnInfo(name = "insDate")
    var insDate: Date  = Date(),

    @ColumnInfo(name = "updDate")
    var updDate: Date  = Date(),

    @Ignore
    var deleteFlg: Boolean = false,
)