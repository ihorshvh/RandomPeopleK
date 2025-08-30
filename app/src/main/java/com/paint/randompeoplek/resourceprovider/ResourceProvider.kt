package com.paint.randompeoplek.resourceprovider

import android.content.Context

interface ResourceProvider {
    fun getString(id: Int): String
}

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(id: Int): String {
        return context.getString(id)
    }
}