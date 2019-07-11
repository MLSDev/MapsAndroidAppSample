package com.mlsdev.mapsappsample.markerclustering

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


object MarkerItemReader {

    /*
     * This matches only once in whole input,
     * so Scanner.next returns whole InputStream as a String.
     * http://stackoverflow.com/a/5445161/2183804
     */
    private const val REGEX_INPUT_BOUNDARY_BEGINNING = "\\A"

    @Throws(JSONException::class)
    fun read(inputStream: InputStream): List<MarkerItem> {
        val items = ArrayList<MarkerItem>()
        val json = Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next()
        val gson = Gson()

        val listType = object : TypeToken<List<MarkerItem>>() {}.type

        items.addAll(gson.fromJson(json, listType))

        return items
    }

}