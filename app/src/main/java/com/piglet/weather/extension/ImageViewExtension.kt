package com.piglet.weather.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition

fun ImageView?.load(
    context: Context?,
    url: String?,
    placeholder: Int? = null,
    scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER
) {

    when (isValidate(this, context)) {
        true -> initGlide(context!!, url, placeholder, scaleType).into(this!!)
        false -> {
            this?.context?.applicationContext?.let { applicationContext ->
                initGlide(
                    applicationContext,
                    url,
                    placeholder,
                    scaleType
                ).into(this)
            }
        }
    }
}

fun ImageView?.loadGift(
    context: Context?,
    resource: Int?,
    placeholder: Int? = null
) {

    when (isValidate(this, context)) {
        true -> this?.let { loadGiftWithGlide(it, context, resource, placeholder) }
        false -> {
            try {
                this?.context?.applicationContext?.let { applicationContext ->
                    loadGiftWithGlide(this, applicationContext, resource, placeholder)
                }
            } catch (error: Exception) {
                val handlingExceptionMap = mapOf(
                    "Key" to "ImageViewExtension.loadGift()",
                    "Value" to "Unexpected error with $error"
                )
            }
        }
    }
}

private fun initGlide(
    context: Context,
    url: String?,
    placeholder: Int? = null,
    scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
): RequestBuilder<Drawable> {

    return Glide.with(context)
        .load(url)
        .apply(
            RequestOptions()
                .apply {
                    format(DecodeFormat.PREFER_RGB_565)
                    diskCacheStrategy(DiskCacheStrategy.DATA)
                    placeholder?.let {
                        placeholder(it)
                        error(it)
                    }
                }
                .apply {
                    if (scaleType == ImageView.ScaleType.FIT_CENTER) {
                        this.fitCenter()
                    } else if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                        this.centerCrop()
                    }
                }
        )
}


private fun isValidate(imageView: ImageView?, context: Context?): Boolean {
    return (imageView != null && context != null)
}

private fun loadMultipleTransformationWithGlide(
    imageView: ImageView,
    context: Context?,
    url: String?,
    transformations: MultiTransformation<Bitmap>,
    placeholder: Int?
) {
    Glide.with(context!!)
        .load(url)
        .apply(
            RequestOptions()
                .transform(transformations)
                .apply {
                    placeholder?.let {
                        placeholder(it)
                        error(it)
                    }
                }
        )
        .into(object : DrawableImageViewTarget(imageView) {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                super.onResourceReady(resource, transition)
                this.setDrawable(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                super.onLoadFailed(errorDrawable)
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                super.onLoadStarted(placeholder)
            }
        })
}

private fun loadGiftWithGlide(
    imageView: ImageView,
    context: Context?,
    resource: Int?,
    placeholder: Int? = null
) {
    Glide.with(context!!)
        .asGif()
        .load(resource)
        .apply(
            RequestOptions()
                .apply {
                    placeholder?.let {
                        error(it)
                    }
                }
        )
        .into(imageView)
}
