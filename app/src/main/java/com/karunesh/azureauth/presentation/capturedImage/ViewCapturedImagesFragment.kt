package com.karunesh.azureauth.presentation.capturedImage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.home.HomeState
import com.karunesh.azureauth.presentation.home.adapter.ViewCapturedImagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewCapturedImagesFragment : Fragment() {

    private lateinit var navigateBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private val viewCapturedImagesAdapter = ViewCapturedImagesAdapter()
    private val viewModel: ViewCapturedImagesViewModel by viewModels()
    private val args: ViewCapturedImagesFragmentArgs by navArgs()
    private lateinit var progressBar: CircularProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_captured_images, container, false)
        navigateBack = view.findViewById(R.id.navigate_back_list_images)
        recyclerView = view.findViewById(R.id.captured_images_recycler_view)
        progressBar = view.findViewById(R.id.captured_image_progress_bar)
        val recyclerViewLayoutManager =
            LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        recyclerView.layoutManager = recyclerViewLayoutManager
        recyclerView.adapter = viewCapturedImagesAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            args.authResults?.id?.let { id ->
                progressBar.visibility = View.VISIBLE
                viewModel.performDataSync(id)
            }

        }
        navigateBack.setOnClickListener {
            val directions =
                ViewCapturedImagesFragmentDirections.actionViewCapturedImagesFragmentToHomeFragment(
                    args.authResults
                )
            findNavController().navigate(directions)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataViewState.collect { viewState ->
                    when (viewState) {
                        is HomeState.ListOfImages -> {
                            viewState.listOfImages?.let { list ->
                                viewCapturedImagesAdapter.getData(list)
                                progressBar.visibility = View.GONE
                            }
                        }
                        is HomeState.ListOfOddImages -> {
                            viewState.listOfOddImages?.forEach { name ->
                                args.authResults?.id?.let { idOfCollection ->
                                    viewModel.getImage(
                                        idOfCollection,
                                        name
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }


}