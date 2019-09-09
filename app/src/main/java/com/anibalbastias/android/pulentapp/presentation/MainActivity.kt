package com.anibalbastias.android.pulentapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.presentation.ui.entry.EntryFragment
import com.anibalbastias.android.pulentapp.presentation.ui.entry.OnBackPressedListener
import com.anibalbastias.android.pulentapp.presentation.ui.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_entry.*

class MainActivity : AppCompatActivity() {

    lateinit var entryFragment: EntryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appComponent().inject(this)

        if (savedInstanceState == null) {
            entryFragment = EntryFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, entryFragment)
                .commitNow()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        if (fragment is SearchFragment) {
            entryFragment = (fragment as? EntryFragment)!!
        }
    }

    override fun onBackPressed() {
        val currentFragment = entryFragment.nav_host_fragment?.childFragmentManager?.fragments?.first()
        val controller = Navigation.findNavController(this,
            R.id.nav_host_fragment
        )

        if (currentFragment is OnBackPressedListener)
            (currentFragment as OnBackPressedListener).onBackPressed()
        else if (!controller.popBackStack())
            finish()
    }
}
