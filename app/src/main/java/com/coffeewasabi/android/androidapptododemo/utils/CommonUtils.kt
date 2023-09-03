package com.coffeewasabi.android.androidapptododemo.utils

import androidx.fragment.app.Fragment
import com.coffeewasabi.android.androidapptododemo.MainActivity

//class CommonUtils {
//    companion object{
//        private var instance: CommonUtils? = null
//
//        fun getInstance(): CommonUtils{
//            if(instance == null){
//                instance = CommonUtils()
//            }
//            return instance!!
//        }
//
//        const val DATE_FORMAT_yyyyMMdd_hhMM = "yyyy/MM/dd HH:mm"
//    }
//}
//
//// TODO BindingAdapterで記述すればcontextを呼び出せる
//// https://zenn.dev/crimsonwoods/articles/535bca78e1abbc
////fun Date.convertToString(): String{
////    val df = SimpleDateFormat(CommonUtils.DATE_FORMAT_yyyyMMdd_hhMM)
////    val test = DateUtils.formatDateTime(applicationContext)
////
////
////    return df.format(this)
////}
//
//fun Fragment.getBackStackTag(): String {
//    return this.javaClass.simpleName
//}

fun Fragment.getMainActivity(): MainActivity? {
    if (activity is MainActivity) {
        return activity as MainActivity
    }
    return null
}