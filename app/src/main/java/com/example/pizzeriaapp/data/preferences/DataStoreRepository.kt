package com.example.pizzeriaapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataStoreRepository @Inject constructor(@ApplicationContext context: Context) :
    DataStoreInterface {
    private val dataStore = context.createDataStore(name = "user_info_preferences")

    companion object {
        val streetNameKey = preferencesKey<String>("STREET_NAME_KEY")
        val houseNumberKey = preferencesKey<String>("HOUSE_NUMBER_KEY")
        val cityNameKey = preferencesKey<String>("CITY_NAME_KEY")
        val phoneNumberKey = preferencesKey<String>("PHONE_NUMBER_KEY")
        val tokenKey = preferencesKey<String>("TOKEN")
    }


    override suspend fun saveToken(token: String) {
        dataStore.edit { store ->
            store[tokenKey] = token
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { store ->
            store[tokenKey] = ""
        }
    }

    override fun getToken(): Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val token = preferences[tokenKey]
            if (token == null || token.isEmpty()) {
                return@map null
            } else {
                return@map token
            }
        }

    override suspend fun saveUserInfo(
        streetName: String,
        houseNumber: String,
        cityName: String,
        phoneNumber: String
    ) {
        dataStore.edit { store ->
            store[streetNameKey] = streetName
            store[houseNumberKey] = houseNumber
            store[cityNameKey] = cityName
            store[phoneNumberKey] = phoneNumber
        }
    }

    override fun getUserInfo(): Flow<UserInfo> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            UserInfo(
                streetName = preferences[streetNameKey] ?: "",
                houseNumber = preferences[houseNumberKey] ?: "",
                cityName = preferences[cityNameKey] ?: "",
                phoneNumber = preferences[phoneNumberKey] ?: "",
            )
        }
}

interface DataStoreInterface {
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    fun getToken(): Flow<String?>
    suspend fun saveUserInfo(
        streetName: String,
        houseNumber: String,
        cityName: String,
        phoneNumber: String
    )

    fun getUserInfo(): Flow<UserInfo>
}