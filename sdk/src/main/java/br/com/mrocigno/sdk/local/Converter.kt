package br.com.mrocigno.sdk.local

import androidx.room.TypeConverter
import br.com.mrocigno.sdk.model.IconCategory

class Converter {

    @TypeConverter
    fun toIconCategory(value: String) =
        IconCategory.values().firstOrNull { it.name == value } ?: IconCategory.UNKNOWN

    @TypeConverter
    fun fromIconCategory(value: IconCategory) = value.name
}