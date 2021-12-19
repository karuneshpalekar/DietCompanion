package com.karunesh.azureauth.presentation.image

import android.content.res.Configuration
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.window.WindowManager
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.lang.Integer.max
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

@AndroidEntryPoint
class ImageFragment : Fragment() {


    private lateinit var outputDirectory: File
    private lateinit var viewFinder: PreviewView
    private lateinit var captureImage: ImageButton
    private lateinit var rotateButton: ImageButton
    private lateinit var navigateToAnotherFragment: ImageButton
    private val viewModel: ImageViewModel by viewModels()
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var windowManager: WindowManager
    private val args by navArgs<ImageFragmentArgs>()
    private lateinit var navigateBack: ImageButton


    private lateinit var cameraExecutor: ExecutorService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        viewFinder = view.findViewById(R.id.view_finder)
        captureImage = view.findViewById(R.id.camera_capture_button)
        rotateButton = view.findViewById(R.id.camera_switch_button)
        navigateToAnotherFragment = view.findViewById(R.id.navigate_to_another_fragment)
        navigateBack = view.findViewById(R.id.navigate_image_back)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        windowManager = WindowManager(view.context)
        outputDirectory = MainActivity.getOutputDirectory(requireContext())
        viewFinder.post {
            updateCameraUi()
            setUpCamera()
            navigateToAnotherFragment.setOnClickListener {
                val directions =
                    ImageFragmentDirections.actionImageFragmentToHomeFragment(args.authResult)
                findNavController().navigate(directions)
            }
            navigateBack.setOnClickListener {
                val directions =
                    ImageFragmentDirections.actionImageFragmentToHomeFragment(args.authResult)
                findNavController().navigate(directions)
            }
        }


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        bindCameraUseCases()
        updateCameraSwitchButton()
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }
            updateCameraSwitchButton()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases() {

        val metrics = windowManager.getCurrentWindowMetrics().bounds
        val screenAspectRatio = aspectRatio(metrics.width(), metrics.height())
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .build()
        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
            preview?.setSurfaceProvider(viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun updateCameraUi() {

        lifecycleScope.launch(Dispatchers.Main) {
            captureImage.setOnClickListener {

                imageCapture?.let { imageCapture ->
                    val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
                    val metadata = ImageCapture.Metadata().apply {
                        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                    }
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                        .setMetadata(metadata)
                        .build()
                    imageCapture.takePicture(
                        outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                            override fun onError(exc: ImageCaptureException) {
                                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                            }

                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                                val fileName = savedUri.toFile().name
                                Log.d(TAG, "Photo capture succeeded: $savedUri")

                                val imageStream: InputStream? =
                                    activity?.contentResolver?.openInputStream(savedUri)
                                val imageLength = imageStream?.available()

                                Log.d(TAG, "The fileName is $fileName")
                                lifecycleScope.launch(Dispatchers.Main) {
                                    imageStream?.let { inputStream ->
                                        imageLength?.let {
                                            if (args.authResult?.id != null) {
                                                viewModel.uploadImage(
                                                    args.authResult?.id!!,
                                                    fileName,
                                                    inputStream,
                                                    imageLength
                                                )
                                            }
                                        }
                                    }
                                }
                                val mimeType = MimeTypeMap.getSingleton()
                                    .getMimeTypeFromExtension(savedUri.toFile().extension)
                                MediaScannerConnection.scanFile(
                                    context,
                                    arrayOf(savedUri.toFile().absolutePath),
                                    arrayOf(mimeType)
                                ) { _, uri ->
                                    Log.d(TAG, "Image capture scanned into media store: $uri")
                                }
                            }
                        })
                }
            }
            rotateButton.let {
                it.isEnabled = false
                it.setOnClickListener {

                    lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                        CameraSelector.LENS_FACING_BACK
                    } else {
                        CameraSelector.LENS_FACING_FRONT
                    }
                    bindCameraUseCases()
                }
            }

        }
    }

    private fun updateCameraSwitchButton() {
        try {
            rotateButton.isEnabled =
                hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            rotateButton.isEnabled = false
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }


    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()

    }

    companion object {

        private const val TAG = "ImageFragment"
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }

}