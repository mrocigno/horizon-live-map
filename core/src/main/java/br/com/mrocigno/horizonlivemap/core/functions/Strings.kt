package br.com.mrocigno.horizonlivemap.core.functions

import br.com.mrocigno.horizonlivemap.core.BuildConfig

fun baseUrl() = BuildConfig.BASE_URL

fun baseUrl(complement: String): String {
    return if (complement.substring(0, 1) == "/") BuildConfig.BASE_URL + complement
    else "${BuildConfig.BASE_URL}/$complement"
}