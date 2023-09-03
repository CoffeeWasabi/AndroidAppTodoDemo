package com.coffeewasabi.android.androidapptododemo.view

import android.graphics.Paint
import android.text.format.DateFormat
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.coffeewasabi.android.androidapptododemo.R
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("DateToString")
fun bindDateToString(textView: TextView, date: Date){
    textView.setText(date.convertToString())
}

@BindingAdapter("strikethroughTextView")
fun bindStrikethroughTextView(checkBox: CheckBox, checkFlg: Boolean){
    if(checkFlg){
        checkBox.apply {
            setTextColor(ContextCompat.getColor(checkBox.context, R.color.text_cancel_line_color))
            // 取り消し線を設定する
            paint.flags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            paint.isAntiAlias = true
        }
    } else {
        checkBox.apply {
            setTextColor(ContextCompat.getColor(checkBox.context, R.color.secondary_text_default_material_common))
            // 取り消し線を解除する
            paint.flags = Paint.ANTI_ALIAS_FLAG
            paint.isAntiAlias = false
        }
    }
}

fun Date.convertToString(): String {
    val local = Locale.getDefault()
    val format = DateFormat.getBestDateTimePattern(local, "yyyyMMdd HH:mm")
    val dateFormat = SimpleDateFormat(format, local)
    return dateFormat.format(time.toLong())
}