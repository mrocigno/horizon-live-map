package br.com.mrocigno.horizonlivemap.core.functions

import br.com.arch.toolkit.delegate.ExtraType
import br.com.arch.toolkit.delegate.extraProvider
import java.lang.IllegalStateException

fun <T> reqExtraProvider(extra: String) = extraProvider<T>(extra, true, ExtraType.AUTO) {
    throw IllegalStateException("404 $extra not found")
}