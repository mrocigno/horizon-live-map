package br.com.mrocigno.sdk.repository

import br.com.mrocigno.sdk.api.MapApi
import kotlinx.coroutines.flow.flow

class MapRepository(private val mapApi: MapApi) {

    fun getMapData() = flow {
        val teste = mapApi.getMapData()
        emit(teste)
    }
}