package com.karunesh.azureauth.presentation.capturedImage

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.business.interactors.BlobInteractors
import com.karunesh.azureauth.presentation.home.HomeState
import com.karunesh.azureauth.presentation.mediastore.ImageContent
import com.karunesh.azureauth.presentation.mediastore.MediaStoreDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ViewCapturedImagesViewModel
@Inject
constructor(
    private val blobInteractors: BlobInteractors,
    mediaStoreDelegate: MediaStoreDelegate
) : ViewModel(), MediaStoreDelegate by mediaStoreDelegate {

    private val _dataViewState: MutableSharedFlow<HomeState> = MutableSharedFlow()
    val dataViewState: SharedFlow<HomeState> get() = _dataViewState


    suspend fun performDataSync(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfLocalImages = async {
                getImagesLocally()
            }
            val listOfRemoteImages = async {
                getImageNamesRemotely(id)
            }

            val deferredListOfLocalImages = listOfLocalImages.await()
            val deferredListOfRemoteImages = listOfRemoteImages.await()
            if (deferredListOfLocalImages != null && deferredListOfRemoteImages != null) {
                val listOfNames = mutableListOf<String>()
                deferredListOfLocalImages.forEach { imageContent ->
                    imageContent.name?.let { name -> listOfNames.add(name) }
                }
                if (!deferredListOfRemoteImages.containsAll(listOfNames)) {
                    val listOfOdd = findOdd(listOfNames, deferredListOfRemoteImages)
                    _dataViewState.emit(HomeState.ListOfOddImages(listOfOdd))
                }
            }
        }
    }

    private suspend fun getImagesLocally(): List<ImageContent>? {
        var list: List<ImageContent>? = null
        viewModelScope.launch(Dispatchers.IO) {
            list = getData()
            list?.forEach {
                Log.d(TAG, "In ViewModel uri is ${it.contentUri}")
            }
            _dataViewState.emit(HomeState.ListOfImages(list))
        }
        return list
    }


    suspend fun getImageNamesRemotely(id: String): List<String>? {
        var list: List<String>? = null
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = blobInteractors.listImages.outputUploadResults(id)) {
                is Result.Success -> {
                    list = result.data.toList()
                }
                is Result.Error -> {
                    Log.d(TAG, "The error is ${result.exception}")
                }
            }
        }
        return list
    }

    suspend fun getImage(
        id: String,
        name: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageStream: ByteArrayOutputStream = ByteArrayOutputStream()
            val imageLength = 0
            blobInteractors.getImage.outputUploadResults(id, name, imageStream, imageLength)
            val buffer = imageStream.toByteArray()
            val bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.size)
            saveImage(bitmap, name)
        }

    }


    private fun findOdd(list1: List<String>, list2: List<String>): List<String> {
        val list = mutableListOf<String>()
        list2.forEach { value ->
            val contains = list1.contains(value)
            if (!contains) {
                list.add(value)
            }
        }
        return list.toList()
    }

    companion object {
        private const val TAG = "ViewCaptured"
    }
}