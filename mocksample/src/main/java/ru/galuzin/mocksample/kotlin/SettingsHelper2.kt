package ru.galuzin.mocksample.kotlin

import javax.sql.DataSource

open class SettingsHelper2(private val datasource: DataSource) {

    init {
        println("sh2 created")
    }

    val someSetting: String
        get() = getString("SomeSetting")

    fun postConstruct() {
        settingsCache = SettingsCache2(datasource)
        settingsCache.postConstruct()
    }

    fun getString(setting: String) : String {
        return settingsCache.getString(setting, "")
    }

    companion object {
        private lateinit var settingsCache: SettingsCache2
    }
}
