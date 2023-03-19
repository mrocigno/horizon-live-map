package br.com.mrocigno.sdk

import br.com.mrocigno.sandman.core.TheDreamingProvider
import br.com.mrocigno.sandman.corinthian.addCorinthian
import br.com.mrocigno.sandman.matthew.addMatthew


class TheDreaming : TheDreamingProvider() {

    override fun setupTheDreaming() {
        addCorinthian()
        addMatthew()
    }
}