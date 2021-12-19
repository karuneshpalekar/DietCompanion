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
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.recommendation.RecommendationClient
import com.karunesh.azureauth.presentation.recommendation.diet.adapter.RecommendDietAdapter
import com.karunesh.azureauth.presentation.recommendation.diet.adapter.RecommendListener
import com.karunesh.azureauth.presentation.recommendation.util.Config
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecommendedDietFragment : Fragment(), RecommendListener {


    private var config = Config()
    private var client: RecommendationClient? = null
    private lateinit var recommendRecyclerView: RecyclerView
    private val viewModel: DietViewModel by activityViewModels()
    private val recommendDietAdapter = RecommendDietAdapter()
    private lateinit var progressBar: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = RecommendationClient(requireContext(), config)
        lifecycleScope.launch {
            client?.load()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recommended_diet, container, false)
        recommendRecyclerView =
            view.findViewById<RecyclerView>(R.id.recommendation_diet_recycler_view)
        progressBar = view.findViewById(R.id.recommend_progress_bar)
        recommendRecyclerView.adapter = recommendDietAdapter
        recommendDietAdapter.listener = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.VISIBLE
        viewModel.selectedMovies.observe(viewLifecycleOwner, { selectedMovie ->
            lifecycleScope.launch {
                delay(2000)
                client?.recommend(selectedMovie)?.let {
                    recommendDietAdapter.getData(it)
                    progressBar.visibility = View.GONE
                }

            }
        })
    }

    override fun onRecyclerViewItemClicked(product: DietItem) {
        val directions = DietFragmentDirections.actionDietFragmentToDietDetailsFragment(product)
        findNavController().navigate(directions)
    }


}