package com.karunesh.azureauth.presentation.authentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.karunesh.azureauth.R
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import dagger.hilt.android.AndroidEntryPoint
import java.util.*



@AndroidEntryPoint
class AuthenticationFragment : Fragment() {

    private lateinit var authenticate: MaterialButton
    private lateinit var progressBar: CircularProgressIndicator
    private var b2cApp: IMultipleAccountPublicClientApplication? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_authentication, container, false)
        authenticate = view.findViewById(R.id.authenticate)
        progressBar = view.findViewById(R.id.authenticate_progress_bar)
        PublicClientApplication.createMultipleAccountPublicClientApplication(requireContext(),
            R.raw.auth_config_b2c,
            object : IPublicClientApplication.IMultipleAccountApplicationCreatedListener {
                override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                    b2cApp = application
                }

                override fun onError(exception: MsalException) {
                    progressBar.visibility = View.GONE

                    showToast("There was problem parsing your request. Please try again later ")
                    Log.d(TAG, "The exception in setting up the auth.json is $exception ")
                }
            })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authenticate.setOnClickListener(View.OnClickListener {
            if (b2cApp == null) {
                progressBar.visibility = View.GONE

                showToast("There was problem parsing your request. Please try again later ")
                return@OnClickListener
            }
            progressBar.visibility = View.VISIBLE
            val parameters = AcquireTokenParameters.Builder()
                .startAuthorizationFromActivity(activity)
                .withScopes(B2CConfiguration.scopes)
                .withPrompt(Prompt.LOGIN)
                .withCallback(authInteractiveCallback)
                .build()

            b2cApp!!.acquireToken(parameters)
        })
    }

    private val authInteractiveCallback: AuthenticationCallback
        get() = object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                displayResult(authenticationResult)
            }

            override fun onError(exception: MsalException) {

                progressBar.visibility = View.GONE

                showToast("There was problem parsing your request. Please try again later ")
                val B2C_PASSWORD_CHANGE = "AADB2C90118"
                if (exception.message!!.contains(B2C_PASSWORD_CHANGE)) {
                    return
                }

                if (exception is MsalClientException) {
                } else if (exception is MsalServiceException) {
                }
            }

            override fun onCancel() {
                Log.d(TAG, "User cancelled login.")
            }
        }


    private fun displayResult(result: IAuthenticationResult) {


        showToast("Successfully authenticated")

        val authenticationResult = AuthenticationResults()
        authenticationResult.id = result.account.claims?.get("sub") as String?
        authenticationResult.name = result.account.claims?.get("given_name") as String?
        authenticationResult.city = result.account.claims?.get("city") as String?
        authenticationResult.country = result.account.claims?.get("country") as String?
        authenticationResult.address = result.account.claims?.get("streetAddress") as String?
        authenticationResult.surname = result.account.claims?.get("family_name") as String?
        progressBar.visibility = View.GONE

        if (authenticationResult.id != null) {
            val directions =
                AuthenticationFragmentDirections.actionAuthenticationFragmentToHomeFragment(
                    authenticationResult
                )
            findNavController().navigate(directions)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val TAG ="AuthenticationFragment"
    }


}