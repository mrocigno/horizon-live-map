package br.com.mrocigno.horizonlivemap.ui

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import br.com.mrocigno.horizonlivemap.R
import br.com.mrocigno.horizonlivemap.core.extension.transparentToolbar
import br.com.mrocigno.horizonlivemap.core.providers.navProvider
import java.lang.IllegalStateException
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navController by navProvider(R.id.main_nav_host)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentToolbar()
        navController.navigate(R.id.home_navigation)
    }
}