package br.com.mrocigno.horizonlivemap.core.compose.functions

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DimenRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.dimensionResource

val context: Context @Composable get() = LocalContext.current

@Composable
fun dimenRes(@DimenRes int: Int?) = int?.let { dimensionResource(it) } ?: 0.dp

@Composable
@SuppressLint("ComposableModifierFactory")
fun Modifier.padres(@DimenRes res: Int) = padding(dimenRes(res))

@Composable
@SuppressLint("ComposableModifierFactory")
fun Modifier.padres(
    @DimenRes horizontal: Int? = null,
    @DimenRes vertical: Int? = null
) = padding(
    horizontal = dimenRes(horizontal),
    vertical = dimenRes(vertical)
)

@Composable
@SuppressLint("ComposableModifierFactory")
fun Modifier.padres(
    @DimenRes start: Int? = null,
    @DimenRes top: Int? = null,
    @DimenRes end: Int? = null,
    @DimenRes bottom: Int? = null
) = padding(
    start = dimenRes(start),
    top = dimenRes(top),
    end = dimenRes(end),
    bottom = dimenRes(bottom)
)