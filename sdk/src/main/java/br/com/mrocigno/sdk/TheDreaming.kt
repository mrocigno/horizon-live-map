package br.com.mrocigno.sdk

import br.com.mrocigno.bigbrother.core.BigBrotherProvider
import br.com.mrocigno.bigbrother.database.addDatabasePage
import br.com.mrocigno.bigbrother.log.addLogPage
import br.com.mrocigno.bigbrother.network.addNetworkPage

class TheDreaming : BigBrotherProvider() {

    override val isEnabled: Boolean = true

    override fun setupPages() {
        addDatabasePage()
        addNetworkPage()
        addLogPage()
    }
}