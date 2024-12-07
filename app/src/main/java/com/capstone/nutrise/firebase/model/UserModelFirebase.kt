package com.capstone.nutrise.firebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModelFirebase(
    var name: String? = null,
    var email: String,
    var password: String
) : Parcelable