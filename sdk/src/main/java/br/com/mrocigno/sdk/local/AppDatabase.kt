package br.com.mrocigno.sdk.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.mrocigno.sdk.local.dao.MapIconDao
import br.com.mrocigno.sdk.local.entity.MapIcon

@Database(entities = [
    MapIcon::class
], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mapIconDao(): MapIconDao
}
