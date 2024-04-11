package com.paint.randompeoplek.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class User(
    val id: String,
    val name: Name,
    val location: String,
    val email: String,
    val phone: String,
    val picture: Picture
) : Parcelable
