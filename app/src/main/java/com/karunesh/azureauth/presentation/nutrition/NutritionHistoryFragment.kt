package com.karunesh.azureauth.presentation.nutrition

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
import androidx.recyclerview.widget.RecyclerView
import com.azure.data.AzureData
import com.azure.data.model.PermissionMode
import com.azure.data.model.service.ListResponse
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.karunesh.azureauth.BuildConfig
import com.karunesh.azureauth.R
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.presentation.nutrition.adapter.history.NutritionHistoryAdapter
import com.karunesh.azureauth.presentation.nutrition.adapter.history.NutritionHistoryListener
import com.karunesh.azureauth.presentation.nutrition.state.NutritionHistoryState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NutritionHistoryFragment : Fragment(), NutritionHistoryListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var navigateBack: ImageButton
    private lateinit var progressBar: CircularProgressIndicator
    private val viewModel: NutritionViewModel by viewModels()
    private val args: NutritionHistoryFragmentArgs by navArgs()
    private val nutritionHistoryAdapter =
        NutritionHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_nutrition_history, container, false)
        recyclerView = view.findViewById(R.id.history_nutrition_recycler_view)
        navigateBack = view.findViewById(R.id.navigate_nutrition_back)
        recyclerView.adapter = nutritionHistoryAdapter
        progressBar = view.findViewById(R.id.nutrition_history_progress_bar)
        nutritionHistoryAdapter.listener = this
        try {
            AzureData.configure(
                requireContext(),
                ACCOUNT_NAME,
                BuildConfig.COSMOS_KEY,
                PermissionMode.All
            )
        } catch (e: Exception) {

        }
        lifecycleScope.launch {
            args.authResults?.id?.let { id ->
                progressBar.visibility = View.VISIBLE
                viewModel.getDocuments(id, PERSONAL_DATABASE)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateBack.setOnClickListener {
            val directions =
                NutritionHistoryFragmentDirections.actionNutritionHistoryFragmentToHomeFragment(args.authResults)
            findNavController().navigate(directions)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.nutritionHistory.collect { viewState ->
                    when (viewState) {
                        is NutritionHistoryState.NutritionDataList -> {
                            when (viewState.listOfNutritionData) {
                                is Result.Success -> {
                                    val list = (viewState.listOfNutritionData as?
                                            Result.Success<ListResponse<out NutritionDataDocument>>)?.data?.resource?.items
                                    list?.let { data ->
                                        nutritionHistoryAdapter.getData(data)
                                        progressBar.visibility = View.GONE
                                    }
                                }

                                is Result.Error -> {

                                }
                            }

                        }
                    }
                }
            }
        }
    }


    override fun onRecyclerViewItemClicked(product: NutritionDataDocument) {
        val nutritionHistoryDataClass = NutritionHistoryDataClass(
            name = product.name,
            description = product.description,
            nutrientDataBankNumber = product.nutrientDataBankNumber,
            alphaCarotene = product.alphaCarotene,
            betaCarotene = product.betaCarotene,
            betaCryptoxanthin = product.betaCryptoxanthin,
            carbohydrate = product.carbohydrate,
            cholestrol = product.cholestrol,
            choline = product.choline,
            fiber = product.fiber,
            luteinandZeaxanthin = product.luteinandZeaxanthin,
            lycopene = product.lycopene,
            niacain = product.niacain,
            protein = product.protein,
            retinol = product.retinol,
            riboflavin = product.riboflavin,
            selenium = product.selenium,
            sugar = product.sugar,
            thiamin = product.thiamin,
            water = product.water,
            monoSaturatedFat = product.monoSaturatedFat,
            polySaturatedFat = product.polySaturatedFat,
            saturatedFat = product.polySaturatedFat,
            totalLipid = product.totalLipid,
            calcium = product.calcium,
            copper = product.copper,
            iron = product.iron,
            magnesium = product.magnesium,
            phosphorus = product.phosphorus,
            potassium = product.potassium,
            sodium = product.sodium,
            zinc = product.zinc,
            vitaminARae = product.vitaminARae,
            vitaminB12 = product.vitaminB12,
            vitaminB6 = product.vitaminB6,
            vitaminC = product.vitaminC,
            vitaminE = product.vitaminE,
            vitaminK = product.vitaminK,
            date = product.date,
            authResults = args.authResults
        )

        val directions =
            NutritionHistoryFragmentDirections.actionNutritionHistoryFragmentToNutritionHistoryDetailsFragment(
                nutritionHistoryDataClass
            )
        findNavController().navigate(directions)
    }

    companion object {
        private const val PERSONAL_DATABASE = "personal"
        private const val ACCOUNT_NAME = "karuneshcosmos"
    }

}