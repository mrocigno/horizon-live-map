package br.com.mrocigno.horizonlivemap.core.providers

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import java.lang.IllegalStateException
import kotlin.reflect.KProperty

fun navProvider(@IdRes navId: Int? = null) = NavControllerProvider(navId)

class NavControllerProvider(@IdRes val navId: Int?) {

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): NavController {
        return when (thisRef) {
            is AppCompatActivity -> thisRef.getOnActivity()
            is Fragment -> thisRef.getOnFragment()
            else -> throw IllegalStateException("NavController probably cannot be found")
        }
    }

    private fun AppCompatActivity.getOnActivity() : NavController {
        val navHost = supportFragmentManager.findFragmentById(navId!!) as? NavHostFragment
        return navHost?.navController ?: throw IllegalStateException("NavController not found")
    }

    private fun Fragment.getOnFragment() : NavController {
        return findNavController()
    }
}