package com.karunesh.azureauth.business.data.abstraction

import com.karunesh.azureauth.business.domain.util.Result
import java.io.ByteArrayOutputStream
import java.io.InputStream

interface BlobStorageB {


    suspend fun uploadImage(
        id: String,
        fileName: String,
        inputStream: InputStream,
        imageLength: Int
    )

    suspend fun listImages(id: String): Result<Array<String>>

    suspend fun getImage(
        id: String,
        name: String,
        imageStream: ByteArrayOutputStream,
        imageLength: Int
    ): Result<Boolean>
}