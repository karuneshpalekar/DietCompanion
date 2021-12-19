package com.karunesh.azureauth.presentation.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunesh.azureauth.business.interactors.BlobInteractors
import com.karunesh.azureauth.business.interactors.blob.UploadImageDataClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject
constructor(
    private val interactors: BlobInteractors
) : ViewModel() {

    fun uploadImage(
        id: String,
        fileName: String,
        inputStream: InputStream,
        imageLength: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataClass = UploadImageDataClass(id, fileName, inputStream, imageLength)
            interactors.uploadImage.outputUploadResults(dataClass)
        }
    }
}