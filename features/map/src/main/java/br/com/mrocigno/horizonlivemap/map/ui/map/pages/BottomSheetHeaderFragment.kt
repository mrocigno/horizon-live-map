package br.com.mrocigno.horizonlivemap.map.ui.map.pages

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.arch.toolkit.delegate.viewProvider
import br.com.mrocigno.horizonlivemap.core.functions.reqExtraProvider
import br.com.mrocigno.horizonlivemap.core.functions.baseUrl
import br.com.mrocigno.horizonlivemap.core.helpers.load
import br.com.mrocigno.horizonlivemap.map.R
import br.com.mrocigno.horizonlivemap.map.ui.map.model.ItemData

class BottomSheetHeaderFragment : Fragment(R.layout.fragment_map_bottom_sheet_header) {

    private val title: TextView by viewProvider(R.id.title)
    private val description: TextView by viewProvider(R.id.description)
    private val icon: ImageView by viewProvider(R.id.icon)

    private val data: ItemData by reqExtraProvider(DATA)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text = data.title
        description.text = data.about
        icon.load(baseUrl(data.marker.image))
    }

    companion object {
        private const val DATA = "data"

        fun newInstance(data: ItemData): BottomSheetHeaderFragment {
            val instance = BottomSheetHeaderFragment()
            instance.arguments = Bundle().apply {
                putParcelable(DATA, data)
            }
            return instance
        }
    }
}