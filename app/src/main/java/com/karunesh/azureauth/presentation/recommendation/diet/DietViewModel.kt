package com.karunesh.azureauth.presentation.recommendation.diet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karunesh.azureauth.presentation.recommendation.RecommendationClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor() : ViewModel() {


    private val _selectedMovies: MutableLiveData<List<DietItem>> = MutableLiveData()
    val selectedMovies: LiveData<List<DietItem>> get() = _selectedMovies

    fun addSelectedMovie(dietItem: List<DietItem>) {
        _selectedMovies.value = dietItem
    }

}