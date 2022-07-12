package com.dboy.todocompose.utils

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateFormaterTest {
    @Test
    fun testDate() {
        val timeStamp: String = SimpleDateFormat("dd MMMM yyyy - HH:mm").format(Date())
        val a = SimpleDateFormat("dd MMMM yyyy - HH:mm").parse(timeStamp)
        println("TimeStamp: $timeStamp")
        println(a.time)
        println( SimpleDateFormat("dd MMMM yyyy - HH:mm").format(a.time))

        val b = SimpleDateFormat.getDateInstance().format(Date())
        val c = SimpleDateFormat.getDateInstance().parse(b)?.time
        val d = SimpleDateFormat.getDateInstance().format(c)
        println(b)
        println(c)
        println(d)
    }
}