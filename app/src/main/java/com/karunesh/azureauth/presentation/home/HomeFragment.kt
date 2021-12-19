package com.karunesh.azureauth.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.card.MaterialCardView
import com.karunesh.azureauth.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var navigateToRecommendationDiet: MaterialCardView
    private lateinit var navigateToNutrition: MaterialCardView
    private lateinit var navigateToImageFragment: MaterialCardView
    private lateinit var navigateToSupportFragment: MaterialCardView
    private lateinit var navigateToListImagesFragment: MaterialCardView
    private lateinit var navigateToHistory: MaterialCardView
    private lateinit var nameTxt: TextView
    private val args by navArgs<HomeFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        navigateToRecommendationDiet = view.findViewById(R.id.recommendation_diet)
        navigateToNutrition = view.findViewById(R.id.navigate_nutrition)
        navigateToImageFragment = view.findViewById(R.id.navigate_to_imageFrag)
        navigateToSupportFragment = view.findViewById(R.id.navigate_to_support)
        navigateToListImagesFragment = view.findViewById(R.id.navigate_captured_images)
        navigateToHistory = view.findViewById(R.id.nutrition_history)
        nameTxt = view.findViewById(R.id.name_home)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTxt.text = args.authResults?.name

        navigateToRecommendationDiet.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToDietFragment(args.authResults)
            findNavController().navigate(directions)
        }
        navigateToNutrition.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToNutritionFragment(args.authResults)
            findNavController().navigate(directions)
        }

        navigateToImageFragment.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToImageFragment(
                    args.authResults
                )
            findNavController().navigate(directions)
        }

        navigateToSupportFragment.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToSupportFragment(args.authResults)
            findNavController().navigate(directions)
        }

        navigateToListImagesFragment.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToViewCapturedImagesFragment(args.authResults)
            findNavController().navigate(directions)
        }

        navigateToHistory.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToNutritionHistoryFragment(args.authResults)
            findNavController().navigate(directions)
        }


    }


}