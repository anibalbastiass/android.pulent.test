package com.anibalbastias.android.pulentapp.base.view

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anibalbastias.android.pulentapp.domain.base.db.RealmManager
import com.anibalbastias.android.pulentapp.base.module.ViewModelFactory
import com.anibalbastias.android.pulentapp.presentation.util.inflate
import javax.inject.Inject

abstract class BaseModuleFragment : Fragment() {

    abstract fun tagName(): String
    abstract fun layoutId(): Int

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
}