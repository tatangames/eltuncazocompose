package com.tatanstudios.eltuncazometapan.extras

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Extension function to create a DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class TokenManager(private val context: Context) {

    // Claves
    private val ID_KEY = stringPreferencesKey("ID")
    private val DEDOS_KEY = stringPreferencesKey("DEDOS")

    // Guardar ID en DataStore
    suspend fun saveID(id: String) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = id
        }
    }

    // Guardar DEDOS en DataStore
    suspend fun saveDEDOS(id: String) {
        context.dataStore.edit { preferences ->
            preferences[DEDOS_KEY] = id
        }
    }



    // Leer ID como Flow
    val idUsuario: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[ID_KEY] ?: ""
        }

    // Leer DEDOS como Flow
    val idDedos: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[DEDOS_KEY] ?: ""
        }




    // Eliminar ID
    suspend fun deletePreferences() {
        context.dataStore.edit { preferences ->
            preferences.remove(ID_KEY)
            preferences.remove(DEDOS_KEY)
        }
    }



    // Migrar desde SharedPreferences (solo una vez)
    suspend fun migrateFromSharedPreferencesIfNeeded() {
        val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val hasMigrated = sharedPrefs.getBoolean("hasMigratedToDataStore", false)

        if (!hasMigrated) {
            // Borrar DataStore por si acaso quedó algo
            context.dataStore.edit { it.clear() }

            // Marcar que ya se limpió para no repetir
            sharedPrefs.edit().putBoolean("hasMigratedToDataStore", true).apply()
        }
    }
}




// UTILIZADO PARA EJECUTAR 1 VEZ LAS PETICIONES RETROFIT
class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}