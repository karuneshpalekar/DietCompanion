package com.karunesh.azureauth.presentation.recommendation.diet

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DietItem(

    var id: Int = 0,
    var title: String? = null,
    var dietType: String? = null,
    var rating: Float? = null,
    val count: Int = 0,
    var selected: Boolean ?= false

):Parcelable