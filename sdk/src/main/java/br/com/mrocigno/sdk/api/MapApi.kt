package br.com.mrocigno.sdk.api

import retrofit2.http.GET

interface MapApi {

    @GET("/data/data.json")
    suspend fun getMapData(): List<MapDataResponse>
}

data class MapDataResponse (
    val about: String,
    val content: String,
    val marker: MarkerResponse,
    val title: String,
    val id: Int,
    val images: List<Any?>
)

data class MarkerResponse (
    val position: List<Double>,
    val image: String,
    val title: String,
    val id: Long? = null
)