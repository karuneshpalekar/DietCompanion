package com.karunesh.azureauth.presentation.nutrition

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
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
import com.azure.data.model.Query
import com.azure.data.model.partition.PartitionKey
import com.azure.data.model.service.ListResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karunesh.azureauth.BuildConfig
import com.karunesh.azureauth.R
import com.karunesh.azureauth.business.domain.util.Result.Error
import com.karunesh.azureauth.business.domain.util.Result.Success
import com.karunesh.azureauth.presentation.nutrition.adapter.AllNutritionAdapter
import com.karunesh.azureauth.presentation.nutrition.adapter.AllNutritionListener
import com.karunesh.azureauth.presentation.nutrition.state.NutritionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class NutritionFragment : Fragment(), AllNutritionListener {

    @PartitionKey
    var key = ""

    private val viewModel: NutritionViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private val allNutritionAdapter = AllNutritionAdapter()
    private val selectedNutritionData = mutableListOf<NutritionDataDocument>()
    private lateinit var addData: MaterialButton
    private lateinit var searchData: TextInputEditText
    private lateinit var searchDataOutput: TextInputLayout
    private lateinit var search: AppCompatImageButton
    private val args: NutritionFragmentArgs by navArgs()
    private lateinit var navigateBack: ImageButton
    private lateinit var progressBar: CircularProgressIndicator
    private var nutritionList: MutableList<NutritionDataDocument>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_information, container, false)
        recyclerView = view.findViewById(R.id.all_nutrition_recycler_view)
        addData = view.findViewById(R.id.addListData)
        searchData = view.findViewById(R.id.search_data)
        searchDataOutput = view.findViewById(R.id.search_data_output)
        search = view.findViewById(R.id.search_btn)
        navigateBack = view.findViewById(R.id.navigate_information_back)
        progressBar = view.findViewById(R.id.information_progress_bar)
        recyclerView.adapter = allNutritionAdapter
        allNutritionAdapter.listener = this
        try {
            AzureData.configure(
                requireContext(),
                ACCOUNT_NAME,
                BuildConfig.COSMOS_KEY,
                PermissionMode.All
            )
            lifecycleScope.launch {
                args.authResult?.id?.let {
                    viewModel.createCollection(
                        it,
                        PERSONAL_DATABASE,
                        PARTITION_KEY
                    )
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "The exception is $e")
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val input = searchData.text.toString()

            if (input.isEmpty()) {
                searchDataOutput.error = getString(R.string.field_error)
                return@setOnClickListener
            }

            val query = Query.select()
                .from(COLLECTION_ID)
                .where(PROPERTY_NAME, input)
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.queryDocument(COLLECTION_ID, ALL_NUTRITION_DATABASE_NAME, query)
            }
        }

        navigateBack.setOnClickListener {
            val directions =
                NutritionFragmentDirections.actionNutritionFragmentToHomeFragment(args.authResult)
            findNavController().navigate(directions)
        }


        addData.setOnClickListener {
            if (selectedNutritionData != null) {
                selectedNutritionData.forEach { nutritionData ->
                    key = UUID.randomUUID().toString()
                    val cal = Calendar.getInstance()
                    val customDateValue = cal.time
                    nutritionData.date = customDateValue
                    nutritionData.id = key

                    lifecycleScope.launch {
                        args.authResult?.id?.let { id ->
                            viewModel.createDocument(
                                nutritionData, id,
                                PERSONAL_DATABASE, key
                            )
                        }
                    }
                }
                Toast.makeText(context,"Data Added Successfully",Toast.LENGTH_SHORT).show()
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.nutritionData.collect { viewState ->
                    when (viewState) {

                        is NutritionState.NutritionDataList -> {
                            when (viewState.listOfNutritionData) {
                                is Success -> {
                                    progressBar.visibility = View.GONE
                                    val list = (viewState.listOfNutritionData as?
                                            Success<ListResponse<out NutritionDataDocument>>)?.data?.resource?.items
                                    nutritionList = list as MutableList<NutritionDataDocument>?
                                    nutritionList?.let { allNutritionAdapter.getData(it) }
                                }
                                is Error -> {
                                    progressBar.visibility = View.GONE
                                    Log.d(
                                        TAG,
                                        "The error in getting all data is " +
                                                "${((viewState.listOfNutritionData as Error).exception)}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onRecyclerViewItemClicked(product: NutritionDataDocument) {
        if (nutritionList?.isNotEmpty() == true) {
            if (nutritionList?.contains(product) == true) {
                val index = nutritionList?.indexOf(product)
                val data = index?.let {
                    nutritionList!![it]
                }
                if (index != null) {
                    nutritionList!!.removeAt(index)
                }

                data?.selected = true
                if (data != null) {
                    nutritionList!!.add(index, data)
                }
                allNutritionAdapter.getData(nutritionList!!)
            }
        }
        selectedNutritionData.add(product)
    }

    override fun onRecyclerViewItemClickRemove(product: NutritionDataDocument) {
        if (nutritionList?.isNotEmpty() == true) {
            if (nutritionList?.contains(product) == true) {
                val index = nutritionList?.indexOf(product)
                val data = index?.let {
                    nutritionList!![it]
                }
                if (index != null) {
                    nutritionList!!.removeAt(index)
                }
                if (data?.selected == true) {
                    data.selected = false
                }
                if (data != null) {
                    nutritionList!!.add(index, data)
                }

                allNutritionAdapter.getData(nutritionList!!)
            }
        }
        selectedNutritionData.remove(product)
    }

    companion object {
        private const val COLLECTION_ID = "collection"
        private const val ALL_NUTRITION_DATABASE_NAME = "nutrition"
        private const val PERSONAL_DATABASE = "personal"
        private const val PARTITION_KEY = "/id"
        private const val PROPERTY_NAME = "name"
        private const val ACCOUNT_NAME = "karuneshcosmos"
        private const val TAG ="NutritionFragment"
    }
}
