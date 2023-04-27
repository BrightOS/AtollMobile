package ru.divarteam.atoll.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("SimpleDateFormat")
fun String.fromDefaultFormatToRuFormatString(): String =
    SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS"
    ).parse(this)!!.let { originalDate ->
        SimpleDateFormat(
            Date().let {
                if (it.year == originalDate.year)
                    "dd MMMM"
                else
                    "dd MMMM yyyy года"
            },
            Locale("ru")
        ).format(
            originalDate
        )
    }