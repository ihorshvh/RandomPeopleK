package com.paint.randompeoplek.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Name(val shortName : String, val fullName : String) : Parcelable
