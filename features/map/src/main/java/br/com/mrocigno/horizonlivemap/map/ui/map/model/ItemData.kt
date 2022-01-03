package br.com.mrocigno.horizonlivemap.map.ui.map.model

import android.os.Parcelable
import br.com.mrocigno.sdk.model.IconCategory
import br.com.mrocigno.sdk.model.MapDataResponse
import br.com.mrocigno.sdk.model.MarkerResponse
import kotlinx.parcelize.Parcelize

@Parcelize
class ItemData(
    val about: String,
    val content: String,
    val marker: MarkerData,
    val title: String,
    val id: Int,
    val images: List<String>
) : Parcelable {

    constructor(response: MapDataResponse) : this(
        about = response.about,
        content = response.content,
        marker = MarkerData(response.marker),
        title = response.title,
        id = response.id,
        images = response.images
    )
}

@Parcelize
class MarkerData (
    val position: List<Double>,
    val image: String,
    val title: String,
    val category: IconCategory,
    val id: Long? = null
) : Parcelable {

    constructor(response: MarkerResponse) : this(
        position = response.position,
        image = response.image,
        title = response.title,
        category = response.category,
        id = response.id,
    )
}