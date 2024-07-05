package com.example.skyfall

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.DayOfWeek

class Converter {

    fun getImage(condition: Int) : Int {
        return when (condition) {
            1000 -> R.drawable.img_sunny

            1003 -> R.drawable.img_partly_cloudy

            1087 -> R.drawable.img_thundery_outbreaks

            1006, 1009, 1030, 1135, 1147 -> R.drawable.img_cloudy

            1273, 1276, 1279, 1282 -> R.drawable.img_thunder

            1066, 1069, 1114, 1117, 1204, 1207, 1210, 1213,
            1216, 1219, 1222, 1225, 1237, 1255, 1258 -> R.drawable.img_snow

            1063, 1072, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189, 1192,
            1195, 1198, 1201, 1240, 1243, 1246, 1249, 1252, 1261, 1264 -> R.drawable.img_rain

            else -> {
                R.drawable.img_snow
            }
        }
    }

    fun getDayOfWeek(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateParse = LocalDate.parse(date, formatter)
        val dayOfWeek = dateParse.dayOfWeek

        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Monday"
            DayOfWeek.TUESDAY -> "Tuesday"
            DayOfWeek.WEDNESDAY -> "Wednesday"
            DayOfWeek.THURSDAY -> "Thursday"
            DayOfWeek.FRIDAY -> "Friday"
            DayOfWeek.SATURDAY -> "Saturday"
            DayOfWeek.SUNDAY -> "Sunday"

            else -> {
                return "Today"
            }
        }
    }
}