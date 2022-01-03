package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import androidx.lifecycle.ViewModel
import br.com.mrocigno.horizonlivemap.map.ui.map.model.ItemData
import br.com.mrocigno.sdk.network.MutableResponseFlow
import br.com.mrocigno.sdk.network.ResponseFlow
import br.com.mrocigno.sdk.repository.MapRepository

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val _mapData = MutableResponseFlow<List<ItemData>>()
    val mapData: ResponseFlow<List<ItemData>> get() = _mapData

    fun getData() = _mapData.sync(mapRepository.getMapData()) { list ->
        list.map { ItemData(it) }
    }
}