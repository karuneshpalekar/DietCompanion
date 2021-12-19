package com.karunesh.azureauth.datasource.abstraction

import com.karunesh.azureauth.business.domain.util.Result
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

interface BlobStorage {


    suspend fun uploadImage(id: String,imageName:String, inputStream: InputStream, imageLength: Int)

    suspend fun listImages(id: String): Result<Array<String>>

    suspend fun getImage(id: String, name: String, imageStream: ByteArrayOutputStream, imageLength: Int):Result<Boolean>
}