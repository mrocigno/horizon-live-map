package br.com.mrocigno.sdk.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.mrocigno.sdk.local.entity.MapIcon

@Dao
interface MapIconDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<MapIcon>)

    @Query("SELECT * FROM MapIcon")
    fun getAll(): List<MapIcon>
}