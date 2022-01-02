package br.com.mrocigno.sdk.repository

import br.com.mrocigno.sdk.api.MapApi
import br.com.mrocigno.sdk.local.dao.MapIconDao
import br.com.mrocigno.sdk.local.entity.MapIcon
import kotlinx.coroutines.flow.flow

class SyncRepository(
    private val mapIconDao: MapIconDao,
    private val mapApi: MapApi
) {

    fun syncMapIcons() = flow {
        val apiData = mapApi.getIconsData()
        val entity = apiData.map { MapIcon(it) }
        mapIconDao.insertAll(entity)
        emit(entity)
    }
}