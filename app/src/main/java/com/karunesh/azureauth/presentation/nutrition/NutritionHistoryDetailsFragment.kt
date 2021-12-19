package com.karunesh.azureauth.presentation.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.karunesh.azureauth.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NutritionHistoryDetailsFragment : Fragment() {

    private val args: NutritionHistoryDetailsFragmentArgs by navArgs()
    private lateinit var nutritionDate: TextView
    private lateinit var nutritionName: TextView
    private lateinit var nutritionDescription: TextView
    private lateinit var nutritionAlphaCarotene: TextView
    private lateinit var nutritionBetaCarotene: TextView
    private lateinit var nutritionBetaCryptoxanthin: TextView
    private lateinit var nutritionCarbohydrate: TextView
    private lateinit var nutritionCholestrol: TextView
    private lateinit var nutritionCholine: TextView
    private lateinit var nutritionFiber: TextView
    private lateinit var nutritionProtein: TextView
    private lateinit var nutritionSugar: TextView
    private lateinit var nutritionWater: TextView
    private lateinit var nutritionSaturatedFat: TextView
    private lateinit var nutritionCopper: TextView
    private lateinit var nutritionIron: TextView
    private lateinit var nutritionVitaminC: TextView
    private lateinit var nutritionVitaminE: TextView
    private lateinit var nutritionVitaminK: TextView
    private lateinit var nutritionVitaminB12: TextView
    private lateinit var nutritionVitaminB6: TextView
    private lateinit var navigateBack: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_nutrition_history_details, container, false)
        nutritionDate = view.findViewById(R.id.nutrition_detail_date)
        nutritionName = view.findViewById(R.id.nutrition_detail_name_txt)
        nutritionDescription = view.findViewById(R.id.nutrition_detail_description_txt)
        nutritionAlphaCarotene = view.findViewById(R.id.nutrition_detail_alpha_carotene_txt)
        nutritionBetaCarotene = view.findViewById(R.id.nutrition_detail_beta_carotene_txt)
        nutritionBetaCryptoxanthin = view.findViewById(R.id.nutrition_detail_betaCryptoxanthin_txt)
        nutritionCarbohydrate = view.findViewById(R.id.nutrition_detail_carbohydrate_txt)
        nutritionCholestrol = view.findViewById(R.id.nutrition_detail_cholestrol_txt)
        nutritionCholine = view.findViewById(R.id.nutrition_detail_choline_txt)
        nutritionFiber = view.findViewById(R.id.nutrition_detail_fiber_txt)
        nutritionProtein = view.findViewById(R.id.nutrition_detail_protein_txt)
        nutritionSugar = view.findViewById(R.id.nutrition_detail_sugar_txt)
        nutritionWater = view.findViewById(R.id.nutrition_detail_water_txt)
        nutritionSaturatedFat = view.findViewById(R.id.nutrition_detail_saturated_txt)
        nutritionCopper = view.findViewById(R.id.nutrition_detail_copper_txt)
        nutritionIron = view.findViewById(R.id.nutrition_detail_iron_txt)
        nutritionVitaminC = view.findViewById(R.id.nutrition_detail_vitaminC_txt)
        nutritionVitaminE = view.findViewById(R.id.nutrition_detail_vitaminE_txt)
        nutritionVitaminK = view.findViewById(R.id.nutrition_detail_vitaminK_txt)
        nutritionVitaminB12 = view.findViewById(R.id.nutrition_detail_vitaminB12_txt)
        nutritionVitaminB6 = view.findViewById(R.id.nutrition_detail_vitaminB6_txt)
        navigateBack = view.findViewById(R.id.navigate_nutrition_history_detail_back)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nutritionDate.text = args.historyDetail?.date.toString()
        nutritionName.text = args.historyDetail?.name
        nutritionDescription.text = args.historyDetail?.description
        nutritionAlphaCarotene.text = args.historyDetail?.alphaCarotene
        nutritionBetaCarotene.text = args.historyDetail?.betaCarotene
        nutritionBetaCryptoxanthin.text = args.historyDetail?.betaCryptoxanthin
        nutritionCarbohydrate.text = args.historyDetail?.carbohydrate
        nutritionCholestrol.text = args.historyDetail?.cholestrol
        nutritionCholine.text = args.historyDetail?.choline
        nutritionFiber.text = args.historyDetail?.fiber
        nutritionProtein.text = args.historyDetail?.protein
        nutritionSugar.text = args.historyDetail?.sugar
        nutritionWater.text = args.historyDetail?.water
        nutritionSaturatedFat.text = args.historyDetail?.saturatedFat
        nutritionCopper.text = args.historyDetail?.copper
        nutritionIron.text = args.historyDetail?.iron
        nutritionVitaminC.text = args.historyDetail?.vitaminC
        nutritionVitaminE.text = args.historyDetail?.vitaminE
        nutritionVitaminK.text = args.historyDetail?.vitaminK
        nutritionVitaminB6.text = args.historyDetail?.vitaminB6
        nutritionVitaminB12.text = args.historyDetail?.vitaminB12

        navigateBack.setOnClickListener {
            val directions =
                NutritionHistoryDetailsFragmentDirections.actionNutritionHistoryDetailsFragmentToNutritionHistoryFragment(
                    args.historyDetail?.authResults
                )
            findNavController().navigate(directions)
        }
    }


}