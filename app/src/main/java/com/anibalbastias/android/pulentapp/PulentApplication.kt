package com.anibalbastias.android.pulentapp

import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.fragment.app.Fragment
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.component.ApplicationComponent
import com.anibalbastias.android.pulentapp.component.DaggerApplicationComponent
import com.anibalbastias.android.pulentapp.module.ApplicationModule

class PulentApplication : MultiDexApplication() {

    companion object {
        lateinit var appContext: Context
        var applicationComponent: ApplicationComponent? = null
    }

    init {
        //Initialize Tenant Always first

    }

    override fun onCreate() {
        super.onCreate()
        appComponent().inject(this)
        appContext = this
    }
}

private fun buildDagger(context: Context): ApplicationComponent {
    if (PulentApplication.applicationComponent == null) {
        PulentApplication.applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(context as PulentApplication))
            .build()
    }
    return PulentApplication.applicationComponent!!
}

fun Context.appComponent(): ApplicationComponent {
    return buildDagger(this.applicationContext)
}

fun Fragment.appComponent(): ApplicationComponent {
    return buildDagger(this.context!!.applicationContext)
}

fun BaseModuleFragment.appComponent(): ApplicationComponent {
    return buildDagger(this.context!!.applicationContext)
}