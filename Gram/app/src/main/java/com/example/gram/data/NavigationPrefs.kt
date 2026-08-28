package com.example.gram.data

import android.content.Context
import androidx.core.content.edit

object NavigationPrefs {
    private const val PREF_NAME = "nav_prefs"
    private const val KEY_LAST_ROUTE = "last_route"

    fun save(context: Context, route: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            putString(KEY_LAST_ROUTE, route)
        }
    }

    fun load(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LAST_ROUTE, null)
    }
}
