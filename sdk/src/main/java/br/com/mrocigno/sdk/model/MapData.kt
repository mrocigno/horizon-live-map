package br.com.mrocigno.sdk.model

data class MapDataResponse (
    val about: String,
    val content: String,
    val marker: MarkerResponse,
    val title: String,
    val id: Int,
    val images: List<String>
)

data class MarkerResponse (
    val position: List<Double>,
    val image: String,
    val title: String,
    val category: IconCategory,
    val id: Long? = null
)