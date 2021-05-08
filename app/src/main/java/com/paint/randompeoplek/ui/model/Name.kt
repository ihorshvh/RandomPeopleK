package com.paint.randompeoplek.ui.model

import android.os.Parcel
import android.os.Parcelable

data class Name(val shortName : String,
                val fullName : String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shortName)
        parcel.writeString(fullName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Name> {
        override fun createFromParcel(parcel: Parcel): Name {
            return Name(parcel)
        }

        override fun newArray(size: Int): Array<Name?> {
            return arrayOfNulls(size)
        }
    }
}
