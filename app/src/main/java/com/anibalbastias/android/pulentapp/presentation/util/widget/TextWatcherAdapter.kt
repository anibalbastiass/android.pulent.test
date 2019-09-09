package com.anibalbastias.android.pulentapp.presentation.util.widget

/**
 * Created by anibalbastias on 2019-05-10.
 */

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TextWatcherAdapter(private val view: EditText,
                         private val listener: TextWatcherListener
) : TextWatcher {

    interface TextWatcherListener {
        fun onTextChanged(view: EditText, text: String)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        listener.onTextChanged(view, s.toString())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // pass
    }

    override fun afterTextChanged(s: Editable) {
        // pass
    }

}