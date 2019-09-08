package com.anibalbastias.android.pulentapp.util

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.anibalbastias.android.pulentapp.ui.search.interfaces.SearchListener

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    imageUrl?.let {
        loadImageWithoutPlaceholder(imageUrl)
    }
}

@BindingAdapter("sendEditTextAction")
fun EditText.sendEditTextAction(callback: SearchListener?) {
    setOnEditorActionListener { view, actionId, event ->
        view?.hideKeyboard()
        try {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER) {
                callback?.onSendActionByHeaderFinder()
                true
            }
        } catch (e: Exception) {
            callback?.onSendActionByHeaderFinder()
        }
        true
    }
}