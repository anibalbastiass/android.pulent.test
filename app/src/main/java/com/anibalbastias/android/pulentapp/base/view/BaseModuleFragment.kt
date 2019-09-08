package com.anibalbastias.android.pulentapp.base.view

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.anibalbastias.android.pulentapp.domain.base.db.RealmManager
import com.anibalbastias.android.pulentapp.base.module.ViewModelFactory
import com.anibalbastias.android.pulentapp.presentation.util.inflate
import javax.inject.Inject

abstract class BaseModuleFragment : Fragment() {

    abstract fun tagName(): String
    abstract fun layoutId(): Int

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var navBaseViewModel: NavBaseViewModel
    lateinit var sharedViewModel: SharedViewModel

    var mResources: Resources? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RealmManager.open()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        retainInstance = true
        mResources = resources

        try {
            return container?.inflate(layoutId())
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onDestroy() {
        RealmManager.close()
        super.onDestroy()
    }

    var navController: NavController? = null

    fun setNavController(view: View?) {
        view?.let {
            navController = Navigation.findNavController(it)
            navBaseViewModel.getNewDestination().observe(this, Observer { dest ->
                if (dest.first.status == ResourceState.LOADING) {
                    dest.second?.let { listener ->
                        listener.onNextNavigate(dest.first.consume())
                    } ?: run {
                        nextNavigate(dest.first.consume())
                    }
                    dest.first.status = ResourceState.DEFAULT
                }
            })
        }
    }

    private fun navigateToUp() {
        navController?.navigateUp()
    }

    fun nextNavigate(nav: Int?, bundle: Bundle? = null) {
        when (nav) {
            -1 -> navigateToUp()
            else -> navigate(nav ?: 0, bundle)
        }
    }

    private fun navigate(destination: Int, bundle: Bundle? = null) {
        if (destination == 0)
            navController?.navigateUp()
        else {
            try {
                navController?.navigate(destination, bundle)
            } catch (e: IllegalArgumentException) {
                // Actions for finish flow
                activity?.finish()
            }
        }
    }
}