package app.simple.inure.preferences

import androidx.annotation.IntRange
import app.simple.inure.preferences.SharedPreferences.getSharedPreferences

object TerminalPreferences {

    private const val fontSize = "terminal_font_size"
    private const val color = "terminal_color"

    /* ---------------------------------------------------------------------------------------------- */

    // Font size
    fun getFontSize(): Int {
        return getSharedPreferences().getInt(fontSize, 10)
    }

    fun setFontSize(@IntRange(from = 0, to = 288) value: Int): Boolean {
        return getSharedPreferences().edit().putInt(fontSize, value).commit()
    }

    /* ---------------------------------------------------------------------------------------------- */

    // Color
    fun getColor(): Int {
        return getSharedPreferences().getInt(color, 0)
    }

    fun setColor(value: Int): Boolean {
        return getSharedPreferences().edit().putInt(color, value).commit()
    }
}