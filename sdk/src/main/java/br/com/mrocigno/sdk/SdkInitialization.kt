package br.com.mrocigno.sdk

import androidx.room.Room
import br.com.mrocigno.horizonlivemap.core.helpers.ModuleInitialization
import br.com.mrocigno.sdk.api.MapApi
import br.com.mrocigno.sdk.local.AppDatabase
import br.com.mrocigno.sdk.network.RetrofitFactory
import br.com.mrocigno.sdk.repository.MapRepository
import br.com.mrocigno.sdk.repository.SyncRepository
import org.koin.android.ext.android.get
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

class SdkInitialization : ModuleInitialization() {

    override val module = module {
        //region Libs
        single { RetrofitFactory.createClient() }
        single { RetrofitFactory.create(get()) }
        single { Room.databaseBuilder(get(), AppDatabase::class.java, "horizon-db").build() }
        //endregion

        //region Repositories
        single { MapRepository(get()) }
        single { SyncRepository(get(), get()) }
        //endregion

        //region APIs
        single { buildApi(get(), MapApi::class.java) }
        //endregion

        //region DAOs
        single { getDB().mapIconDao() }
        //endregion
    }
}

private fun <T> buildApi(retrofit: Retrofit, clazz: Class<T>) = retrofit.create(clazz)

private fun Scope.getDB(): AppDatabase = get()