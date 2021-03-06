package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mrocigno.sdk.api.MapDataResponse
import br.com.mrocigno.sdk.network.MutableResponseFlow
import br.com.mrocigno.sdk.network.ResponseFlow
import br.com.mrocigno.sdk.repository.MapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val _teste = MutableResponseFlow<List<MapDataResponse>>()
    val teste: ResponseFlow<List<MapDataResponse>> get() = _teste

    fun testee() {
        viewModelScope
        _teste.sync(mapRepository.getMapData())
    }

}