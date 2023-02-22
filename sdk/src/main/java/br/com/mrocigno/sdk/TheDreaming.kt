package br.com.mrocigno.sdk

import br.com.mrocigno.sandman.TheDreaming
import br.com.mrocigno.sandman.TheDreamingProvider
import br.com.mrocigno.sandman.network.NetworkFragment

class TheDreaming : TheDreamingProvider() {

    override fun onCreate(): Boolean {
        TheDreaming.addNightmare("Corintio") { NetworkFragment() }
        return super.onCreate()
    }
}