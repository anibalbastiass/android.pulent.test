package com.anibalbastias.android.pulentapp.util

import androidx.databinding.BindingAdapter
import android.widget.ImageView

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    imageUrl?.let {
        loadImageWithoutPlaceholder(imageUrl)
    }
}