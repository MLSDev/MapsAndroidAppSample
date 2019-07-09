package com.mlsdev.mapsappsample.url

import android.net.Uri

object UrlBuilder {

    fun buildUrl(params: Map<String, String>): String {
        val uriBuilder = Uri.parse(Constants.BASE_URL).buildUpon()
        for ((key, value) in params)
            uriBuilder.appendQueryParameter(key, value)
        return uriBuilder.build().toString()
    }

}
