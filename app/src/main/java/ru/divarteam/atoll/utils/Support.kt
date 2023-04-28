package ru.divarteam.atoll.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("SimpleDateFormat")
fun String.fromDefaultFormatToRuFormatString(): String =
    fromDefaultStringToDate()!!.let { originalDate ->
        SimpleDateFormat(
            Date().let {
                if (it.year == originalDate.year)
                    "dd MMMM HH:mm"
                else
                    "dd MMMM yyyy года HH:mm"
            },
            Locale("ru")
        ).format(
            originalDate
        )
    }

@SuppressLint("SimpleDateFormat")
fun String.fromDefaultStringToDate(): Date? =
    SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS"
    ).parse(this)