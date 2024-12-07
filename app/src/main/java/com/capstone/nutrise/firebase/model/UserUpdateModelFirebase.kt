package com.capstone.nutrise.firebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUpdateModelFirebase(
    var name : String?,
    var password : String?
) : Parcelable