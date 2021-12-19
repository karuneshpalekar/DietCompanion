package com.karunesh.azureauth.presentation.authentication

import com.karunesh.azureauth.presentation.authentication.B2CConfiguration.getAuthorityFromPolicyName
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication
import com.microsoft.identity.client.SilentAuthenticationCallback
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalUiRequiredException
import com.microsoft.identity.common.internal.providers.oauth2.IDToken
import java.util.*

class B2CUser private constructor() {
    private val accounts: MutableList<IAccount> = ArrayList()

    val displayName: String?
        get() {
            if (accounts.isEmpty()) {
                return null
            }

            // Make sure that all of your policies are returning the same set of claims.
            val displayName = getB2CDisplayNameFromAccount(accounts[0])
            return displayName ?: getSubjectFromAccount(accounts[0])
        }

    fun acquireTokenSilentAsync(
        multipleAccountPublicClientApplication: IMultipleAccountPublicClientApplication,
        policyName: String,
        scopes: List<String?>?,
        callback: SilentAuthenticationCallback
    ) {
        for (account in accounts) {
            if (policyName.equals(getB2CPolicyNameFromAccount(account), ignoreCase = true)) {
                val parameters = AcquireTokenSilentParameters.Builder()
                    .fromAuthority(getAuthorityFromPolicyName(policyName))
                    .withScopes(scopes)
                    .forAccount(account)
                    .withCallback(callback)
                    .build()
                multipleAccountPublicClientApplication.acquireTokenSilentAsync(parameters)
                return
            }
        }
        callback.onError(
            MsalUiRequiredException(
                MsalUiRequiredException.NO_ACCOUNT_FOUND,
                "Account associated to the policy is not found."
            )
        )
    }

    fun signOutAsync(
        multipleAccountPublicClientApplication: IMultipleAccountPublicClientApplication,
        callback: IMultipleAccountPublicClientApplication.RemoveAccountCallback
    ) {
        Thread {
            try {
                for (account in accounts) {
                    multipleAccountPublicClientApplication.removeAccount(account)
                }
                accounts.clear()
                callback.onRemoved()
            } catch (e: MsalException) {
                callback.onError(e)
            } catch (e: InterruptedException) {
                // Unexpected.
            }
        }.start()
    }

    companion object {
        fun getB2CUsersFromAccountList(accounts: List<IAccount>): List<B2CUser> {
            val b2CUserHashMap = HashMap<String?, B2CUser>()
            for (account in accounts) {
                val subject = getSubjectFromAccount(account)
                var user = b2CUserHashMap[subject]
                if (user == null) {
                    user = B2CUser()
                    b2CUserHashMap[subject] = user
                }
                user.accounts.add(account)
            }
            val users: MutableList<B2CUser> = ArrayList()
            users.addAll(b2CUserHashMap.values)
            return users
        }

        private fun getB2CPolicyNameFromAccount(account: IAccount): String? {
            return account.claims!!["tfp"] as String?
                ?: // Fallback to "acr" (for older policies)
                return account.claims!!["acr"] as String?
        }

        private fun getSubjectFromAccount(account: IAccount): String? {
            return account.claims!![IDToken.SUBJECT] as String?
        }

        private fun getB2CDisplayNameFromAccount(account: IAccount): String? {
            val displayName = account.claims!![IDToken.NAME] ?: return null
            return displayName.toString()
        }
    }
}