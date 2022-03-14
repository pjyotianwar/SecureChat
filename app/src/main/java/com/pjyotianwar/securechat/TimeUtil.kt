package com.pjyotianwar.securechat

import java.text.SimpleDateFormat
import java.util.*

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy\nHH:mm")
    return format.format(date)
}