package com.karunesh.azureauth.presentation.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karunesh.azureauth.BuildConfig
import com.karunesh.azureauth.R
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.Flags
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.fragment_diet_details.*

class SupportFragment : Fragment() {

    private lateinit var ratingsSpinner: AppCompatSpinner
    private lateinit var dietSpinner: AppCompatSpinner
    private lateinit var submit: MaterialButton
    private lateinit var backButton: ImageButton
    private lateinit var edAge: TextInputEditText
    private lateinit var edAgeOutput: TextInputLayout
    private lateinit var edWeight: TextInputEditText
    private lateinit var edWeightOutput: TextInputLayout
    private var ratingValue: String = ""
    private var dietValue: String = ""
    private val args: SupportFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_support, container, false)
        ratingsSpinner = view.findViewById(R.id.ratings_spinner)
        dietSpinner = view.findViewById(R.id.diet_spinner)
        submit = view.findViewById(R.id.submit_application_insight)
        backButton = view.findViewById(R.id.navigate_support_back)
        edAge = view.findViewById(R.id.age_ed_txt)
        edAgeOutput = view.findViewById(R.id.age_ed)
        edWeight = view.findViewById(R.id.weight_ed_txt)
        edWeightOutput = view.findViewById(R.id.weight_ed)
        AppCenter.start(
            activity?.application, BuildConfig.APP_SECRET,
            Analytics::class.java, Crashes::class.java
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener {
            val directions =
                SupportFragmentDirections.actionSupportFragmentToHomeFragment(args.authResults)
            findNavController().navigate(directions)
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ratings_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            ratingsSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.diet_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dietSpinner.adapter = adapter
        }

        submit.setOnClickListener {
            val age = edAge.text.toString()
            val weight = edWeight.text.toString()

            if (age.isEmpty()) {
                edAgeOutput.error = getString(R.string.field_error)
                return@setOnClickListener
            }

            if (weight.isEmpty()) {
                edWeightOutput.error = getString(R.string.field_error)
                return@setOnClickListener
            }

            val properties: MutableMap<String, String> = HashMap()
            properties["age"] = age
            properties["weight"] = weight
            properties["rating"] = ratingValue
            properties["dietValue"] = dietValue

            Analytics.trackEvent("eventName", properties, Flags.CRITICAL)
            Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show()
        }

        ratingsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                ratingValue = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                ratingValue = "1"
            }

        }

        dietSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                dietValue = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                dietValue = "MayoClinic"
            }

        }
    }


}