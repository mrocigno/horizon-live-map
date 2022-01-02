package br.com.mrocigno.sdk.network

import br.com.mrocigno.sdk.model.IconCategory
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

internal val gson = GsonBuilder()
    .registerTypeAdapter(IconCategory::class.java, IconCategoryAdapter())
    .create()

class IconCategoryAdapter : EnumAdapter<IconCategory>() {

    override fun to(enum: IconCategory) = enum.name

    override fun from(value: String) =
        IconCategory.values().firstOrNull { it.name == value } ?: IconCategory.UNKNOWN
}

// ====================== **  ** ======================

abstract class EnumAdapter<T> : TypeAdapter<T>() {

    abstract fun to(enum: T): String

    abstract fun from(value: String): T

    override fun write(out: JsonWriter?, value: T) {
        out?.value(toJson(value))
    }

    override fun read(reader: JsonReader?): T {
        return from(reader?.nextString().orEmpty())
    }
}