package br.com.mrocigno.sdk

import br.com.mrocigno.horizonlivemap.core.helpers.ModuleInitialization
import br.com.mrocigno.sdk.api.MapApi
import br.com.mrocigno.sdk.network.RetrofitFactory
import br.com.mrocigno.sdk.repository.MapRepository
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

class SdkInitialization : ModuleInitialization() {

    override val module = module {
        //region Network
        single { RetrofitFactory.createClient() }
        single { RetrofitFactory.create(get()) }
        //endregion

        //region Repositories
        single { MapRepository(get()) }
        //endregion

        single { build(get(), MapApi::class.java) }
    }
}

fun <T> build(retrofit: Retrofit, clazz: Class<T>) = retrofit.create(clazz)
