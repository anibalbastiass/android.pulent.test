package com.anibalbastias.android.pulentapp.presentation.util

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.android.pulentapp.domain.search.model.SearchRecentRealmData
import com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces.SearchListener
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.BaseBindClickHandler
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.SingleLayoutBindRecyclerAdapter
import com.anibalbastias.android.pulentapp.presentation.util.widget.WrapContentLinearLayoutManager

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

@BindingAdapter(value = ["loadAdapterData", "loadAdapterLayout", "loadAdapterListener"], requireAll = false)
fun <T> RecyclerView.loadAdapterData(list: MutableList<T>?, layout: Int?, callback: BaseBindClickHandler<T>? = null) {
    context?.run {
        layout?.let { layoutId ->
            layoutManager = WrapContentLinearLayoutManager(context)
            adapter = SingleLayoutBindRecyclerAdapter(layoutId, list, callback)
            runLayoutAnimation()
        }
    }
}