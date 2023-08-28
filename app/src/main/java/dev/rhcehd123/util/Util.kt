package dev.rhcehd123.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

object Util {
    fun formatDate(date: Long): String {
        return try {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).format(date)
        } catch (e: Exception) {
            throw e
        }
    }

    fun parseCountryCodeFromName(countryName: String): String {
        return try {
            countryName.slice(countryName.length - 4 .. countryName.length - 2)
        } catch (e: Exception) {
            throw e
        }
    }

    fun Double.toFormattedString(): String {
        return try {
            DecimalFormat("#,##0.00").format(this)
        } catch (e: Exception) {
            throw e
        }
    }
}