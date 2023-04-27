package ru.divarteam.atoll.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PreferenceRepository(private val sharedPreferences: SharedPreferences) {

    // Night mode repository
    var nightMode: Int
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEF_VAL)
        set(value) {
            sharedPreferences.edit().putInt(PREFERENCE_NIGHT_MODE, value).apply()
            AppCompatDelegate.setDefaultNightMode(value)
            _nightModeLive.postValue(value)
        }

    private val _nightModeLive: MutableLiveData<Int> = MutableLiveData()
    val nightModeLive: LiveData<Int>
        get() = _nightModeLive


    // User token repository
    var userToken: String
        get() = sharedPreferences.getString(PREFERENCE_USER_TOKEN, "")
            .toString()
        set(value) {
            sharedPreferences.edit().putString(PREFERENCE_USER_TOKEN, value)
                .apply()
        }

    companion object {
        private const val PREFERENCE_USER_TOKEN = "divar_voronezh_preference_user_token"
        private const val PREFERENCE_NIGHT_MODE = "divar_voronezh_preference_night_mode"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}