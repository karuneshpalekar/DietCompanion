package com.karunesh.azureauth.presentation.recommendation.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.azure.data.AzureData
import com.azure.data.model.Document
import com.azure.data.model.PermissionMode
import com.azure.data.model.Query
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.karunesh.azureauth.BuildConfig
import com.karunesh.azureauth.R
import com.karunesh.azureauth.business.domain.util.data
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DietDetailsFragment : Fragment() {

    private val args: DietDetailsFragmentArgs by navArgs()
    private lateinit var navigateBack: ImageButton
    private val viewModel: DietDetailViewModel by viewModels()
    private lateinit var title: TextView
    private lateinit var aim: TextView
    private lateinit var claim: TextView
    private lateinit var description: TextView
    private lateinit var progressBar: CircularProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_diet_details, container, false)
        AzureData.configure(
            requireContext(),
            ACCOUNT_NAME,
            BuildConfig.COSMOS_KEY,
            PermissionMode.All
        )
        navigateBack = view.findViewById(R.id.navigate_diet_detail_back)
        title = view.findViewById(R.id.diet_detail_name_txt)
        aim = view.findViewById(R.id.diet_detail_aim_txt)
        claim = view.findViewById(R.id.diet_detail_claim_txt)
        description = view.findViewById(R.id.diet_detail_description_txt)
        progressBar = view.findViewById(R.id.diet_detail_progress_bar)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        progressBar.visibility = View.VISIBLE
        val query = args.dietItem.title?.let {
            Query.select()
                .from(COLLECTION)
                .where(ID, it)
        }

        if (query != null) {
            lifecycleScope.launch {
                viewModel.queryDetailDiet(
                    COLLECTION,
                    DATABASE_NAME,
                    query = query
                )
            }

        }

        observeViewModel()
        navigateBack.setOnClickListener {
            findNavController().navigate(R.id.action_dietDetailsFragment_to_dietFragment)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dietDetailState.collect { viewState ->
                    when (viewState) {
                        is DietDetailState.DietDetailList -> {
                            viewState.dietDetailList.data?.resource?.items?.forEach {
                                title.text = it.id
                                aim.text = it.aim
                                claim.text = it.claim
                                description.text = it.description
                                progressBar.visibility = View.GONE

                            }
                        }
                    }
                }
            }
        }
    }

    companion object{
        private const val COLLECTION ="collection"
        private const val ACCOUNT_NAME="karuneshcosmos"
        private const val DATABASE_NAME="diets"
        private const val ID="id"
    }
}

class DetailsDocument(id: String?) : Document(id) {
    var aim: String? = null
    var claim: String? = null
    var description: String? = null
}