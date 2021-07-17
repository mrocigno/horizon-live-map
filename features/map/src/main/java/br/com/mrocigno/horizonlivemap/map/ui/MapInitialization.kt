package br.com.mrocigno.horizonlivemap.map.ui

import br.com.mrocigno.horizonlivemap.core.helpers.ModuleInitialization
import br.com.mrocigno.horizonlivemap.map.ui.map.pages.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class MapInitialization : ModuleInitialization() {

    override val module: Module = module {
        viewModel { MapViewModel(get()) }
    }
}