package br.com.mrocigno.sdk.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.mrocigno.sdk.model.IconCategory
import br.com.mrocigno.sdk.model.IconDataResponse

@Entity
class MapIcon(
    @PrimaryKey val id: Int,
    val image: String,
    val title: String,
    val category: IconCategory
) {

    constructor(response: IconDataResponse) : this(
        id = response.id,
        image = response.image,
        title = response.title,
        category = response.category
    )
}