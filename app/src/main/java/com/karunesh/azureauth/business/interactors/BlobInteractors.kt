package com.karunesh.azureauth.business.interactors

import com.karunesh.azureauth.business.interactors.blob.GetImage
import com.karunesh.azureauth.business.interactors.blob.ListImages
import com.karunesh.azureauth.business.interactors.blob.UploadImage

data class BlobInteractors(
    val uploadImage: UploadImage,
    val listImages: ListImages,
    val getImage: GetImage
)