package com.karunesh.azureauth.presentation.nutrition

import android.os.Parcelable
import com.karunesh.azureauth.presentation.authentication.AuthenticationResults
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NutritionHistoryDataClass(
    var name: String? = null,
    var description: String? = null,
    var nutrientDataBankNumber: String? = null,
    var alphaCarotene: String? = null,
    var betaCarotene: String? = null,
    var betaCryptoxanthin: String? = null,
    var carbohydrate: String? = null,
    var cholestrol: String? = null,
    var choline: String? = null,
    var fiber: String? = null,
    var luteinandZeaxanthin: String? = null,
    var lycopene: String? = null,
    var niacain: String? = null,
    var protein: String? = null,
    var retinol: String? = null,
    var riboflavin: String? = null,
    var selenium: String? = null,
    var sugar: String? = null,
    var thiamin: String? = null,
    var water: String? = null,
    var monoSaturatedFat: String? = null,
    var polySaturatedFat: String? = null,
    var saturatedFat: String? = null,
    var totalLipid: String? = null,
    var calcium: String? = null,
    var copper: String? = null,
    var iron: String? = null,
    var magnesium: String? = null,
    var phosphorus: String? = null,
    var potassium: String? = null,
    var sodium: String? = null,
    var zinc: String? = null,
    var vitaminARae: String? = null,
    var vitaminB12: String? = null,
    var vitaminB6: String? = null,
    var vitaminC: String? = null,
    var vitaminE: String? = null,
    var vitaminK: String? = null,
    var date: Date? = null,
    var authResults: AuthenticationResults? = null
) : Parcelable