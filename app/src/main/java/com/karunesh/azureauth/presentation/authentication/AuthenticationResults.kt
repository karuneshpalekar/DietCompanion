package com.karunesh.azureauth.presentation.authentication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthenticationResults(
    var id: String? = null,
    var name: String? = null,
    var country: String? = null,
    var city: String? = null,
    var address: String? = null,
    var surname: String? = null
) : Parcelable