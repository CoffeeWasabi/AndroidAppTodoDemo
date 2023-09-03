package com.coffeewasabi.android.androidapptododemo.entity

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.coffeewasabi.android.androidapptododemo.utils.DataType
import java.util.*

@Entity(tableName = "TodoTable",
    foreignKeys = [ForeignKey(
        entity=InfoEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("infoId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    var id: Long = 0,

    @ColumnInfo(name = "infoId", index = true)
    var infoId: Long = 0,

    @ColumnInfo(name = "todoName")
    var todoName: String = "",

    @ColumnInfo(name = "checkFlg")
    var checkFlg: MutableLiveData<Boolean> = MutableLiveData(false),

    @ColumnInfo(name = "sortNo")
    var sortNo: Int = 0,

    @ColumnInfo(name = "insDate")
    var insDate: Date = Date(),

    @ColumnInfo(name = "updDate")
    var updDate: Date = Date(),

    @Ignore
    var dataType: DataType = DataType.TODO_DATA,

    @Ignore
    val liveDataFlg: MutableLiveData<Boolean> = MutableLiveData(false)
)