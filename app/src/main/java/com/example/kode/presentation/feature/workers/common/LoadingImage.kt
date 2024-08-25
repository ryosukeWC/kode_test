package com.example.kode.presentation.feature.workers.common

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.kode.R
import com.google.android.material.imageview.ShapeableImageView

fun ShapeableImageView.loadImageOrDefault(imageUrl : String?, context: Context) {
    val imageLoader = ImageLoader.Builder(context).build()
    val request = ImageRequest.Builder(context)
        .data(imageUrl ?: R.drawable.goose_plug)
        .target(this)
        .build()
    imageLoader.enqueue(request)
}