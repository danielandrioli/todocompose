package com.dboy.todocompose.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormater {
//    fun getTimeStampAsLong(): Long {
//        val dateAsString = SimpleDateFormat.getDateInstance().format(Date())
//        return SimpleDateFormat.getDateInstance().parse(dateAsString)?.time ?: 0L
//    }

    fun getTimeStampAsString(timeStamp: Long?) : String {
        return SimpleDateFormat.getDateInstance().format(timeStamp) //Returning just the date. I don't want to show the hours and minutes.
    }


    fun getTimeStampAsLong(): Long { //This function also gets the time, so I can order by the last edited task.
        SimpleDateFormat.getDateInstance()
        val timeStamp: String = SimpleDateFormat("dd MMMM yyyy - HH:mm:ss").format(Date())
        return SimpleDateFormat("dd MMMM yyyy - HH:mm:ss").parse(timeStamp)?.time ?: 0L
    }

//    fun getTimeStampAsStringTST(timeStamp: Long) : String {
//        return SimpleDateFormat("dd MMMM yyyy - HH:mm").format(timeStamp)
//    }
}


