package com.karunesh.azureauth.presentation.authentication

import java.util.*

object B2CConfiguration {

    val Policies = arrayOf(
        "B2C_1_signupsignin1"
    )
    const val azureAdB2CHostName = "karuneshb2c.b2clogin.com"
    const val tenantName = "karuneshb2c.onmicrosoft.com"
    fun getAuthorityFromPolicyName(policyName: String): String {
        return "https://" + azureAdB2CHostName + "/" + tenantName + "/" + policyName + "/"
    }

    val scopes: List<String>
        get() = Arrays.asList(
            "https://karuneshb2c.onmicrosoft.com/tasks-api/tasks.read",
            "https://karuneshb2c.onmicrosoft.com/tasks-api/tasks.write"
        )
}