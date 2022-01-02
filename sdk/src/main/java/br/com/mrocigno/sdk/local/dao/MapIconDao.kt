package br.com.mrocigno.sdk.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.mrocigno.sdk.local.entity.MapIcon

@Dao
interface MapIconDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<MapIcon>)

    @Query("SELECT * FROM MapIcon")
    suspend fun getAll(): List<MapIcon>
}