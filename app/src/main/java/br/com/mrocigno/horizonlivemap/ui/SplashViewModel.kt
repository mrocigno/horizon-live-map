package br.com.mrocigno.horizonlivemap.ui

import androidx.lifecycle.ViewModel
import br.com.mrocigno.sdk.local.entity.MapIcon
import br.com.mrocigno.sdk.network.MutableResponseFlow
import br.com.mrocigno.sdk.network.ResponseFlow
import br.com.mrocigno.sdk.repository.SyncRepository

class SplashViewModel(
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _sync = MutableResponseFlow<Any>()
    val sync: ResponseFlow<Any> get() = _sync

    fun sync() {
        _sync.sync(syncRepository.syncMapIcons())
    }
}