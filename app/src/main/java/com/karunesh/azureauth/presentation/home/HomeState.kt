package com.karunesh.azureauth.presentation.home

import com.karunesh.azureauth.presentation.mediastore.ImageContent

sealed class HomeState {

    object Loading : HomeState()

    data class ListOfImages(var listOfImages: List<ImageContent>? = null) : HomeState()

    data class ListOfOddImages(var listOfOddImages: List<String>? = null) : HomeState()
}