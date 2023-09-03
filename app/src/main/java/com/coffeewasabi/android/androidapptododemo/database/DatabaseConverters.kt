package com.coffeewasabi.android.androidapptododemo.database

import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter
import java.util.*

class DatabaseConverters {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date?{
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long?{
        return date?.time?.toLong()
    }
    @TypeConverter
    fun fromCheckFlgBoolean(value: Boolean?): MutableLiveData<Boolean>? {
        return value?.let { MutableLiveData(it) }
    }
    @TypeConverter
    fun checkFlgLiveToBoolean(checked: MutableLiveData<Boolean>?): Boolean? {
        return checked?.value
    }

}