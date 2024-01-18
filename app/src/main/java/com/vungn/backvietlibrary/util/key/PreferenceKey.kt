package com.vungn.backvietlibrary.util.key

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKey {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val DISPLAY_NAME = stringPreferencesKey("display_name")
    val AVATAR_URL = stringPreferencesKey("avatar_url")
}