package br.com.mrocigno.sdk.api

import br.com.mrocigno.sdk.model.IconDataResponse
import br.com.mrocigno.sdk.model.MapDataResponse
import retrofit2.http.GET

interface MapApi {

    @GET("/data/data.json")
    suspend fun getMapData(): List<MapDataResponse>

    @GET("/api/mapicons")
    suspend fun getIconsData(): List<IconDataResponse>
}
