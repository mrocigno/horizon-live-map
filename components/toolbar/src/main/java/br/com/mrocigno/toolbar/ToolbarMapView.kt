package br.com.mrocigno.toolbar

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.mrocigno.horizonlivemap.core.compose.functions.dimenRes
import br.com.mrocigno.horizonlivemap.core.compose.functions.padres
import br.com.mrocigno.horizonlivemap.core.compose.theme.HorizonLiveMapTheme

class ToolbarMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    override fun onAttachedToWindow() {
        if (!isInEditMode) super.onAttachedToWindow()
    }

    @Composable
    override fun Content() {
        HorizonLiveMapTheme {
            ToolbarMapComposable()
        }
    }
}

@Composable
fun ToolbarMapComposable() {
    val surfaceShape = RoundedCornerShape(30.dp)
    Surface(
        shape = surfaceShape,
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.toolbar_size))
            .shadow(10.dp, shape = surfaceShape)
            .border(dimenRes(R.dimen.toolbar_border), Color.Black, shape = surfaceShape)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padres(horizontal = R.dimen.spacing_m)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = "",
                modifier = Modifier.padres(end = R.dimen.spacing_m)
            )
            Text(text = "Title", modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "",
                modifier = Modifier.padres(start = R.dimen.spacing_m)
            )
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
    HorizonLiveMapTheme {
        ToolbarMapComposable()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviews() {
    HorizonLiveMapTheme {
        ToolbarMapComposable()
    }
}