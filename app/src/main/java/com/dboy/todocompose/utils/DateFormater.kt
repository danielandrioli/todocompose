package com.dboy.todocompose.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormater {
    fun getTimeStampAsLong(): Long {
        val dateAsString = SimpleDateFormat.getDateInstance().format(Date())
        return SimpleDateFormat.getDateInstance().parse(dateAsString)?.time ?: 0L
    }

    fun getTimeStampAsString(timeStamp: Long?) : String {
        return SimpleDateFormat.getDateInstance().format(timeStamp)
    }
}

/*
fun getTimeStampLong(): Long? {
        SimpleDateFormat.getDateInstance()
        val timeStamp: String = SimpleDateFormat("dd MMMM yyyy - HH:mm").format(Date())
        return SimpleDateFormat("dd MMMM yyyy - HH:mm").parse(timeStamp)?.time
    }

    fun getTimeStampAsString(timeStamp: Long) : String {
        return SimpleDateFormat("dd MMMM yyyy - HH:mm").format(timeStamp)
    }
 */