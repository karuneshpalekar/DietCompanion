package com.karunesh.azureauth.presentation.recommendation.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.recommendation.diet.adapter.AllDietAdapter
import com.karunesh.azureauth.presentation.recommendation.diet.adapter.AllDietListener
import com.karunesh.azureauth.presentation.recommendation.util.Config
import com.karunesh.azureauth.presentation.recommendation.util.getJsonDataFromAsset
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllDietFragment : Fragment(), AllDietListener {

    private val ratings = arrayOf("1", "2", "3", "4", "5")
    private val items: MutableList<DietItem> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private val allDietAdapter: AllDietAdapter = AllDietAdapter()
    val viewModel: DietViewModel by activityViewModels()
    private var selectedMovie: MutableList<DietItem> = mutableListOf(DietItem())
    private var adapterList: MutableList<DietItem>? = null
    private lateinit var progressBar: CircularProgressIndicator
    private val checkedItem = 1
    var rating: Float = 1f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_diet, container, false)
        recyclerView = view.findViewById(R.id.all_diet_recycler_view)
        progressBar = view.findViewById(R.id.all_diet_progress_bar)
        recyclerView.adapter = allDietAdapter
        allDietAdapter.listener = this
        progressBar.visibility = View.VISIBLE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            adapterList = getContent() as MutableList<DietItem>?
            adapterList?.let {
                allDietAdapter.getData(it)
            }
            progressBar.visibility = View.GONE

        }
    }

    private suspend fun getContent(): List<DietItem> {
        if (items.isEmpty()) {
            val jsonFileString = getJsonDataFromAsset(requireContext(), Config().movieListPath)
            val gson = Gson()
            val listPersonType = object : TypeToken<List<DietItem>>() {}.type
            items.addAll(gson.fromJson(jsonFileString, listPersonType))
        }
        return items
    }

    override fun onRecyclerViewItemCardViewClicked(product: DietItem) {
        val directions = DietFragmentDirections.actionDietFragmentToDietDetailsFragment(product)
        findNavController().navigate(directions)
    }

    override fun onRecyclerViewItemClicked(product: DietItem) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.rateeffilike))
            .setNeutralButton(getString(R.string.cancel)) { _, _ ->
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->

                if (adapterList != null) {
                    if (adapterList!!.contains(product)) {
                        val index = adapterList!!.indexOf(product)
                        val data = adapterList!![index]
                        adapterList!!.removeAt(index)
                        data.selected = true
                        data.rating = rating
                        adapterList?.add(index, data)
                        selectedMovie.add(data)
                        viewModel.addSelectedMovie(selectedMovie)
                        allDietAdapter.getData(adapterList!!)
                    }
                }

            }
            .setSingleChoiceItems(ratings, checkedItem) { _, which ->
                rating = ratings[which].toFloat()
            }
            .show()


    }

    override fun onRecyclerViewItemClickRemove(product: DietItem) {
        selectedMovie.remove(product)
        if (adapterList?.isNotEmpty() == true) {
            if (adapterList!!.contains(product)) {
                val index = adapterList!!.indexOf(product)
                val data = adapterList!![index]
                adapterList!!.removeAt(index)
                if (data.selected == true) {
                    data.selected = false
                }
                adapterList!!.add(index, data)
                allDietAdapter.getData(adapterList!!)
            }
        }
        viewModel.addSelectedMovie(selectedMovie)
    }


}