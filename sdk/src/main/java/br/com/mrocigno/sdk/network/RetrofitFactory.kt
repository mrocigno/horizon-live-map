package br.com.mrocigno.sdk.network

import br.com.mrocigno.horizonlivemap.core.BuildConfig
import br.com.mrocigno.sandman.corinthian.corinthian
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    const val BASE_URL = BuildConfig.BASE_URL
    const val HTTP_CACHE = "http_cache"

    fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .corinthian()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    fun create(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}

