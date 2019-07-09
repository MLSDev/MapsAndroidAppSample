package com.mlsdev.mapsappsample.url;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Map;

public class UrlBuilder {

    public static String buildUrl(@NonNull Map<String, String> params) {
        Uri.Builder uriBuilder = Uri.parse(Constants.BASE_URL).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet())
            uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        return uriBuilder.build().toString();
    }

}
