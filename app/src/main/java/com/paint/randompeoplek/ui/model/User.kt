package com.paint.randompeoplek.ui.model

import android.os.Parcel
import android.os.Parcelable

data class User(val name : Name,
                val location : String,
                val email : String,
                val phone : String,
                val picture : Picture) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Name::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(Picture::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(name, flags)
        parcel.writeString(location)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeParcelable(picture, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
