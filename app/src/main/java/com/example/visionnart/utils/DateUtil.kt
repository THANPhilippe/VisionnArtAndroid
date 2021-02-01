package com.example.visionnart.utils


import java.util.*

object DateUtil{

    fun formatdate (date: Date?) :String {
        val dayint :Int = date!!.date
        val monthint :Int = date!!.month + 1
        val month : String
        val yearint : Int = date.year + 1900
        var date :String
        month = if (monthint < 10){
            "0$monthint"
        }else{
            "" + monthint
        }
        date = "$dayint / $month / $yearint"
        return date
    }
}