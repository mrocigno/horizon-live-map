package br.com.mrocigno.horizonlivemap

import br.com.mrocigno.horizonlivemap.core.helpers.ModuleInitialization
import br.com.mrocigno.horizonlivemap.ui.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppInitialization : ModuleInitialization() {

    override val module = module {
        viewModel { SplashViewModel(get()) }
    }
}