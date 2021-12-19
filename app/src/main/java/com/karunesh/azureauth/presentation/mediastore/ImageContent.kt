package com.karunesh.azureauth.presentation.mediastore

import android.net.Uri

data class ImageContent(
    val contentUri: Uri? = null,
    var id: Long? = null,
    val name: String? = null,
    val relativePath: String? = null
)