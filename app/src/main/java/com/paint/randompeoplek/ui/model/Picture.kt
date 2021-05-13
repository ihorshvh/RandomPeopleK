package com.paint.randompeoplek.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Picture(val medium : String, val thumbnail : String) : Parcelable
