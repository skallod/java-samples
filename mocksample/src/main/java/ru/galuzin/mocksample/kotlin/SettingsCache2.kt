package ru.galuzin.mocksample.kotlin

import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource

open class SettingsCache2(private val datasource: DataSource) {

    val map = ConcurrentHashMap<String,String>()

    fun postConstruct() {
        println("cache2 load entries from db")
    }

    fun getString(someSetting: String, default_some: String): String {
        println("cache2 get string")
        val value = map.get(someSetting)
        println("cache2 value = " + value)
        if (value == null) {
            println("cache2 load from db")
            return default_some
        }
        return value
    }

}
